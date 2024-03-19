package com.rpm.rpmmovil.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.rpm.rpmmovil.Login.Login

class AppRPM : Application() {

    companion object{
        lateinit var prefe: Preferences
    }

    override fun onCreate() {
        super.onCreate()
        prefe = Preferences(applicationContext)
    }

}

class Preferences(context: Context){

    val storage = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveToken(token:String){
        storage.edit().putString("token", token).apply()
    }

    fun getToken() : String? {
        return storage.getString("token", "")
    }

    // Método para limpiar todas las preferencias
    fun clearPreferences() {
        storage.edit().clear().apply()


    }



}