package com.rpm.rpmmovil.presupuesto.model

import com.rpm.rpmmovil.presupuesto.Presupuesto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PresupuestoService {
    @POST("presupuesto")
    suspend fun enviarPresupuesto(@Body presupuestoRequest: Presupuesto): Response<Any>
}