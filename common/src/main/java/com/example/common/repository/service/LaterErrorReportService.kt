package com.example.common.repository.service

interface LaterErrorReportService {
    fun uploadError(error: String, date: String)
}