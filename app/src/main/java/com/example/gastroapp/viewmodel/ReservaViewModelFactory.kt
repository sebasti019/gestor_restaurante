package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gastroapp.data.repository.ReservaRepository

class ReservaViewModelFactory(
    private val repo: ReservaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}