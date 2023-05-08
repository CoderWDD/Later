package com.example.calendar.repository

import com.example.calendar.repository.service.TodoService
import com.example.common.constants.BaseUrl
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.entity.TodoItem
import com.example.common.reporesource.NetworkBoundResource
import com.example.common.reporesource.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

class TodoItemRepository(private val viewModelScope: CoroutineScope): TodoService {
    private val database by lazy { Firebase.database(BaseUrl.FirebaseRealTimeDatabaseUrl) }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private val userId by lazy { user?.uid ?: "" }
    private val todoItemRef by lazy { getTodoRef() }

    override fun getTodoListByDate(date: String): MutableSharedFlow<Resource<List<TodoItem>>> {
        val mutableSharedFlow = object : NetworkBoundResource<List<TodoItem>>() {
            override fun saveToCache(item: Resource<List<TodoItem>>) { }

            override fun shouldFetch(data: List<TodoItem>?): Boolean {
                return true
            }

            override fun loadFromCache(): Flow<List<TodoItem>> = flow { emit(emptyList()) }

            override suspend fun fetchFromNetwork(): Flow<Resource<List<TodoItem>>> {
                return flow {
                    emit(Resource.loading())
                }
            }
        }.asMutableSharedFlow(coroutineScope = viewModelScope)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todoList = mutableListOf<TodoItem>()
                snapshot.children.forEach { dataSnapshot ->
                    dataSnapshot.getValue(TodoItem::class.java)?.let { todoItem ->
                        todoItem.key = dataSnapshot.key ?: ""
                        todoList.add(todoItem)
                    }
                }
                viewModelScope.launch { mutableSharedFlow.emit(Resource.success(data = todoList)) }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch { mutableSharedFlow.emit(Resource.error(error.message)) }
            }
        }



        todoItemRef.child(date).addValueEventListener(valueEventListener)

        return mutableSharedFlow
    }

    override fun createTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) { }

            override fun shouldFetch(data: String?): Boolean {
                return true
            }

            override fun loadFromCache(): Flow<String> = flow { emit("") }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> {
                return flow {
                    emit(Resource.loading())
                    val result = suspendCoroutine<Resource<String>> {
                        todoItemRef.child(date).push().setValue(todoItem).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                it.resumeWith(Result.success(Resource.success(data = "success")))
                            } else {
                                it.resumeWith(Result.success(Resource.error(task.exception?.message)))
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.asSharedFlow(coroutineScope = viewModelScope)
    }

    override fun updateTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) { }

            override fun shouldFetch(data: String?): Boolean {
                return true
            }

            override fun loadFromCache(): Flow<String> = flow { emit("") }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> {
                return flow {
                    emit(Resource.loading())
                    val result = suspendCoroutine<Resource<String>> {
                        todoItemRef.child(date).child(todoItem.key).setValue(todoItem).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                it.resumeWith(Result.success(Resource.success(data = "success")))
                            } else {
                                it.resumeWith(Result.success(Resource.error(task.exception?.message)))
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.asSharedFlow(coroutineScope = viewModelScope)
    }

    override fun deleteTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>> {
        return object : NetworkBoundResource<String>() {
            override fun saveToCache(item: Resource<String>) { }

            override fun shouldFetch(data: String?): Boolean {
                return true
            }

            override fun loadFromCache(): Flow<String> = flow { emit("") }

            override suspend fun fetchFromNetwork(): Flow<Resource<String>> {
                return flow {
                    emit(Resource.loading())
                    val result = suspendCoroutine<Resource<String>> {
                        todoItemRef.child(date).child(todoItem.key).removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                it.resumeWith(Result.success(Resource.success(data = "success")))
                            } else {
                                it.resumeWith(Result.success(Resource.error(task.exception?.message)))
                            }
                        }
                    }
                    emit(result)
                }
            }
        }.asSharedFlow(coroutineScope = viewModelScope)
    }

    private fun getTodoRef() = database.getReference(FirebaseFieldsConstants.USER_ID).child(userId).child(FirebaseFieldsConstants.TODO)

}