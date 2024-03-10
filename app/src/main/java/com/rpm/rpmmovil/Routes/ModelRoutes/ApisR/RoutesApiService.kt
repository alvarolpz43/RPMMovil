package com.rpm.rpmmovil.Routes.ModelRoutes.ApisR


import com.rpm.rpmmovil.Routes.apiRoute.Route
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RoutesApiService {

    @POST("rutas")
    fun postRoutes (@Body route: Route): Call<Route>
}