package com.example.tecnomessager.data.local

import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(private val preferences: SharedPreferences) {

    fun saveUidUser(uidUser:String){
        preferences.edit {
            putString(UID_USER,uidUser)
        }
    }

    fun getSavedUidUser(): String = preferences.getString(UID_USER,"")!!

    companion object{
        private const val UID_USER = "uid_user"
    }

}