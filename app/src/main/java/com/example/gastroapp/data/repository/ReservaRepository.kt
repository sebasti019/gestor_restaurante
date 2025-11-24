package com.example.gastroapp.data.repository

import com.example.gastroapp.data.local.*
import com.example.gastroapp.model.*

class ReservaRepository(
    private val userDao: UserDao,
    private val mesaDao: MesaDao,
    private val reservaDao: ReservaDao
) {


    suspend fun crearUsuario(user: User): Long = userDao.insert(user)
    suspend fun obtenerUsuarioPorEmail(email: String): User? = userDao.getByEmail(email)


    fun obtenerMesas() = mesaDao.getAll()
    suspend fun insertarMesa(mesa: Mesa) = mesaDao.insert(mesa)
    suspend fun actualizarMesa(mesa: Mesa) = mesaDao.update(mesa)


    fun obtenerReservas() = reservaDao.getAll()
    suspend fun insertarReserva(reserva: Reserva) = reservaDao.insert(reserva)
    suspend fun actualizarReserva(reserva: Reserva) = reservaDao.update(reserva)

    suspend fun obtenerMesaPorId(id: Long): Mesa? = mesaDao.getMesaPorId(id)
}