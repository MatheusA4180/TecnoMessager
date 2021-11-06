package com.example.tecnomessager.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.intro.repository.IntroRepository

class SplashViewModel(private val repository: IntroRepository) : ViewModel() {

    fun isLoggedIn(): LiveData<Boolean> {
        return repository.isLoggedIn()
    }

}
