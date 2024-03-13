package com.rpm.rpmmovil.Model

import android.widget.ImageView
import retrofit2.http.Url

class Constains {

    companion object{
        const val nomDb="rpmdb"
        const val versionDb = 8

        lateinit var ivImage: ImageView

        const val ROUTES= "CREATE TABLE routes(rutaN string, cordenadasInicio string ,cordenadasFinal string, detalle string, imagen string)"

        const val User= "CREATE TABLE User(id integer primary key autoincrement, nombre text , apellido text, email text unique, password text )"

        const val REGISTERMOTOS = "CREATE TABLE rmotos (marca text, modelo int, cilindraje int, placa text)"
        const val GETRMOTOS = "Select * from rmotos"

        const val BASE_URL = "https://rpm-back-end.vercel.app/api/"

        var DISTANCIA_RUTA = 0.0
        var DIRECCION_URL = ""
    }
}