package com.example.common.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.common.room.dao.ConversationDao
import com.example.common.room.entities.ConversationEntity
import com.example.common.room.entities.MessageEntity

@Database(entities = [ConversationEntity::class, MessageEntity::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
        abstract fun conversationDao(): ConversationDao
}