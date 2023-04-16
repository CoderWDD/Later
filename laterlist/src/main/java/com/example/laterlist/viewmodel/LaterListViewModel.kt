package com.example.laterlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterViewItem
import com.example.common.reporesource.Resource
import com.example.laterlist.repository.LaterListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LaterListViewModel: ViewModel() {
    private val laterListRepository: LaterListRepository by lazy { LaterListRepository(viewModelScope) }

    fun createFolder(folderEntity: LaterFolderEntity) = laterListRepository.createFolder(folderEntity)

    fun createTag(tag: String) = laterListRepository.createLaterTag(tag).flowOn(Dispatchers.IO)

    fun createWebsite(website: LaterViewItem) {
    }

    fun createImage(){

    }

    fun createVideo(){

    }

    fun getFavoriteFolderList() = laterListRepository.getFavoriteFolderList().asLiveData()

    fun getRecentFolderList() = laterListRepository.getRecycleBinFolderList().asLiveData()

    fun getTagList() = laterListRepository.getTagsList().asLiveData()

    fun saveToFavorite(){

    }

    fun getFavoriteList() = laterListRepository.getFavoriteLaterViewItemList().asLiveData()

    fun getTodayList() = laterListRepository.getTodayLaterViewItemList().asLiveData()

    fun getListByFolder(){

    }

    fun getListByTag(){

    }

    fun deleteFolder(){

    }

    fun deleteTag(){

    }

    fun deleteLaterItem(){

    }

    fun updateFolder(){

    }

    fun updateTag(){

    }

    fun updateLaterItem(){

    }

    // clear the reference of instance which is not used
    override fun onCleared() {
        super.onCleared()
    }
}