package com.example.tecnomessager.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.User
import com.example.tecnomessager.intro.repository.IntroRepository

class LoginViewModel(private val repository: IntroRepository) : ViewModel() {

    fun auth(user: User): LiveData<Resource<Boolean>> {
        val valid = validFields(user)
        return if (valid.value!!.dado) {
            repository.auth(user)
        } else {
            valid
        }
    }

    private fun validFields(user: User): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            value = when {
                user.email.isBlank() && user.password.isBlank() -> {
                    Resource(false, "E-mail e senha são obrigatórios")
                }
                user.email.isBlank() -> {
                    Resource(false, "E-mail é obrigatório")
                }
                user.password.isBlank() -> {
                    Resource(false, "Senha é obrigatória")
                }
                else -> {
                    Resource(true)
                }

            }
        }

}