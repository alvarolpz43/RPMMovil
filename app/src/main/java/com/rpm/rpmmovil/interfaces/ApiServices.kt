package com.rpm.rpmmovil.interfaces

import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos

import com.rpm.rpmmovil.Routes.apiRoute.PostRoutes

import okhttp3.MultipartBody
import okhttp3.OkHttpClient

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface ApiServices {


    // Otras funciones y anotaciones...

    @Multipart

    @POST("motos")
    suspend fun postRegisterMotoWithImage(
        @Part("moto") moto: DataItemMotos,
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<Any>


    // Método post de rutas
    @POST("rutas")
    suspend fun PostSaveRoutes(
        @Header("Authorization") token: String,
        @Body data: PostRoutes
    ): Response<Any>

}

object ApiClient {
    val web: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl(Constains.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS) // Ajusta el tiempo de espera según tus necesidades
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(ApiServices::class.java)
    }
}
