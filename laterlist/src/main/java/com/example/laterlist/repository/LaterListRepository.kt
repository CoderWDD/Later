package com.example.laterlist.repository

import com.example.common.constants.BaseUrl
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterTagEntity
import com.example.common.entity.LaterViewItem
import com.example.common.entity.TodoItem
import com.example.common.extents.isToday
import com.example.common.log.LaterLog
import com.example.common.reporesource.NetworkBoundResource
import com.example.common.reporesource.Resource
import com.example.laterlist.repository.service.LaterListService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LaterListRepository(private val viewModelScope: CoroutineScope) : LaterListService {
    private val database by lazy { Firebase.database(BaseUrl.FirebaseRealTimeDatabaseUrl) }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private val userId by lazy { user?.uid ?: "" }
    private val favoriteFolderRef by lazy { getFavoriteFolderReference() }
    private val recycleFolderRef by lazy { getRecycleFolderReference() }
    private val tagRef by lazy { getTagReference() }

    override fun createFolder(laterFolderEntity: LaterFolderEntity): SharedFlow<Resource<String>> {
        return object : NetworkBoundResource<String> (){
            override fun saveToCache(item: Resource<String>) { }

            override fun shouldFetch(data: String?): Boolean = true

            override fun loadFromCache(): Flow<String> {
                return flowOf("")
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.child(laterFolderEntity.title).push().setValue(laterFolderEntity)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope)
    }

    override fun getFavoriteFolderList(): MutableSharedFlow<Resource<List<LaterFolderEntity>>> {
        val favoriteFolderList = mutableListOf<LaterFolderEntity>()

        val mutableSharedFlow = object : NetworkBoundResource<List<LaterFolderEntity>>() {
            override fun saveToCache(item: Resource<List<LaterFolderEntity>>) {}

            override fun shouldFetch(data: List<LaterFolderEntity>?) = true

            override fun loadFromCache(): Flow<List<LaterFolderEntity>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterFolderEntity>>> =
                flow<Resource<List<LaterFolderEntity>>> {
                    emit(Resource.loading(data = emptyList()))
                }
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val folderEntity = snapshot.getValue(LaterFolderEntity::class.java)!!
                folderEntity.key = snapshot.key!!
                folderEntity.cnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                favoriteFolderList.add(folderEntity)
                viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = favoriteFolderList)) }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val folder = snapshot.getValue(LaterFolderEntity::class.java)
                val itemCnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                folder?.let {
                    it.cnt = itemCnt
                    val index = favoriteFolderList.indexOfFirst { folder -> folder.id == it.id }
                    if (index != -1) {
                        favoriteFolderList[index] = it
                        viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = favoriteFolderList)) }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val folder = snapshot.getValue(LaterFolderEntity::class.java)
                val itemCnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                folder?.let {
                    it.cnt = itemCnt
                    val index = favoriteFolderList.indexOfFirst { folder -> folder.id == it.id }
                    if (index != -1) {
                        favoriteFolderList.removeAt(index)
                        viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = favoriteFolderList)) }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        // add child event listener
        favoriteFolderRef.addChildEventListener(childEventListener)
        return mutableSharedFlow
    }

    override fun getRecycleBinFolderList(): MutableSharedFlow<Resource<List<LaterFolderEntity>>> {
        val recycleFolderList = mutableListOf<LaterFolderEntity>()
        val mutableSharedFlow =  object : NetworkBoundResource<List<LaterFolderEntity>>() {
            override fun saveToCache(item: Resource<List<LaterFolderEntity>>) {}

            override fun shouldFetch(data: List<LaterFolderEntity>?) = true

            override fun loadFromCache(): Flow<List<LaterFolderEntity>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterFolderEntity>>> =
                flow<Resource<List<LaterFolderEntity>>> {
                    emit(Resource.loading(data = emptyList()))
                }
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val folderEntity = snapshot.getValue(LaterFolderEntity::class.java)!!
                val itemCnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                folderEntity.cnt = itemCnt
                folderEntity.key = snapshot.key!!
                recycleFolderList.add(folderEntity)
                viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = recycleFolderList)) }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val folder = snapshot.getValue(LaterFolderEntity::class.java)
                val itemCnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                folder?.let {
                    it.cnt = itemCnt
                    val index = recycleFolderList.indexOfFirst { folder -> folder.id == it.id }
                    if (index != -1) {
                        recycleFolderList[index] = it
                        viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = recycleFolderList)) }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val folder = snapshot.getValue(LaterFolderEntity::class.java)
                val itemCnt = snapshot.child(FirebaseFieldsConstants.LATER_VIEW_ITEM).children.toList().size
                folder?.let {
                    it.cnt = itemCnt
                    val index = recycleFolderList.indexOfFirst { folder -> folder.id == it.id }
                    if (index != -1) {
                        recycleFolderList.removeAt(index)
                        viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = recycleFolderList)) }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        // add child event listener
        recycleFolderRef.addChildEventListener(childEventListener)
        return mutableSharedFlow
    }

    override fun deleteFavoriteFolder(folderKey: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.child(folderKey).get().addOnSuccessListener {
                            recycleFolderRef.push().setValue(it.getValue(LaterFolderEntity::class.java))
                            favoriteFolderRef.child(folderKey).removeValue()
                            continuation.resume(Resource.success(data = "Success"))
                        }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun deleteRecycleBinFolder(folderKey: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        recycleFolderRef.child(folderKey).removeValue()
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun moveFolderToRecycleBin(folderKey: String): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        favoriteFolderRef.child(folderKey).get()
                            .addOnSuccessListener {dataSnapshot ->
                                val folder = dataSnapshot.getValue(LaterFolderEntity::class.java)
                                if (folder != null) {
                                    recycleFolderRef.child(folderKey).setValue(folder)
                                        .addOnSuccessListener {_ ->
                                            favoriteFolderRef.child(folderKey).removeValue()
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
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun getTodayLaterViewItemList(): MutableSharedFlow<Resource<List<LaterViewItem>>> {
        val laterViewItemList = mutableListOf<LaterViewItem>()
        val mutableSharedFlow =  object : NetworkBoundResource<List<LaterViewItem>>() {
            override fun saveToCache(item: Resource<List<LaterViewItem>>) {}

            override fun shouldFetch(data: List<LaterViewItem>?) = true

            override fun loadFromCache(): Flow<List<LaterViewItem>> {
                return flowOf(emptyList())
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterViewItem>>> =
                flow<Resource<List<LaterViewItem>>> {
                    val result = suspendCoroutine<Resource<List<LaterViewItem>>> { continuation ->
                        favoriteFolderRef.get()
                            .addOnSuccessListener {folderListSnapshot ->
                                val list = mutableListOf<LaterFolderEntity>()
                                folderListSnapshot.children.forEach { folderSnapshot ->
                                    folderSnapshot.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        folder.key = folderSnapshot.key.toString()
                                        list.add(folder)
                                    }
                                }
                                var count = 0
                                list.forEach {folder ->
                                    getFavoriteLaterViewItemReference(folder.key).get()
                                        .addOnSuccessListener {laterListSnapshot ->
                                            laterListSnapshot.children
                                                .filter { laterItem ->
                                                    val later = laterItem.getValue(LaterViewItem::class.java)
                                                    later?.key = laterItem.key.toString()
                                                    later?.createTime?.isToday() == true || later?.updateTime?.isToday() == true
                                                }
                                                .forEach { laterSnapshot ->
                                                    laterSnapshot.getValue(LaterViewItem::class.java)?.let { later ->
                                                        later.key = laterSnapshot.key.toString()
                                                        laterViewItemList.add(later)
                                                    }
                                                }
                                            count++
                                            if (count == list.size) {
                                                continuation.resume(Resource.success(data = laterViewItemList))
                                            }
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
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val childEventListener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    if (it.createTime.isToday() || it.updateTime.isToday()) {
                        laterViewItemList.add(it)
                        mutableSharedFlow.tryEmit(Resource.success(data = laterViewItemList))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    if (it.createTime.isToday() || it.updateTime.isToday()) {
                        laterViewItemList.indexOfFirst { laterViewItem -> laterViewItem.id == it.id }.let { index ->
                            if (index != -1) {
                                laterViewItemList[index] = it
                                mutableSharedFlow.tryEmit(Resource.success(data = laterViewItemList))
                            }
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    if (it.createTime.isToday() || it.updateTime.isToday()) {
                        laterViewItemList.indexOfFirst { laterViewItem -> laterViewItem.id == it.id }.let { index ->
                            if (index != -1) {
                                laterViewItemList.removeAt(index)
                                mutableSharedFlow.tryEmit(Resource.success(data = laterViewItemList))
                            }
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        // add child event listener
        favoriteFolderRef.get().addOnSuccessListener {
            it.children.forEach { folderSnapshot ->
                folderSnapshot.children.forEach { folder ->
                    folder.ref.addChildEventListener(childEventListener)
                }
            }
        }
        return mutableSharedFlow
    }

    override fun getFavoriteLaterViewItemList(): MutableSharedFlow<Resource<List<LaterViewItem>>> {
        val favoriteLaterViewItemList = mutableListOf<LaterViewItem>()
        val mutableSharedFlow =  object : NetworkBoundResource<List<LaterViewItem>>() {
            override fun saveToCache(item: Resource<List<LaterViewItem>>) {}

            override fun shouldFetch(data: List<LaterViewItem>?) = true

            override fun loadFromCache(): Flow<List<LaterViewItem>> {
                return flowOf(emptyList())
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterViewItem>>> {
                val list = mutableListOf<LaterFolderEntity>()
                val res = flow<Resource<List<LaterViewItem>>> {
                    val result = suspendCoroutine<Resource<List<LaterViewItem>>> { continuation ->
                        favoriteFolderRef.get()
                            .addOnSuccessListener {folderListSnapshot ->
                                folderListSnapshot.children.forEach { folderSnapshot ->
                                    folderSnapshot.getValue(LaterFolderEntity::class.java)?.let { folder ->
                                        folder.key = folderSnapshot.key.toString()
                                        list.add(folder)
                                    }
                                }
                                var count = 0
                                list.forEachIndexed { _, laterFolderEntity ->
                                    getFavoriteLaterViewItemReference(laterFolderEntity.key).get()
                                        .addOnSuccessListener {laterListSnapshot ->
                                            laterListSnapshot.children.forEach { laterSnapshot ->
                                                laterSnapshot.getValue(LaterViewItem::class.java)?.let { later ->
                                                    later.key = laterSnapshot.key.toString()
                                                    favoriteLaterViewItemList.add(later)
                                                }
                                            }
                                            count++
                                            if (count == list.size) {
                                                continuation.resume(Resource.success(data = favoriteLaterViewItemList))
                                            }
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

                return res
            }

        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val childEventListener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    favoriteLaterViewItemList.add(it)
                    mutableSharedFlow.tryEmit(Resource.success(data = favoriteLaterViewItemList))
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    favoriteLaterViewItemList.indexOfFirst { laterViewItem -> laterViewItem.id == it.id }.let { index ->
                        if (index != -1) {
                            favoriteLaterViewItemList[index] = it
                            mutableSharedFlow.tryEmit(Resource.success(data = favoriteLaterViewItemList))
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(LaterViewItem::class.java)?.let {
                    favoriteLaterViewItemList.indexOfFirst { laterViewItem -> laterViewItem.id == it.id }.let { index ->
                        if (index != -1) {
                            favoriteLaterViewItemList.removeAt(index)
                            mutableSharedFlow.tryEmit(Resource.success(data = favoriteLaterViewItemList))
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        // add child event listener
        favoriteFolderRef.get().addOnSuccessListener {
            it.children.forEach { folderSnapshot ->
                folderSnapshot.children.forEach {folder ->
                    folder.ref.addChildEventListener(childEventListener)
                }
            }
        }

        return mutableSharedFlow
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
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun createLaterTag(tag: LaterTagEntity): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flowOf("")
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        tagRef.child(tag.name).push().setValue(tag)
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun getTagsList(): MutableSharedFlow<Resource<List<LaterTagEntity>>> {
        val tagsList = mutableListOf<LaterTagEntity>()
        val mutableSharedFlow = object : NetworkBoundResource<List<LaterTagEntity>>() {
            override fun saveToCache(item: Resource<List<LaterTagEntity>>) {}

            override fun shouldFetch(data: List<LaterTagEntity>?) = true

            override fun loadFromCache(): Flow<List<LaterTagEntity>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterTagEntity>>> =
                flow<Resource<List<LaterTagEntity>>> {
                    emit(Resource.loading(data = emptyList()))
                }
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val childEventListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterTagEntity::class.java)?.let {
                    it.key = snapshot.key!!
                    tagsList.add(it)
                    viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = tagsList)) }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(LaterTagEntity::class.java)?.let {
                    tagsList.indexOfFirst { tag -> tag.id == it.id }.let { index ->
                        if (index != -1) {
                            it.key = snapshot.key.toString()
                            tagsList[index] = it
                            viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = tagsList)) }
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.getValue(LaterTagEntity::class.java)?.let {
                    tagsList.indexOfFirst { tag -> tag.id == it.id }.let { index ->
                        if (index != -1) {
                            tagsList.removeAt(index)
                            viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = tagsList)) }
                        }
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        tagRef.addChildEventListener(childEventListener)
        return mutableSharedFlow
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
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun getLaterViewItemByFolder(folderPath: String): MutableSharedFlow<Resource<List<LaterViewItem>>> {
        val mutableSharedFlow = object : NetworkBoundResource<List<LaterViewItem>>() {
            override fun saveToCache(item: Resource<List<LaterViewItem>>) {}

            override fun shouldFetch(data: List<LaterViewItem>?) = true

            override fun loadFromCache(): Flow<List<LaterViewItem>> {
                return flow { emit(emptyList()) }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<LaterViewItem>>> =
                flow<Resource<List<LaterViewItem>>> {
                    emit(Resource.loading(data = emptyList()))
                }
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<LaterViewItem>()
                snapshot.children.forEach { dataSnapshot ->
                    dataSnapshot.getValue(LaterViewItem::class.java)?.let { viewItem ->
                        viewItem.key = dataSnapshot.key ?: ""
                        itemList.add(viewItem)
                    }
                }
                viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = itemList)) }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch { mutableSharedFlow.emit(Resource.error(error.message)) }
            }
        }
        getFavoriteLaterViewItemReference(folderPath).addValueEventListener(valueEventListener)
        return mutableSharedFlow
    }

    override fun updateLaterViewItem(favoriteFolderPath: String, laterViewItem: LaterViewItem): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        getFavoriteLaterViewItemReference(favoriteFolderPath).updateChildren(mapOf(laterViewItem.key to laterViewItem))
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
    }

    override fun deleteLaterViewItem(
        favoriteFolderPath: String,
        laterViewItem: LaterViewItem
    ): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) {}

            override fun shouldFetch(data: String?) = true

            override fun loadFromCache(): Flow<String> {
                return flow { emit("") }
            }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> =
                flow<Resource<String>> {
                    val result = suspendCoroutine<Resource<String>> { continuation ->
                        getFavoriteLaterViewItemReference(favoriteFolderPath).child(laterViewItem.key).removeValue()
                            .addOnSuccessListener { continuation.resume(Resource.success(data = "Success")) }
                            .addOnCanceledListener { continuation.resume(Resource.error(message = "Canceled")) }
                            .addOnFailureListener { continuation.resume(Resource.error(message = it.message ?: "Unknown error")) }
                    }
                    emit(result)
                }
        }.asSharedFlow(coroutineScope = viewModelScope).catch { emit(Resource.error(message = it.message ?: "Unknown error")) }
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
