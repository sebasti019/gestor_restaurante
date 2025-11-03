package com.example.gastroapp.ui.reservas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.viewmodel.ReservaViewModel
import com.example.gastroapp.viewmodel.ReservaViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MisReservasScreen(
    onVerDetalle: (Long) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { ReservaRepository(db.userDao(), db.mesaDao(), db.reservaDao()) }
    val viewModel: ReservaViewModel = viewModel(factory = ReservaViewModelFactory(repo))
    val uiState = viewModel.uiState.collectAsState()

    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Reservas", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(uiState.value.reservas.size) { i ->
                val r = uiState.value.reservas[i]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onVerDetalle(r.id) },
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Reserva: ${r.nombreReserva}")
                        Text("Fecha: ${sdf.format(Date(r.fecha))}")
                        Text("Estado: ${r.estado}")
                    }
                }
            }
        }
    }
}