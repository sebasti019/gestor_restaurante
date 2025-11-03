package com.example.gastroapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservas",
    indices = [Index("userId"), Index("mesaId")]
)
data class Reserva(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long? = null,
    val mesaId: Long? = null,
    val nombreReserva: String,
    val contacto: String,
    val fecha: Long,
    val hora: String,
    val numComensales: Int,
    val estado: String
)