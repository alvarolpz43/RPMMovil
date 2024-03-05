package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceMotos {
    @GET("usermotos")
    suspend fun getAllMotos(
        @Header("Authorization") token: String
    ): Response<DataMotosResponse>
}
