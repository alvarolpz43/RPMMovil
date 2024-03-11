package com.rpm.rpmmovil.interfaces



import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
<<<<<<< HEAD
import okhttp3.MultipartBody
=======
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
>>>>>>> c70c9c6e08b751dce6d88da68f0f87dd1424b610
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
<<<<<<< HEAD
import retrofit2.http.Part
=======
import retrofit2.http.PUT
import retrofit2.http.Path
>>>>>>> c70c9c6e08b751dce6d88da68f0f87dd1424b610

interface ApiServices {


    @GET("profile")
    suspend fun getprofileUser(@Header("Authorization") token: String): Response<dataProfileUser>

    @PUT("updateuser/{idUser}")
    suspend fun updateUser(
        @Path("idUser") idUser: String,
        @Body request: updateUser,
        @Header("Authorization") token: String
    ): Response<updateUser>



    @Multipart
    @POST("motos")
    suspend fun postRegisterMotoWithImage(
        @Part("moto") moto: DataItemMotos,
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<Any>



}

<<<<<<< HEAD
=======

>>>>>>> c70c9c6e08b751dce6d88da68f0f87dd1424b610
object ApiClient {
    val web: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl(Constains.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }
}
