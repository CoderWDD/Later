package com.example.common.room

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.common.MyApplication
import com.example.common.log.LaterLog
import com.example.common.room.database.ChatDatabase
import java.io.Serializable

class ConversationDBManager private constructor() : Serializable {
    companion object {
        @JvmStatic
        val instance: ChatDatabase by lazy {
            Room
                .databaseBuilder(
                context = MyApplication.instance.applicationContext,
                ChatDatabase::class.java,
                "chatDatabase"
                ).build()
        }

        private object Callback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

    }
}