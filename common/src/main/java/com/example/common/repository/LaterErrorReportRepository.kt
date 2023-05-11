package com.example.common.repository

import com.example.common.constants.BaseUrl
import com.example.common.constants.FirebaseFieldsConstants
import com.example.common.repository.service.LaterErrorReportService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LaterErrorReportRepository: LaterErrorReportService {
    private val database by lazy { Firebase.database(BaseUrl.FirebaseRealTimeDatabaseUrl) }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private val userId by lazy { user?.uid ?: "" }
    private val laterErrorReportReference by lazy { getErrorReportReference() }

    override fun uploadError(error: String, date: String) {
        laterErrorReportReference.child(date).push().setValue(error)
    }

    private fun getErrorReportReference() =
        database.getReference(FirebaseFieldsConstants.USER_ID).child(userId).child(FirebaseFieldsConstants.ERROR)
}