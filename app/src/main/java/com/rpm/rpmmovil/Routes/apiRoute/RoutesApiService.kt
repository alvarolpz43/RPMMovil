package com.rpm.rpmmovil.Routes.apiRoute

import retrofit2.Call
import com.rpm.rpmmovil.Model.Route
import retrofit2.http.POST

interface RoutesApiService {

    @POST("routes")
    fun postRoutes (): Call<Route>
}