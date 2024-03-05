package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceMotos {
    @GET("motos")
    suspend fun getAllMotos(): Response<DataMotosResponse>
}