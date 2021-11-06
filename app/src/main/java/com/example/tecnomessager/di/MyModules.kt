package com.example.tecnomessager.di

import android.content.Context
import com.example.tecnomessager.R
import com.example.tecnomessager.intro.repository.IntroRepository
import com.example.tecnomessager.intro.viewmodel.LoginViewModel
import com.example.tecnomessager.intro.viewmodel.OnboardingViewModel
import com.example.tecnomessager.intro.viewmodel.RegisterViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
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
        IntroRepository(firebaseAuth = get(), firebaseStorage = get(), sessionManager = get())
    }

    single {
        Firebase.auth
    }

    single {
        Firebase.storage
    }

    single {
        Firebase.firestore
    }

    single {
        androidContext().getSharedPreferences(
            androidContext().getString(R.string.preference_key),
            Context.MODE_PRIVATE
        )
    }

}
