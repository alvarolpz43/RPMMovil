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
        @Body dataRMotos: DataItemMotos,
        @Header("Authorization") token: String
    ): Call<DataMotosResponse>
}
