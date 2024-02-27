package com.rpm.rpmmovil.ExplorarRutas.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiServiceRutas {
    @GET("rutas")
    fun getAllRutas():Call<DataAllRutas>
}