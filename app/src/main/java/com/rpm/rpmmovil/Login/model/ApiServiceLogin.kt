package com.rpm.rpmmovil.Login.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceLogin {
    @POST("login")
    fun getLogin(@Body users:DtaUser):Call<DtaUser>
}