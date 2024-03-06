package com.rpm.rpmmovil.Routes.apiRoute

import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

public interface RoutesApiService {



    @GET("userrutas")
    suspend fun getAllUserroutes(
        @Header("Authorization") token: String
    ): Response<AllRutes>



    @POST("routes") fun postRoutes(): Call<Route>
}