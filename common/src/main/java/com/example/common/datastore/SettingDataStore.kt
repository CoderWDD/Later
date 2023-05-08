package com.example.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.common.constants.DataStoreConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SettingDataStore(private val dataStore: DataStore<Preferences>) {
    data class SettingDataParameters(
        val token: String,
        val model: String,
        val temperature: Double,
        val msgNum: Int
    )

    companion object {
        val TOKEN_KEY = stringPreferencesKey(DataStoreConstants.OPENAI_TOKEN_KEY)
        val MODEL_KEY = stringPreferencesKey(DataStoreConstants.OPENAI_MODEL_KEY)
        val TEMPERATURE_KEY = doublePreferencesKey(DataStoreConstants.OPENAI_TEMPERATURE_KEY)
        val MSG_NUM_KEY = intPreferencesKey(DataStoreConstants.OPENAI_MSG_NUM_KEY)
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun saveModel(model: String) {
        dataStore.edit {
            it[MODEL_KEY] = model
        }
    }

    suspend fun saveTemperature(temperature: Double) {
        dataStore.edit {
            it[TEMPERATURE_KEY] = temperature
        }
    }

    suspend fun saveMsgNum(msgNum: Int) {
        dataStore.edit {
            it[MSG_NUM_KEY] = msgNum
        }
    }

    suspend fun saveAll(settingDataParameters: SettingDataParameters) {
        dataStore.edit {
            it[TOKEN_KEY] = settingDataParameters.token
            it[MODEL_KEY] = settingDataParameters.model
            it[TEMPERATURE_KEY] = settingDataParameters.temperature
            it[MSG_NUM_KEY] = settingDataParameters.msgNum
        }
    }

    fun getAll(): kotlinx.coroutines.flow.Flow<SettingDataParameters> = dataStore.data.map {
        SettingDataParameters(
            it[TOKEN_KEY] ?: "",
            it[MODEL_KEY] ?: "",
            it[TEMPERATURE_KEY] ?: 0.0,
            it[MSG_NUM_KEY] ?: 0
        )
    }.flowOn(Dispatchers.IO)

    fun getToken(): kotlinx.coroutines.flow.Flow<String> = dataStore.data.map {
        it[TOKEN_KEY] ?: ""
    }.flowOn(Dispatchers.IO)

    fun getModel(): kotlinx.coroutines.flow.Flow<String> = dataStore.data.map {
        it[MODEL_KEY] ?: ""
    }.flowOn(Dispatchers.IO)

    fun getTemperature(): kotlinx.coroutines.flow.Flow<Double> = dataStore.data.map {
        it[TEMPERATURE_KEY] ?: 0.0
    }.flowOn(Dispatchers.IO)

    fun getMsgNum(): kotlinx.coroutines.flow.Flow<Int> = dataStore.data.map {
        it[MSG_NUM_KEY] ?: 0
    }.flowOn(Dispatchers.IO)
}