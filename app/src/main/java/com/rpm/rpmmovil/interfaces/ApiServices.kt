package com.rpm.rpmmovil.interfaces


import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Routes.apiRoute.UserRoutes
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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


    @GET("userrutas")
    suspend fun getUserRutas(@Header("Authorization") token: String): Response<UserRoutes>


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
