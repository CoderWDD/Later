package com.example.laterlist.repository.service

import com.example.common.entity.LaterFolderEntity
import com.example.common.unify.ResBody

interface LaterListService {
    fun<T> createFolder(laterFolderEntity: LaterFolderEntity): ResBody<T>

    fun onClear()
}