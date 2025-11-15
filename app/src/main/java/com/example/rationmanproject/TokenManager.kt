package com.example.rationmanproject

import android.content.Context
import android.content.SharedPreferences



object tokenmanager{

    private const val file = "auth_prefs"
    private const val tokenkey = "TOKEN_KEY"

    fun getprefs(context: Context) : SharedPreferences{

        return context.getSharedPreferences(file , Context.MODE_PRIVATE)
    }


    fun savetoken(context : Context , token: String){
        return getprefs(context).edit().putString(tokenkey , token).apply()

    }

    fun gettoken(context: Context) : String? {

        return getprefs(context).getString(tokenkey , null)


    }

    fun cleartoken(context: Context){
        return getprefs(context).edit().remove(tokenkey).apply()
    }


}