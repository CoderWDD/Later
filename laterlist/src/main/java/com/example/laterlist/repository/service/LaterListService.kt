package com.example.laterlist.repository.service

import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterTagEntity
import com.example.common.entity.LaterViewItem
import com.example.common.reporesource.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface LaterListService {
    fun createFolder(laterFolderEntity: LaterFolderEntity): SharedFlow<Resource<String>>

    fun getFavoriteFolderList(): MutableSharedFlow<Resource<List<LaterFolderEntity>>>

    fun getRecycleBinFolderList(): MutableSharedFlow<Resource<List<LaterFolderEntity>>>

    fun deleteFavoriteFolder(folderPath: String): Flow<Resource<String>>

    fun deleteRecycleBinFolder(folderPath: String): Flow<Resource<String>>

    fun moveFolderToRecycleBin(folderPath: String): Flow<Resource<String>>

    fun getTodayLaterViewItemList(): MutableSharedFlow<Resource<List<LaterViewItem>>>

    fun getFavoriteLaterViewItemList(): MutableSharedFlow<Resource<List<LaterViewItem>>>

    fun createLaterViewItem(folderPath: String, laterViewItem: LaterViewItem): Flow<Resource<String>>

    fun createLaterTag(tag: LaterTagEntity): Flow<Resource<String>>

    fun getTagsList(): MutableSharedFlow<Resource<List<LaterTagEntity>>>

    fun deleteTag(tag: String): Flow<Resource<String>>

    fun onClear()
}