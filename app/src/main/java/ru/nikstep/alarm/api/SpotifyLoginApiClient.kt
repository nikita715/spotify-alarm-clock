package ru.nikstep.alarm.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyLoginApiClient {

    @GET("/authorize")
    fun getMe(
        @Query("client_id") clientId: String,
        @Query("response_type") responseType: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("scope") scope: String
    ): Call<ResponseBody>

}