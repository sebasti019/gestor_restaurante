package com.example.gastroapp.data.local

import androidx.room.*
import com.example.gastroapp.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {
    @Query("SELECT * FROM reservas ORDER BY fecha")
    fun getAll(): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserva: Reserva): Long

    @Update
    suspend fun update(reserva: Reserva)
}