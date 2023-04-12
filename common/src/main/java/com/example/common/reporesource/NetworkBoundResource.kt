package com.example.common.reporesource

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType>() {
    fun asFlow(forceFromCached: Boolean = false) = flow<Resource<ResultType>> {

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
    }.catch { e ->
        emit(Resource.error(e.message ?: "Unknown error"))
    }

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
