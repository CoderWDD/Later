package com.example.common.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.example.common.datastore.SettingDataStore

class OpenAISettingViewModel: ViewModel() {
    private lateinit var settingDataStore: SettingDataStore

    fun setDataStore(dataStore: DataStore<Preferences>){
        settingDataStore = SettingDataStore(dataStore)
    }

    fun getOpenAIModel() = settingDataStore.getModel()

    suspend fun setOpenAIModel(model: SettingDataStore.OpenAIModel) = settingDataStore.saveModel(model)

    fun getOpenAIToken() = settingDataStore.getToken()

    suspend fun setOpenAIToken(token: String) = settingDataStore.saveToken(token)

    fun getOpenAITemperature() = settingDataStore.getTemperature()

    suspend fun setOpenAITemperature(temperature: Double) = settingDataStore.saveTemperature(temperature)

    fun getOpenAIMsgNum() = settingDataStore.getMsgNum()

    suspend fun setOpenAIMsgNum(msgNum: Int) = settingDataStore.saveMsgNum(msgNum)

    fun getOpenAIAll() = settingDataStore.getAll()

    suspend fun setOpenAIAll(model: SettingDataStore.OpenAIModel, token: String, temperature: Double, msgNum: Int) = settingDataStore.saveAll(
        SettingDataStore.SettingDataParameters(token = token, model = model, temperature = temperature, msgNum = msgNum))
}