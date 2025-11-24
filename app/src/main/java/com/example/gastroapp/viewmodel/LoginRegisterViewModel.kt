package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.model.User
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val repo: ReservaRepository) : ViewModel() {


    fun registrar(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {


            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                onResult(false, "Todos los campos son obligatorios")
                return@launch
            }

            if (!email.contains("@")) {
                onResult(false, "El email no es valido")
                return@launch
            }

            if (password.length < 6) {
                onResult(false, "La contraseña debe tener minimo 6 caracteres")
                return@launch
            }


            val existe = repo.obtenerUsuarioPorEmail(email)

            if (existe != null) {
                onResult(false, "El email ya está registrado")
                return@launch
            }


            val user = User(
                name = name,
                email = email,
                password = password,
                role = "CLIENTE"
            )

            repo.crearUsuario(user)

            onResult(true, "Cuenta creada correctamente")
        }
    }


    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String, String) -> Unit
    ) {
        viewModelScope.launch {

            val user = repo.obtenerUsuarioPorEmail(email)

            if (user == null) {
                onResult(false, "Usuario no encontrado", "")
                return@launch
            }

            if (user.password != password) {
                onResult(false, "Contraseña incorrecta", "")
                return@launch
            }


            val rol = if (user.email == "admin@admin.com") "ADMIN" else "CLIENTE"

            onResult(true, "Login correcto", rol)
        }
    }
}