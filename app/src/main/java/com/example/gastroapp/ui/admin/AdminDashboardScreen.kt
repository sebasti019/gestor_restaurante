package com.example.gastroapp.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.model.Mesa
import com.example.gastroapp.viewmodel.MesaViewModel
import com.example.gastroapp.viewmodel.MesaViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete


@Composable
fun AdminDashboardScreen(
    onVerReservas: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val dao = db.mesaDao()
    val viewModel: MesaViewModel = viewModel(factory = MesaViewModelFactory(dao))
    val mesas by viewModel.mesas.collectAsState(initial = emptyList())

    var numero by remember { mutableStateOf("") }
    var capacidad by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Panel del Administrador", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))


        Button(
            onClick = { onVerReservas() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Reservas")
        }

        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("N° Mesa") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = capacidad,
                onValueChange = { capacidad = it },
                label = { Text("Capacidad") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                val n = numero.toIntOrNull()
                val c = capacidad.toIntOrNull()
                if (n != null && c != null) {
                    viewModel.agregarMesa(n, c)
                    numero = ""
                    capacidad = ""
                }
            }) {
                Text("Agregar")
            }
        }

        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(mesas.size) { i ->
                val mesa = mesas[i]
                MesaCard(
                    mesa = mesa,
                    onCambiarEstado = { nuevo ->
                        viewModel.actualizarEstado(mesa, nuevo)
                    },
                    onEliminar = {
                        viewModel.eliminarMesa(mesa)
                    }
                )
            }
        }
    }
}

@Composable
fun MesaCard(mesa: Mesa, onCambiarEstado: (String) -> Unit, onEliminar: () -> Unit) {
    val color = when (mesa.estado) {
        "LIBRE" -> Color(0xFFB2FF59)
        "RESERVADA" -> Color(0xFFFFFF8D)
        "OCUPADA" -> Color(0xFFFF8A80)
        else -> Color.LightGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Mesa ${mesa.numero} - Capacidad: ${mesa.capacidad}")
            Text("Estado: ${mesa.estado}")
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onCambiarEstado("LIBRE") }) { Text("Libre") }
                TextButton(onClick = { onCambiarEstado("RESERVADA") }) { Text("Reservar") }
                TextButton(onClick = { onCambiarEstado("OCUPADA") }) { Text("Ocupar") }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
