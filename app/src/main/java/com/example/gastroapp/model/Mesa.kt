package com.example.gastroapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesas")
data class Mesa(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val numero: Int,
    val capacidad: Int,
    val estado: String
)