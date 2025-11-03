package com.example.gastroapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.content.Intent

data class ReservaUiState(
    val mensaje: String? = null,
    val exito: Boolean = false,
    val reservas: List<Reserva> = emptyList()
)

class ReservaViewModel(private val repo: ReservaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservaUiState())
    val uiState: StateFlow<ReservaUiState> = _uiState

    init {
        cargarReservas()
    }

    fun cargarReservas() {
        viewModelScope.launch {
            repo.obtenerReservas().collect { lista ->
                _uiState.value = _uiState.value.copy(reservas = lista)
            }
        }
    }

    fun crearReserva(context: Context, reserva: Reserva) {
        viewModelScope.launch {
            repo.insertarReserva(reserva)
            _uiState.value = _uiState.value.copy(
                mensaje = "Reserva creada correctamente",
                exito = true
            )


            programarNotificacion(context, reserva)
            agregarEventoAlCalendario(context, reserva)
        }
    }
    fun actualizarEstado(reserva: Reserva, nuevoEstado: String) {
        viewModelScope.launch {
            val actualizada = reserva.copy(estado = nuevoEstado)


            if (nuevoEstado == "CONFIRMADA") {
                val mesasLibres = repo.obtenerMesas().firstOrNull()?.filter { it.estado == "LIBRE" }
                val mesaAsignada = mesasLibres?.firstOrNull()

                if (mesaAsignada != null) {

                    val mesaActualizada = mesaAsignada.copy(estado = "RESERVADA")
                    repo.actualizarMesa(mesaActualizada)


                    val reservaConMesa = actualizada.copy(mesaId = mesaAsignada.id)
                    repo.actualizarReserva(reservaConMesa)
                } else {

                    val reservaPendiente = actualizada.copy(estado = "PENDIENTE - SIN MESA")
                    repo.actualizarReserva(reservaPendiente)
                }
            } else {

                repo.actualizarReserva(actualizada)


                if (nuevoEstado == "CANCELADA" && reserva.mesaId != null) {
                    val mesa = repo.obtenerMesas().firstOrNull()?.find { it.id == reserva.mesaId }
                    mesa?.let {
                        repo.actualizarMesa(it.copy(estado = "LIBRE"))
                    }
                }


                if (nuevoEstado == "REALIZADA" && reserva.mesaId != null) {
                    val mesa = repo.obtenerMesas().firstOrNull()?.find { it.id == reserva.mesaId }
                    mesa?.let {
                        repo.actualizarMesa(it.copy(estado = "LIBRE"))
                    }
                }
            }


            cargarReservas()
        }
    }
    private fun programarNotificacion(context: Context, reserva: Reserva) {
        val intent = Intent(context, com.example.gastroapp.notifications.ReservaNotificationReceiver::class.java)
        intent.putExtra("nombre", reserva.nombreReserva)
        intent.putExtra("hora", reserva.hora)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reserva.id?.toInt() ?: 0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager

        //  Para pruebas10 seg
        val triggerTime = System.currentTimeMillis() + 10_000L

        alarmManager.setExact(
            android.app.AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
    private fun agregarEventoAlCalendario(context: Context, reserva: Reserva) {
        try {
            val startMillis = reserva.fecha
            val endMillis = startMillis + (2 * 60 * 60 * 1000)

            val values = android.content.ContentValues().apply {
                put(android.provider.CalendarContract.Events.DTSTART, startMillis)
                put(android.provider.CalendarContract.Events.DTEND, endMillis)
                put(android.provider.CalendarContract.Events.TITLE, "Reserva en GastroApp")
                put(android.provider.CalendarContract.Events.DESCRIPTION, "Reserva a nombre de ${reserva.nombreReserva}")
                put(android.provider.CalendarContract.Events.EVENT_TIMEZONE, java.util.TimeZone.getDefault().id)
                put(android.provider.CalendarContract.Events.CALENDAR_ID, 1)
            }

            val uri = context.contentResolver.insert(
                android.provider.CalendarContract.Events.CONTENT_URI,
                values
            )

            if (uri != null) {
                println("Evento agregado al calendario: $uri")
            } else {
                println("No se pudo agregar el evento")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}