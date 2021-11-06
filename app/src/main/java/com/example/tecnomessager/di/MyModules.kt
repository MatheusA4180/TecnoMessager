package com.example.tecnomessager.di

import com.example.tecnomessager.intro.repository.IntroRepository
import com.example.tecnomessager.intro.viewmodel.LoginViewModel
import com.example.tecnomessager.intro.viewmodel.OnboardingViewModel
import com.example.tecnomessager.intro.viewmodel.RegisterViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val MyModules = module {

    viewModel {
        LoginViewModel(repository = get())
    }

    viewModel {
        RegisterViewModel(repository = get())
    }

    viewModel {
        OnboardingViewModel(repository = get())
    }

    factory {
        IntroRepository(firebaseAuth = get())
    }

    single {
        Firebase.auth
    }

    single {
        Firebase.firestore
    }

}