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

    fun saveEmailUser(emailUser:String){
        preferences.edit {
            putString(EMAIL_USER,emailUser)
        }
    }

    fun getSavedEmailUser(): String = preferences.getString(EMAIL_USER,"")!!

    companion object{
        private const val UID_USER = "uid_user"
        private const val EMAIL_USER = "email_user"
    }

}