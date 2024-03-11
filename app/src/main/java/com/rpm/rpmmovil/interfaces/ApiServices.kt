package com.rpm.rpmmovil.interfaces

import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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



    @POST("motos")
    fun PostRegisterMoto(
        @Body moto: DataItemMotos,
        @Header("Authorization") token: String
    ): Call<DataItemMotos>


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