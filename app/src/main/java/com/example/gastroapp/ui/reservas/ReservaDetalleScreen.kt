package com.example.gastroapp.ui.reservas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.model.Reserva
import com.example.gastroapp.viewmodel.ReservaViewModel
import com.example.gastroapp.viewmodel.ReservaViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReservaDetalleScreen(
    reservaId: Long,
    onVolver: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { ReservaRepository(db.userDao(), db.mesaDao(), db.reservaDao()) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repo))
    val uiState = viewModel.uiState.collectAsState()

    val reserva = uiState.value.reservas.find { it.id == reservaId }
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    var numeroMesa by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(reserva?.mesaId) {
        if (reserva?.mesaId != null) {
            val mesa = repo.obtenerMesaPorId(reserva.mesaId)
            numeroMesa = mesa?.numero?.toString()
        }
    }

    if (reserva != null) {
        DetalleReservaCard(
            reserva = reserva,
            fecha = sdf.format(Date(reserva.fecha)),
            numeroMesa = numeroMesa,
            onVolver = onVolver
        )
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Reserva no encontrada")
        }
    }
}

@Composable
private fun DetalleReservaCard(
    reserva: Reserva,
    fecha: String,
    numeroMesa: String?,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Detalle de Reserva", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text("Nombre: ${reserva.nombreReserva}")
        Text("Comensales: ${reserva.numComensales}")
        Text("Fecha: $fecha")
        Text("Hora: ${reserva.hora}")
        Text("Contacto: ${reserva.contacto}")
        Text("Estado: ${reserva.estado}")
        Text("Mesa asignada: ${numeroMesa ?: "Aun sin asignar"}")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}