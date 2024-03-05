package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegisterMoto {
    @POST("motos")
    fun PostRegisterMoto(
        @Body moto: DataItemMotos,
        @Header("Authorization") token: String
    ): Call<DataItemMotos>
}
