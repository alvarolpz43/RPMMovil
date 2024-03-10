package com.rpm.rpmmovil.SelectMoto.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiSelectMotos {
    @GET("motos/{id}")
    fun getMotoById(@Path("id") id: String): Call<DtaSelectMoto>
}