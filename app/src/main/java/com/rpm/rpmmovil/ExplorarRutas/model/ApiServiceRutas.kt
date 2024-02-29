package com.rpm.rpmmovil.ExplorarRutas.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiServiceRutas {
    @GET
    fun getAllRutas(@Url url:String):Response<DataRutas>
}