package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServiceMotos {
    @GET("usermotos")
    suspend fun getAllMotos(
        @Header("Authorization") token: String
    ): Response<DataMotosResponse>

    @POST("usermotos")
    suspend fun postDataMoto(
        @Header("Authorization") token: String,
        @Body data: DataItemMotos
    ): Response<Any>
}
