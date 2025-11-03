package com.example.gastroapp.data.local

import androidx.room.*
import com.example.gastroapp.model.Mesa
import kotlinx.coroutines.flow.Flow

@Dao
interface MesaDao {
    @Query("SELECT * FROM mesas")
    fun getAll(): Flow<List<Mesa>>

    @Insert
    suspend fun insert(mesa: Mesa)

    @Update
    suspend fun update(mesa: Mesa)

    @Delete
    suspend fun delete(mesa: Mesa)

    @Query("SELECT * FROM mesas WHERE id = :id")
    suspend fun getMesaPorId(id: Long): Mesa?
}