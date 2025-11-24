package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gastroapp.data.repository.ReservaRepository

class LoginRegisterViewModelFactory(private val repo: ReservaRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginRegisterViewModel(repo) as T
    }
}