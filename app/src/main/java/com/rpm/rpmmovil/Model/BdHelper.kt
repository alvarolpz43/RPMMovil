package com.rpm.rpmmovil.Model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BdHelper(context:Context):SQLiteOpenHelper(
    context, Constains.nomDb,null, Constains.versionDb
) {
    override fun onCreate(db: SQLiteDatabase?) {
        //db?.execSQL(Constains.USERS)
        db?.execSQL(Constains.ROUTES)
        db?.execSQL(Constains.User)
        db?.execSQL(Constains.REGISTERMOTOS)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS routes")
        db?.execSQL("DROP TABLE IF EXISTS User")
        db?.execSQL("DROP TABLE IF EXISTS rmotos")




        onCreate(db)
    }

}