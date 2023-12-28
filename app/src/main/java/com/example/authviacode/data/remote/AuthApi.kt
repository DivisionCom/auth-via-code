package com.example.authviacode.data.remote

import com.example.authviacode.data.remote.responses.Code
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthApi {

    @GET("getCode")
    suspend fun getAuthCode(
        @Query("login") login: String
    ): Response<Code>
}