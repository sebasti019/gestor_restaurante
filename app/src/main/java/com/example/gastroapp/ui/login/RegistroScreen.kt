package com.example.gastroapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.viewmodel.LoginRegisterViewModel
import com.example.gastroapp.viewmodel.LoginRegisterViewModelFactory

@Composable
fun RegistroScreen(onRegistrado: () -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repo = ReservaRepository(db.userDao(), db.mesaDao(), db.reservaDao())

    val viewModel: LoginRegisterViewModel =
        viewModel(factory = LoginRegisterViewModelFactory(repo))

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Crear Cuenta", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electronico") })
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmar,
            onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {


                when {
                    name.isBlank() -> { mensaje = "El nombre es obligatorio"; return@Button }
                    email.isBlank() -> { mensaje = "El correo es obligatorio"; return@Button }
                    !email.contains("@") -> { mensaje = "Correo inválido"; return@Button }
                    !email.endsWith("@gmail.com") -> { mensaje = "Debe ser un correo @gmail.com"; return@Button }
                    password.length < 6 -> { mensaje = "La contraseña debe tener al menos 6 caracteres"; return@Button }
                    confirmar != password -> { mensaje = "Las contraseñas no coinciden"; return@Button }
                }

                viewModel.registrar(name, email, password) { ok, msg ->
                    mensaje = msg
                    if (ok) onRegistrado()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        if (mensaje.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}