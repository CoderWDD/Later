package com.example.notification.repository

import com.example.common.constants.ChatConstants
import com.example.common.constants.HTTPConstants
import com.example.common.entity.ChatRequest
import com.example.common.entity.ChatResponseStream
import com.example.common.entity.Choice
import com.example.common.entity.MessageItem
import com.example.common.network.RetrofitClient
import com.example.common.network.service.OpenAiChatService
import com.example.common.room.ConversationDBManager
import com.example.common.room.dao.ConversationDao
import com.example.common.room.entities.ConversationEntity
import com.example.common.room.entities.MessageEntity
import com.example.notification.entity.MsgResponse
import com.example.notification.entity.MsgResponseCode
import com.example.notification.repository.service.MsgService
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MsgRepository: MsgService {
    private val chatServiceClient: OpenAiChatService by lazy { RetrofitClient.chatServiceClient }
    private val conversationDao: ConversationDao by lazy { ConversationDBManager.instance.conversationDao() }

    override suspend fun sendMsg(msg: MessageEntity): Flow<MsgResponse> = flow {
        emit(MsgResponse())
        // 保存发送的消息到数据库
        conversationDao.insertMessage(msg)

        // TODO 设置请求模型
        val chatRequest = ChatRequest(model = "")

        // 每次只携带最新的 MSG_ITEM_SIZE 条消息
        val msgList = getLastMsgListByNumInConversation(num = ChatConstants.MSG_ITEM_SIZE, conversationId = msg.messageConversationId)
        chatRequest.messages.clear()
        chatRequest.messages.addAll(msgList)
        val responseBody = chatServiceClient.getChatResponseByRequest(chatRequest)

        // 请求失败, 返回错误信息
        if (responseBody.code() != HTTPConstants.HTTP_OK) {
            emit(MsgResponse(code = MsgResponseCode.ERROR, msg = responseBody.message()))
            return@flow
        }

        val responseContent: String = ""
        var responseRole: String = "assistant"

        // 请求成功, 返回聊天内容
        responseBody.body()?.byteStream()?.bufferedReader()?.useLines { lines ->
            lines.forEach { line ->
                if (line.startsWith("{")) {
                    // 解析返回的结果
                    Moshi.Builder().build().adapter(ChatResponseStream::class.java).fromJson(line)?.let { chatResponseStream ->
                        chatResponseStream.choices.forEach {choice: Choice ->
                            // 根据返回的结果, 发送不同的消息
                            if (choice.delta.content != null) {
                                responseContent.plus(choice.delta.content!!)
                                emit(MsgResponse(code = MsgResponseCode.MESSAGE, msg = choice.delta.content!!))
                            }
                            else if (choice.delta.role != null) {
                                responseRole = choice.delta.role!!
                                emit(MsgResponse(code = MsgResponseCode.ROLE, msg = choice.delta.role!!))
                            }
                            if (choice.finish_reason != null){
                                emit(MsgResponse(code = MsgResponseCode.CONTINUE))
                            }
                        }
                    }
                }
                else if (line.startsWith("[")){
                    // done
                    // 只有成功才会存入数据库
                    val responseMsg = MessageEntity(messageConversationId = msg.messageConversationId, messageContent = responseContent, messageSender = responseRole, messageIsSender = false)
                    conversationDao.insertMessage(responseMsg)
                    emit(MsgResponse(code = MsgResponseCode.DONE, msg = "done"))
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMsgListByConversationId(conversationId: Long): List<MessageEntity> {
        val conversationWithMessageList = conversationDao.getConversationWithMessageList(conversationId = conversationId)
        return conversationWithMessageList.messageList
    }

    override suspend fun createConversation(conversationName: String): Long{
        val conversation = ConversationEntity(conversationName = conversationName)
        return conversationDao.insertConversation(conversation)
    }

    override suspend fun deleteConversation(conversation: ConversationEntity) {
        conversationDao.deleteConversation(conversation)
    }

    override suspend fun updateConversation(conversation: ConversationEntity) {
        if (conversation.conversationName.isEmpty()) return
        conversationDao.updateConversation(conversation)
    }

    override suspend fun getConversationList(): List<ConversationEntity> = conversationDao.getAllConversations()

    private suspend fun getLastMsgListByNumInConversation(num: Int, conversationId: Long): List<MessageItem> {
        val conversationWithMessageList = conversationDao.getConversationWithMessageList(conversationId = conversationId)
        return conversationWithMessageList.messageList.takeLast(num).map { msgEntity ->
            MessageItem(role = msgEntity.messageSender, content = msgEntity.messageContent)
        }
    }
}