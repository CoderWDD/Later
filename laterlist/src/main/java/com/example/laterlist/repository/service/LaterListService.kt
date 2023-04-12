package com.example.laterlist.repository.service

import com.example.common.entity.LaterFolderEntity
import com.example.common.reporesource.Resource
import kotlinx.coroutines.flow.Flow

interface LaterListService {
    fun <ResultType> createFolder(laterFolderEntity: LaterFolderEntity): Flow<Resource<ResultType>>

    fun <ResultType> getFavoriteFolderList(): Flow<Resource<ResultType>>

    fun <ResultType> getRecycleBinFolderList(): Flow<Resource<ResultType>>


    fun onClear()
}