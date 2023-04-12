package com.example.laterlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.LaterFolderEntity
import com.example.common.log.LaterLog
import com.example.common.reporesource.Resource
import com.example.common.unify.ResBody
import com.example.laterlist.repository.LaterListRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LaterListViewModel: ViewModel() {
    private val laterListRepository: LaterListRepository by lazy { LaterListRepository() }

    fun createFolder(folderEntity: LaterFolderEntity){
        viewModelScope.launch{
            laterListRepository.createFolder<String>(folderEntity).collect{
                when (it) {
                    is Resource.Success -> {
                        println("createFolder success")
                    }
                    is Resource.Error -> {
                        println("createFolder error")
                    }
                    is Resource.Loading -> {
                        println("createFolder loading")
                    }
                    is Resource.Cached -> {

                    }
                    else -> {}
                }
            }
        }
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