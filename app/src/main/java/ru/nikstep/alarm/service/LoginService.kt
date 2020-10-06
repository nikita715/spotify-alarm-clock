package ru.nikstep.alarm.service

import java.util.Date

class LoginService {

    private val expirationTimeMillis = 3600000 - 300000

    private var accessToken: String? = null
    private var expiresAt: Date? = null

    fun hasAccessToken() = accessToken != null && Date().before(expiresAt)

    fun saveAccessToken(accessToken: String) {
        this.accessToken = "Bearer $accessToken"
        this.expiresAt = Date(System.currentTimeMillis() + expirationTimeMillis)
    }

    fun getAccessToken() = accessToken

}