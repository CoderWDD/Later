package com.example.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterTagEntity
import com.example.common.entity.LaterViewItem
import com.example.common.repository.LaterListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class LaterListViewModel: ViewModel() {
    private val laterListRepository: LaterListRepository by lazy { LaterListRepository(viewModelScope) }

    private val _sharedAction: MutableLiveData<LaterViewItem> = MutableLiveData()
    val sharedAction: LiveData<LaterViewItem> = _sharedAction

    fun emitSharedAction(laterViewItem: LaterViewItem){
        _sharedAction.value = laterViewItem
    }

    fun createFolder(folderEntity: LaterFolderEntity) = laterListRepository.createFolder(folderEntity)

    fun createTag(tag: LaterTagEntity) = laterListRepository.createLaterTag(tag).flowOn(Dispatchers.IO)

    fun createWebsite(website: LaterViewItem) = laterListRepository.createLaterViewItem(folderPath = website.folder, laterViewItem = website).asLiveData()

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

    fun getListByFolder(folderKey: String) = laterListRepository.getLaterViewItemByFolder(folderKey).asLiveData()

    fun getListByTag(tag: String) = laterListRepository.getLaterViewItemListByTag(tag = tag).asLiveData()

    fun deleteFavoriteFolder(folderKey: String) = laterListRepository.deleteFavoriteFolder(folderKey).asLiveData()

    fun deleteRecycleFolder(folderKey: String) = laterListRepository.deleteRecycleBinFolder(folderKey).asLiveData()

    fun deleteTag(tag: String) = laterListRepository.deleteTag(tag = tag)

    fun deleteLaterItem(favoriteFolderPath: String, laterViewItem: LaterViewItem) = laterListRepository.deleteLaterViewItem(favoriteFolderPath, laterViewItem).asLiveData()

    fun updateFolder(){

    }

    fun updateTag(){

    }

    fun updateLaterItem(favoriteFolderPath: String, laterViewItem: LaterViewItem) = laterListRepository.updateLaterViewItem(favoriteFolderPath, laterViewItem).asLiveData()

    // clear the reference of instance which is not used
    override fun onCleared() {
        super.onCleared()
    }
}