package com.example.laterlist.repository.service

import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterViewItem
import com.example.common.reporesource.Resource
import kotlinx.coroutines.flow.Flow

interface LaterListService {
    fun createFolder(laterFolderEntity: LaterFolderEntity): Flow<Resource<String>>

    fun getFavoriteFolderList(): Flow<Resource<List<LaterFolderEntity>>>

    fun getRecycleBinFolderList(): Flow<Resource<List<LaterFolderEntity>>>

    fun deleteFavoriteFolder(folderPath: String): Flow<Resource<String>>

    fun deleteRecycleBinFolder(folderPath: String): Flow<Resource<String>>

    fun moveFolderToRecycleBin(folderPath: String): Flow<Resource<String>>

    fun getTodayLaterViewItemList(): Flow<Resource<List<LaterViewItem>>>

    fun getFavoriteLaterViewItemList(): Flow<Resource<List<LaterViewItem>>>

    fun createLaterViewItem(folderPath: String, laterViewItem: LaterViewItem): Flow<Resource<String>>

    fun createLaterTag(tag: String): Flow<Resource<String>>


    fun onClear()
}