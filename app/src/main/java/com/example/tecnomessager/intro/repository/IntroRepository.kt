package com.example.tecnomessager.intro.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.User
import com.google.firebase.auth.*

class IntroRepository(private val firebaseAuth: FirebaseAuth) {

    fun auth(user: User): LiveData<Resource<Boolean>> = MutableLiveData<Resource<Boolean>>().apply{
        try {
            firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        value = Resource(true)
                    } else {
                        val messageError: String = returnErrorForAuth(task.exception)
                        value = Resource(false, messageError)
                    }
                }
        } catch (e: IllegalArgumentException) {
            value = Resource(false, "E-mail ou senha não pode ser vazio")
        }
    }

    private fun returnErrorForAuth(exception: Exception?): String = when (exception) {
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
        else -> "Erro desconhecido"
    }

    fun register(user: User): LiveData<Resource<Boolean>> = MutableLiveData<Resource<Boolean>>().apply{
        try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    value = Resource(true)
                }
                .addOnFailureListener { exception ->
                    val messageError: String = returnErrorForRegister(exception)
                    value = Resource(false, messageError)
                }
        } catch (e: IllegalArgumentException) {
            value = Resource(false, "E-mail ou senha não ser vazio")
        }
    }

    private fun returnErrorForRegister(exception: Exception): String = when (exception) {
        is FirebaseAuthWeakPasswordException -> "Senha precisa de pelo menos 6 dígitos"
        is FirebaseAuthInvalidCredentialsException -> "E-mail inválido"
        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"
        else -> "Erro desconhecido"
    }

}