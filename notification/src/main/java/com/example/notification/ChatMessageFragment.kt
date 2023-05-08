package com.example.notification

import android.view.Gravity
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.log.LaterLog
import com.example.common.room.entities.MessageEntity
import com.example.common.utils.FragmentStackUtil
import com.example.notification.databinding.FragmentChatMessageBinding
import com.example.notification.entity.MsgResponseCode
import com.example.notification.recyclerview.ChatRecyclerViewAdapter
import com.example.notification.recyclerview.ConversationDividerItemDecoration
import com.example.notification.viewmodel.MsgViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Route(
    path = RoutePathConstant.ChatMessageFragment,
    description = "The entrance fragment of chat message"
)
class ChatMessageFragment : BaseFragment<FragmentChatMessageBinding>(FragmentChatMessageBinding::inflate) {
    private lateinit var viewModel: MsgViewModel
    private lateinit var msgRVAdapter: ChatRecyclerViewAdapter
    private val msgList = mutableListOf<MessageEntity>()

    @Autowired(name = "conversationId")
    lateinit var conversationId: String

    @Autowired(name = "conversationName")
    lateinit var conversationName: String

    override fun onCreateView() {
        initToolbar()
        initObject()
        initMsgList()
        setClickListener()
    }

    private fun initMsgList(){
        viewLifecycleOwner.lifecycleScope.launch {
            msgList.addAll(viewModel.getMsgListByConversationId(conversationId.toLong()))
            msgRVAdapter.addMsgList(msgList)
            if (msgList.isNotEmpty()) viewBinding.chatHistoryRecyclerView.smoothScrollToPosition(msgList.size - 1)
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(requireActivity())[MsgViewModel::class.java]
        msgRVAdapter = ChatRecyclerViewAdapter()
        viewBinding.chatHistoryRecyclerView.adapter = msgRVAdapter
    }

    private fun setClickListener(){
        viewBinding.sendButton.setOnClickListener {
            val msgSend = MessageEntity(
                messageConversationId = conversationId.toLong(),
                messageContent = viewBinding.messageInput.text.toString(),
                messageSender = "user",
                messageIsSender = true
            )
            msgList.add(msgSend)
            msgRVAdapter.addMsg(msgSend)
            viewBinding.messageInput.text?.clear()

            val receiverContent = ""
            val msgReceiver = MessageEntity(
                messageConversationId = conversationId.toLong(),
                messageContent = receiverContent,
                messageSender = "assistant",
                messageIsSender = false
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewBinding.chatHistoryRecyclerView.smoothScrollToPosition(msgList.size - 1)
                viewModel.sendMsg(msgSend).collect{ msgResponse ->
                    when (msgResponse.code){
                        MsgResponseCode.MESSAGE -> {
                            viewBinding.chatHistoryRecyclerView.layoutManager?.findViewByPosition(msgRVAdapter.itemCount - 1)?.let {
                                val messageBodyTV = it.findViewById<TextView>(R.id.chat_message_body)
                                // 移除上一条消息末尾的下划线字符
                                if (messageBodyTV.text.isNotEmpty() && messageBodyTV.text.last() == '_') {
                                    messageBodyTV.text = messageBodyTV.text.substring(0, messageBodyTV.text.length - 1)
                                }
                                // 添加新消息
                                messageBodyTV.append(msgResponse.msg)
                                receiverContent.plus(msgResponse.msg)
                                // 在消息未结束之前，在文本末尾显示输入的光标
                                messageBodyTV.append("_")
                            }

                        }
                        MsgResponseCode.ROLE -> {
                            msgReceiver.messageSender = msgResponse.msg
                        }
                        MsgResponseCode.CONTINUE -> {}
                        MsgResponseCode.START -> {
                            msgList.add(msgReceiver)
                            msgRVAdapter.addMsg(msgReceiver)
                            viewBinding.chatHistoryRecyclerView.smoothScrollToPosition(msgList.size - 1)
                        }
                        MsgResponseCode.DONE -> {
                            viewBinding.chatHistoryRecyclerView.layoutManager?.findViewByPosition(msgRVAdapter.itemCount - 1)?.let {
                                val messageBodyTV = it.findViewById<TextView>(R.id.chat_message_body)
                                // 移除消息末尾的下划线字符
                                if (messageBodyTV.text.isNotEmpty() && messageBodyTV.text.last() == '_') {
                                    messageBodyTV.text = messageBodyTV.text.substring(0, messageBodyTV.text.length - 1)
                                }
                            }
                        }
                        MsgResponseCode.ERROR -> {
                            viewBinding.chatHistoryRecyclerView.layoutManager?.findViewByPosition(msgRVAdapter.itemCount - 1)?.let {
                                val messageBodyTV = it.findViewById<TextView>(R.id.chat_message_body)
                                // 移除消息末尾的下划线字符
                                if (messageBodyTV.text.isNotEmpty() && messageBodyTV.text.last() == '_') {
                                    messageBodyTV.text = messageBodyTV.text.substring(0, messageBodyTV.text.length - 1)
                                }
                                messageBodyTV.append(msgResponse.msg)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar(){
        viewBinding.chatToolbarTitle.text = conversationName
        viewBinding.chatToolbarTitle.gravity = Gravity.CENTER

        viewBinding.toolbar.navigationIcon = resources.getDrawable(com.example.common.R.drawable.baseline_arrow_back_24)
        viewBinding.toolbar.setNavigationOnClickListener {
            FragmentStackUtil.goBack()
        }
    }
}