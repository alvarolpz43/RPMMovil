package com.rpm.rpmmovil.utils

import android.app.Application
import android.content.Context

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

    val storage = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    fun saveToken(token:String){
        storage.edit().putString("token", token)
    }

    fun getToken() : String? {
        return storage.getString("token", null)
    }

}