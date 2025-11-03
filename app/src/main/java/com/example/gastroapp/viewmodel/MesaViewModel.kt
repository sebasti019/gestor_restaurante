package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastroapp.data.local.MesaDao
import com.example.gastroapp.model.Mesa
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MesaViewModel(private val mesaDao: MesaDao) : ViewModel() {

    val mesas = mesaDao.getAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregarMesa(numero: Int, capacidad: Int) {
        viewModelScope.launch {
            mesaDao.insert(Mesa(numero = numero, capacidad = capacidad, estado = "LIBRE"))
        }
    }

    fun actualizarEstado(mesa: Mesa, nuevoEstado: String) {
        viewModelScope.launch {
            mesaDao.update(mesa.copy(estado = nuevoEstado))
        }
    }

    fun eliminarMesa(mesa: Mesa) {
        viewModelScope.launch {
            mesaDao.delete(mesa)
        }
    }
}