package com.example.laterlist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.common.entity.LaterFolderEntity
import com.example.laterlist.repository.LaterListRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LaterListViewModel: ViewModel() {
    private val laterListRepository by lazy { LaterListRepository() }

    fun createFolder(folderEntity: LaterFolderEntity){
        val createFolder = laterListRepository.createFolder<String>(laterFolderEntity = folderEntity)
    }

    fun createTag(){

    }

    fun createWebsite(){

    }

    fun createImage(){

    }

    fun createVideo(){

    }


    fun getFolderList(){

    }

    fun getTagList(){

    }

    fun saveToFavorite(){

    }

    fun getFavoriteList(){

    }

    fun getTodayList(){

    }

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