package com.example.common.reporesource

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class NetworkBoundResource<ResultType>() {
    fun asSharedFlow(forceFromCached: Boolean = false, coroutineScope: CoroutineScope) = asMutableSharedFlow(forceFromCached, coroutineScope).asSharedFlow()

    fun asMutableSharedFlow(forceFromCached: Boolean = false, coroutineScope: CoroutineScope): MutableSharedFlow<Resource<ResultType>>{
        val mutableSharedFlow = MutableSharedFlow<Resource<ResultType>>()
        coroutineScope.launch {
            // Check if local cache is available
            val cachedData = loadFromCache().first()
            if (shouldFetch(cachedData)) {
                mutableSharedFlow.emit(Resource.loading(cachedData))
                // Fetch data from network
                val apiResponse = fetchFromNetwork()
                apiResponse.collect {response ->
                    // Process response
                    val processedResponse = processResponse(response)
                    // Save data to local cache
                    saveToCache(processedResponse)
                    if (forceFromCached) {
                        // Return data from local cache
                        mutableSharedFlow.emitAll(loadFromCache().map {
                            Resource.success(data = it)
                        })
                    }else {
                        mutableSharedFlow.emit(processedResponse)
                    }
                }
            } else {
                // Step 5: Return data from local cache
                mutableSharedFlow.emitAll(loadFromCache().map {
                    Resource.cached(it)
                })
            }
        }
        return mutableSharedFlow
    }

    fun asFlow(forceFromCached: Boolean = false): Flow<Resource<ResultType>> = flow {
        // Check if local cache is available
        val cachedData = loadFromCache().first()
        if (shouldFetch(cachedData)) {
            emit(Resource.loading(cachedData))
            // Fetch data from network
            val apiResponse = fetchFromNetwork()
            apiResponse.collect {response ->
                // Process response
                val processedResponse = processResponse(response)
                // Save data to local cache
                saveToCache(processedResponse)
                if (forceFromCached) {
                    // Return data from local cache
                    emitAll(loadFromCache().map {
                        Resource.success(data = it)
                    })
                }else {
                    emit(processedResponse)
                }
            }
        } else {
            // Step 5: Return data from local cache
            emitAll(loadFromCache().map {
                Resource.cached(it)
            })
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    protected open fun processResponse(response: Resource<ResultType>) = response

    @WorkerThread
    protected abstract fun saveToCache(item: Resource<ResultType>)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromCache(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun fetchFromNetwork(): Flow<Resource<ResultType>>
}
