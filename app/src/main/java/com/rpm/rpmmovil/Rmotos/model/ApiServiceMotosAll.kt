package com.rpm.rpmmovil.Rmotos.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServiceMotosAll {
    @GET("motos")
    fun getAllMotos():Response<DataItemMotos>
}