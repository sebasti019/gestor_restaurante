package com.example.gastroapp.ui.reservas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.model.Reserva
import com.example.gastroapp.viewmodel.ReservaViewModel
import com.example.gastroapp.viewmodel.ReservaViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(
    onReservaConfirmada: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { ReservaRepository(db.userDao(), db.mesaDao(), db.reservaDao()) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repo))

    var nombre by remember { mutableStateOf("") }
    var comensales by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }


    var mostrarDialogo by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nueva Reserva", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = comensales, onValueChange = { comensales = it }, label = { Text("N° comensales") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha (dd/MM/yyyy)") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = hora, onValueChange = { hora = it }, label = { Text("Hora (HH:mm)") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = contacto, onValueChange = { contacto = it }, label = { Text("Contacto") })
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val numComensales = comensales.toIntOrNull()
                    val parsedDate = try { sdf.parse(fecha) } catch (e: Exception) { null }

                    when {
                        nombre.isBlank() -> errorMsg = "El nombre es obligatorio"
                        contacto.isBlank() -> errorMsg = "El contacto es obligatorio"
                        numComensales == null || numComensales <= 0 -> errorMsg = "Número de comensales invalido"
                        parsedDate == null -> errorMsg = "Formato de fecha invalido (usa dd/MM/yyyy)"
                        parsedDate.before(Date()) -> errorMsg = "La fecha no puede ser anterior a hoy"
                        else -> {
                            val reserva = Reserva(
                                userId = 1,
                                mesaId = null,
                                nombreReserva = nombre,
                                contacto = contacto,
                                fecha = parsedDate.time,
                                hora = hora.ifBlank { "00:00" },
                                numComensales = numComensales,
                                estado = "SOLICITADA"
                            )
                            viewModel.crearReserva(context, reserva)
                            errorMsg = null
                            mostrarDialogo = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }

            errorMsg?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }


        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogo = false
                        onReservaConfirmada()
                    }) {
                        Text("Aceptar")
                    }
                },
                title = { Text("Reserva Confirmada") },
                text = { Text("Tu reserva ha sido guardada, añadida al calendario y recibiras un recordatorio.") }
            )
        }
    }
}