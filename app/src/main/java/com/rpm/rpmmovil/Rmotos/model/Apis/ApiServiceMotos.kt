package com.rpm.rpmmovil.Rmotos.model.Apis

import DataItemMotos
import DataMotosResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiServiceMotos {
    @GET("usermotos")
    suspend fun getAllMotos(
        @Header("Authorization") token: String
    ): Response<DataMotosResponse>

    @POST("motos")
    suspend fun postDataMoto(
        @Header("Authorization") token: String,
        @Body data: DataItemMotos
    ): Response<Any>

    @PUT("motos")
    suspend fun putDataMoto(
        @Header("Authorization") token: String,
        @Body data: DataItemMotos
    ): Response<Any>
}
