package com.rpm.rpmmovil.interfaces



import DataItemMotos
import com.rpm.rpmmovil.Model.Constains


import okhttp3.MultipartBody

import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST

import retrofit2.http.Part

import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiServices {


    @GET("profile")
    suspend fun getprofileUser(@Header("Authorization") token: String): Response<dataProfileUser>

    @PUT("updateuser/{idUser}")
    suspend fun updateUser(
        @Path("idUser") idUser: String,
        @Body request: updateUser,
        @Header("Authorization") token: String
    ): Response<updateUser>


    @PUT("motos/update/{idMoto}")
    suspend fun updateMoto(
        @Path("idMoto") idMoto: String,
        @Body request: DataItemMotos,
        @Header("Authorization") token: String
    ): Response<DataItemMotos>
    @Multipart
    @POST("motos")
    suspend fun postRegisterMotoWithImage(
        @Part("moto") moto: DataItemMotos,
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<Any>



}


object ApiClient {
    val web: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl(Constains.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }
}
