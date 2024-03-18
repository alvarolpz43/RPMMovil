

// Cambios en la interfaz de servicio para obtener todas las rutas
package com.rpm.rpmmovil.ExplorarRutas.model

import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceRutas {
    @GET("rutas")
    suspend fun getAllRutas(): Response<DataRutasRespose>
}

