package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceMotosAll {
    @GET("motos")
    fun getAllMotos():Response<DataItemMotos>
}