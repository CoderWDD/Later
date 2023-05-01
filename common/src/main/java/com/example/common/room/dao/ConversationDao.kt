package com.example.common.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.common.room.entities.ConversationEntity
import com.example.common.room.entities.ConversationWithMessageList
import com.example.common.room.entities.MessageEntity

@Dao
interface ConversationDao {
    @Transaction
    @Query("SELECT * FROM conversation WHERE conversation_id = :conversationId")
    fun getConversationWithMessageList(conversationId: Long): LiveData<ConversationWithMessageList>

    // Conversation related methods
    @Insert
    fun insertConversation(conversation: ConversationEntity): Long

    @Update
    fun updateConversation(conversation: ConversationEntity)

    @Delete
    fun deleteConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM conversation")
    fun getAllConversations(): LiveData<List<ConversationEntity>>

    // Message related methods
    @Insert
    fun insertMessage(message: MessageEntity): Long

    @Update
    fun updateMessage(message: MessageEntity)

    @Delete
    fun deleteMessage(message: MessageEntity)
}