package com.example.laterlist.repository

import com.example.common.constants.BaseUrl
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterViewItem
import com.example.common.extents.isToday
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
    private val favoriteFolderRef by lazy { getFavoriteFolderReference() }
    private val recycleFolderRef by lazy { getRecycleFolderReference() }
    private val tagRef by lazy { getTagReference() }

    override fun createFolder(laterFolderEntity: LaterFolderEntity): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("" as String) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.push().setValue(laterFolderEntity)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun getFavoriteFolderList(): Flow<Resource<List<LaterFolderEntity>>> {
        return object : NetworkBoundResource<List<LaterFolderEntity>>() {
            override fun saveToCache(item: Resource<List<LaterFolderEntity>>) {}

            override fun shouldFetch(data: List<LaterFolderEntity>?) = true

            override fun loadFromCache(): Flow<List<LaterFolderEntity>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterFolderEntity>>> =
                flow<Resource<List<LaterFolderEntity>>> {
                    val result = suspendCoroutine<Resource<List<LaterFolderEntity>>> { continuation ->
                        favoriteFolderRef.get()
                            .addOnSuccessListener {
                                val list = mutableListOf<LaterFolderEntity>()
                                it.children.forEach { child ->
                                    child.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        list.add(folder)
                                    }
                                }
                                continuation.resume(Resource.success(data = list))
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun getRecycleBinFolderList(): Flow<Resource<List<LaterFolderEntity>>> {
        return object : NetworkBoundResource<List<LaterFolderEntity>>() {
            override fun saveToCache(item: Resource<List<LaterFolderEntity>>) {}

            override fun shouldFetch(data: List<LaterFolderEntity>?) = true

            override fun loadFromCache(): Flow<List<LaterFolderEntity>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterFolderEntity>>> =
                flow<Resource<List<LaterFolderEntity>>> {
                    val result = suspendCoroutine<Resource<List<LaterFolderEntity>>> { continuation ->
                        recycleFolderRef.get()
                            .addOnSuccessListener {
                                val list = mutableListOf<LaterFolderEntity>()
                                it.children.forEach { child ->
                                    child.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        list.add(folder)
                                    }
                                }
                                continuation.resume(Resource.success(data = list))
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun deleteFavoriteFolder(folderPath: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.child(folderPath).removeValue()
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun deleteRecycleBinFolder(folderPath: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        recycleFolderRef.child(folderPath).removeValue()
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun moveFolderToRecycleBin(folderPath: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.child(folderPath).get()
                            .addOnSuccessListener {dataSnapshot ->
                                val folder = dataSnapshot.getValue(LaterFolderEntity::class.java)
                                if (folder != null) {
                                    recycleFolderRef.child(folderPath).setValue(folder)
                                        .addOnSuccessListener {_ ->
                                            favoriteFolderRef.child(folderPath).removeValue()
                                                .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                                                .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                                                .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                                        }
                                        .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                                        .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                                } else {
                                    continuation.resume(Resource.error(message = "Folder not found"))
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun getTodayLaterViewItemList(): Flow<Resource<List<LaterViewItem>>> {
        return object : NetworkBoundResource<List<LaterViewItem>>() {
            override fun saveToCache(item: Resource<List<LaterViewItem>>) {}

            override fun shouldFetch(data: List<LaterViewItem>?) = true

            override fun loadFromCache(): Flow<List<LaterViewItem>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterViewItem>>> =
                flow<Resource<List<LaterViewItem>>> {
                    val result = suspendCoroutine<Resource<List<LaterViewItem>>> { continuation ->
                        favoriteFolderRef.get()
                            .addOnSuccessListener {folderListSnapshot ->
                                val list = mutableListOf<LaterFolderEntity>()
                                folderListSnapshot.children.forEach { folderSnapshot ->
                                    folderSnapshot.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        list.add(folder)
                                    }
                                }

                                list.forEach {folder ->
                                    getFavoriteLaterViewItemReference(folder.id).get()
                                        .addOnSuccessListener {laterListSnapshot ->
                                            val laterList = mutableListOf<LaterViewItem>()
                                            laterListSnapshot.children
                                                .filter {laterItem ->
                                                    val later = laterItem.getValue(LaterViewItem::class.java)
                                                    later?.createTime?.isToday() == true || later?.updateTime?.isToday() == true
                                                }
                                                .forEach { laterSnapshot ->
                                                    laterSnapshot.getValue(LaterViewItem::class.java)?.let { later ->
                                                    laterList.add(later)
                                                }
                                            }
                                            continuation.resume(Resource.success(data = laterList))
                                        }
                                        .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                                        .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun getFavoriteLaterViewItemList(): Flow<Resource<List<LaterViewItem>>> {
        return object : NetworkBoundResource<List<LaterViewItem>>() {
            override fun saveToCache(item: Resource<List<LaterViewItem>>) {}

            override fun shouldFetch(data: List<LaterViewItem>?) = true

            override fun loadFromCache(): Flow<List<LaterViewItem>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterViewItem>>> =
                flow<Resource<List<LaterViewItem>>> {
                    val result = suspendCoroutine<Resource<List<LaterViewItem>>> { continuation ->
                        favoriteFolderRef.get()
                            .addOnSuccessListener {folderListSnapshot ->
                                val list = mutableListOf<LaterFolderEntity>()
                                folderListSnapshot.children.forEach { folderSnapshot ->
                                    folderSnapshot.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        list.add(folder)
                                    }
                                }

                                list.forEach {folder ->
                                    getFavoriteLaterViewItemReference(folder.id).get()
                                        .addOnSuccessListener {laterListSnapshot ->
                                            val laterList = mutableListOf<LaterViewItem>()
                                            laterListSnapshot.children
                                                .filter {laterItem ->
                                                    val later = laterItem.getValue(LaterViewItem::class.java)
                                                    later?.isStar == true
                                                }
                                                .forEach { laterSnapshot ->
                                                    laterSnapshot.getValue(LaterViewItem::class.java)?.let { later ->
                                                        laterList.add(later)
                                                    }
                                                }
                                            continuation.resume(Resource.success(data = laterList))
                                        }
                                        .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                                        .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun createLaterViewItem(folderPath: String, laterViewItem: LaterViewItem): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        getFavoriteLaterViewItemReference(folderPath).push().setValue(laterViewItem)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun createLaterTag(tag: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        tagRef.push().setValue(tag)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun getTagsList(): Flow<Resource<List<String>>> {
        return object : NetworkBoundResource<List<String>>() {
            override fun saveToCache(item: Resource<List<String>>) {}

            override fun shouldFetch(data: List<String>?) = true

            override fun loadFromCache(): Flow<List<String>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<String>>> =
                flow<Resource<List<String>>> {
                    val result = suspendCoroutine<Resource<List<String>>> { continuation ->
                        tagRef.get()
                            .addOnSuccessListener {tagListSnapshot ->
                                val list = mutableListOf<String>()
                                tagListSnapshot.children.forEach { tagSnapshot ->
                                    tagSnapshot.getValue(String::class.java)?.let { tag ->
                                        list.add(tag)
                                    }
                                }
                                continuation.resume(Resource.success(data = list))
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }

    override fun deleteTag(tag: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        tagRef.orderByValue().equalTo(tag).get()
                            .addOnSuccessListener {tagListSnapshot ->
                                tagListSnapshot.children.forEach { tagSnapshot ->
                                    tagSnapshot.ref.removeValue()
                                        .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                                        .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                                        .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asFlow()
    }


    private fun getFavoriteFolderReference() =
        database.getReference(FirebaseFieldsConstants.USER_ID).child(userId).child(FirebaseFieldsConstants.FAVORITE_FOLDER)

    private fun getRecycleFolderReference() =
        database.getReference(FirebaseFieldsConstants.USER_ID).child(userId).child(FirebaseFieldsConstants.RECYCLE_FOLDER)

    private fun getFavoriteLaterViewItemReference(favoriteFolderPath: String) =
        favoriteFolderRef.child(favoriteFolderPath).child(FirebaseFieldsConstants.LATER_VIEW_ITEM)

    private fun getTagReference() =
        database.getReference(FirebaseFieldsConstants.USER_ID).child(userId).child(FirebaseFieldsConstants.TAG)

    override fun onClear() {
    }
}
