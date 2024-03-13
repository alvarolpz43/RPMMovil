package com.rpm.rpmmovil.Routes

import com.rpm.rpmmovil.ExplorarRutas.model.DataRutasItemRespose
import com.rpm.rpmmovil.ExplorarRutas.model.DataRutasRespose
import com.rpm.rpmmovil.ExplorarRutas.model.rutas.RutasResponses
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") key: String,
        @Query("start", encoded = true) start: String,
        @Query("end", encoded = true) end: String
    ): Response<RouteResponse>

    @GET("rutas/{id}")
    suspend fun getCordinateRoutes(@Path("id") rutaid:String):RutasResponses
}