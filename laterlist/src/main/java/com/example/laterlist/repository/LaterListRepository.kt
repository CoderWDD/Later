package com.example.laterlist.repository

import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.LaterFolderEntity
import com.example.common.unify.ResBody
import com.example.laterlist.repository.service.LaterListService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LaterListRepository: LaterListService {
    private val database by lazy { Firebase.database }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private val userId by lazy { user?.uid ?: "" }
    private val folderRef by lazy { getFolderReference() }

    override fun <T> createFolder(laterFolderEntity: LaterFolderEntity): ResBody<T> {
        var newFolderRef: ResBody<T> = ResBody.Loading
        folderRef.push().setValue(laterFolderEntity)
            .addOnSuccessListener { newFolderRef = ResBody.Success}
            .addOnFailureListener { newFolderRef = ResBody.Error(0, it.message ?: "create folder failed", other = "" as T) }
            .addOnCanceledListener { newFolderRef = ResBody.Cancel }
        return newFolderRef
    }

    private fun getFolderReference() = database.getReference(FirebaseFieldsConstants.FOLDER).child(userId)


    override fun onClear() {
    }
}
