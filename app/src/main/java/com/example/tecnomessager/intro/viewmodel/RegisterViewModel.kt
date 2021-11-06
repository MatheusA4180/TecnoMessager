package com.example.tecnomessager.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.User
import com.example.tecnomessager.intro.repository.IntroRepository

class RegisterViewModel(private val repository: IntroRepository) : ViewModel() {

    fun onRegistrationUser(user: User, confirmPassword: String): LiveData<Resource<Boolean>> {
        val validResponse = validFields(user, confirmPassword)
        return if (validResponse.value!!.dado) {
            repository.register(user)
        } else {
            validResponse
        }
    }

    private fun validFields(user: User, confirmPassword: String): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            value = when {
                user.email.isBlank() && user.password.isBlank() && confirmPassword.isBlank() -> {
                    Resource(false, "E-mail e senhas são obrigatórios")
                }
                user.email.isBlank() -> {
                    Resource(false, "E-mail é obrigatório")
                }
                user.password.isBlank() -> {
                    Resource(false, "Senha é obrigatória")
                }
                user.password != confirmPassword -> {
                    Resource(false, "As senhas não coincidem")
                }
                else -> {
                    Resource(true)
                }
            }
        }

}
