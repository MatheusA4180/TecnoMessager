package com.example.tecnomessager.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.data.model.Resource
import com.example.tecnomessager.data.model.UserApp
import com.example.tecnomessager.intro.repository.IntroRepository

class OnboardingViewModel(private val repository: IntroRepository) : ViewModel() {

    fun onProfilerEnter(userApp: UserApp): LiveData<Resource<Boolean>> {
        val validResponse = validFields(userApp)
        return if (validResponse.value!!.dado) {
            repository.saveUserDataToFirebase(userApp)
        } else {
            validResponse
        }
    }

    private fun validFields(userApp: UserApp): LiveData<Resource<Boolean>> =
        MutableLiveData<Resource<Boolean>>().apply {
            value = when {
                userApp.nameProfile!!.isBlank() && userApp.messageProfile!!.isBlank() -> {
                    Resource(false, "Nome e recado são obrigatórios")
                }
                userApp.nameProfile!!.isBlank() -> {
                    Resource(false, "Nome de perfil é obrigatório")
                }
                userApp.messageProfile!!.isBlank() -> {
                    Resource(false, "Recado é obrigatório")
                }
                else -> {
                    Resource(true)
                }
            }
        }

}
