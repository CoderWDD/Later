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
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Transaction
    @Query("SELECT * FROM conversation WHERE conversation_id = :conversationId")
    suspend fun getConversationWithMessageList(conversationId: Long): ConversationWithMessageList

    @Query("SELECT * FROM conversation WHERE conversation_id = :conversationId")
    suspend fun getConversationById(conversationId: Long): ConversationEntity

    // Conversation related methods
    @Insert
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Update
    suspend fun updateConversation(conversation: ConversationEntity)

    @Delete
    suspend fun deleteConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM conversation")
    suspend fun getAllConversations(): List<ConversationEntity>

    // Message related methods
    @Insert
    suspend fun insertMessage(message: MessageEntity): Long

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)
}