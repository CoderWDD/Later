package com.example.laterlist.repository

import com.example.common.constants.BaseUrl
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.LaterFolderEntity
import com.example.common.reporesource.NetworkBoundResource
import com.example.common.reporesource.Resource
import com.example.laterlist.repository.service.LaterListService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LaterListRepository : LaterListService {
    private val database by lazy { Firebase.database(BaseUrl.FirebaseRealTimeDatabaseUrl) }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private val userId by lazy { user?.uid ?: "" }
    private val folderRef by lazy { getFolderReference() }

    override fun <ResultType> createFolder(laterFolderEntity: LaterFolderEntity): Flow<Resource<ResultType>> {
        return object : NetworkBoundResource<ResultType>() {
            override fun saveToCache(item: Resource<ResultType>) {}

            override fun shouldFetch(data: ResultType?) = true

            override fun loadFromCache(): Flow<ResultType> {
                return flow { emit("" as ResultType) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<ResultType>> =
                flow<Resource<ResultType>> {
                    val result = suspendCoroutine<Resource<ResultType>> { continuation ->
                        folderRef.push().setValue(laterFolderEntity)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success" as ResultType)) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun <ResultType> getFavoriteFolderList(): Flow<Resource<ResultType>> {
        TODO("Not yet implemented")
    }

    override fun <ResultType> getRecycleBinFolderList(): Flow<Resource<ResultType>> {
        TODO("Not yet implemented")
    }

    private fun getFolderReference() =
        database.getReference(FirebaseFieldsConstants.FOLDER).child(userId)

    override fun onClear() {
    }
}
