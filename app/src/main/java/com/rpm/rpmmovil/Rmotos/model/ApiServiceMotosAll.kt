package com.rpm.rpmmovil.Rmotos.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceMotosAll {
    @GET("motos")
    fun getAllMotos():Call<AllMotosDta>
}