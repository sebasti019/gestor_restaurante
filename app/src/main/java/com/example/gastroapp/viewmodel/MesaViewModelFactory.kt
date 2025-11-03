package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gastroapp.data.local.MesaDao

class MesaViewModelFactory(private val dao: MesaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MesaViewModel::class.java)) {
            return MesaViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}