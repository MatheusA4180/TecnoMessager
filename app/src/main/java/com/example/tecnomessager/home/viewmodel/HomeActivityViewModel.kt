package com.example.tecnomessager.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tecnomessager.intro.repository.IntroRepository

class HomeActivityViewModel(private val repository: IntroRepository) : ViewModel() {

    fun logoutApp() {
        repository.logoutApp()
    }

}