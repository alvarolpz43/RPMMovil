package com.rpm.rpmmovil.Routes.apiRoute

import com.rpm.rpmmovil.Model.Route
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface RoutesApiService {


    @GET("rutas")
    fun getRutas(): Call<AllRutes>

    @POST("routes") fun postRoutes(): Call<Route>
}