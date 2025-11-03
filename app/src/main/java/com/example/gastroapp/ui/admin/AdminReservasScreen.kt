package com.example.gastroapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color

@Composable
fun AdminReservasScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { ReservaRepository(db.userDao(), db.mesaDao(), db.reservaDao()) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repo))
    val uiState = viewModel.uiState.collectAsState()

    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestión de Reservas", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(uiState.value.reservas.size) { i ->
                val reserva = uiState.value.reservas[i]
                AdminReservaCard(
                    reserva = reserva,
                    fecha = sdf.format(Date(reserva.fecha)),
                    onActualizarEstado = { nuevoEstado ->
                        viewModel.actualizarEstado(reserva, nuevoEstado)
                    }
                )
            }
        }
    }
}

@Composable
fun AdminReservaCard(reserva: Reserva, fecha: String, onActualizarEstado: (String) -> Unit) {


    val targetColor = when (reserva.estado) {
        "CONFIRMADA" -> Color(0xFFC8E6C9) // verde
        "CANCELADA" -> Color(0xFFFFCDD2) // rojo
        "REALIZADA" -> Color(0xFFBBDEFB) // azul
        else -> MaterialTheme.colorScheme.surfaceVariant
    }


    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 600),
        label = "colorAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = animatedColor)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Reserva de ${reserva.nombreReserva}")
            Text("Fecha: $fecha")
            Text("Hora: ${reserva.hora}")
            Text("Comensales: ${reserva.numComensales}")
            Text("Estado actual: ${reserva.estado}")
            Text("Mesa asignada: ${reserva.mesaId ?: "Sin asignar"}")
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onActualizarEstado("CONFIRMADA") }) { Text("Confirmar") }
                TextButton(onClick = { onActualizarEstado("CANCELADA") }) { Text("Cancelar") }
                TextButton(onClick = { onActualizarEstado("REALIZADA") }) { Text("Realizada") }
            }
        }
    }
}