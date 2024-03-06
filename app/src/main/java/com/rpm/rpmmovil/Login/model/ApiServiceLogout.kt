package com.rpm.rpmmovil.Login.model

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServiceLogout {
    @POST(value = "logout")
    fun postLogout(@Header(value="Autorization")authHeader:String):
            Call<Void>
}