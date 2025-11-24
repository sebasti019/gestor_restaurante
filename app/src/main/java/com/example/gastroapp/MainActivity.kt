package com.example.gastroapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastroapp.ui.login.LoginScreen
import com.example.gastroapp.ui.reservas.ReservaFormScreen
import com.example.gastroapp.ui.theme.GastroappTheme
import com.example.gastroapp.ui.reservas.MisReservasScreen
import com.example.gastroapp.ui.reservas.ReservaDetalleScreen
import com.example.gastroapp.ui.admin.AdminDashboardScreen
import com.example.gastroapp.ui.admin.AdminReservasScreen
import android.os.Build
import androidx.core.app.ActivityCompat
import com.example.gastroapp.ui.home.HomeScreen
import com.example.gastroapp.ui.login.RegistroScreen
import com.example.gastroapp.data.local.AppDatabase
import com.example.gastroapp.data.repository.ReservaRepository
import com.example.gastroapp.model.User
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permisos = arrayOf(
                android.Manifest.permission.WRITE_CALENDAR,
                android.Manifest.permission.READ_CALENDAR,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            ActivityCompat.requestPermissions(this, permisos, 200)
        }


        val db = AppDatabase.getInstance(this)

        lifecycleScope.launch {
            val existeAdmin = db.userDao().getByEmail("admin@admin.com")

            if (existeAdmin == null) {
                db.userDao().insert(
                    User(
                        name = "Administrador",
                        email = "admin@admin.com",
                        password = "admin1",
                        role = "ADMIN"
                    )
                )
            }
        }


        setContent {
            GastroappTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(onLoginSuccess = { rol ->
                when (rol) {
                    "ADMIN" -> navController.navigate("admin")
                    "CLIENTE" -> navController.navigate("home")
                    "registro" -> navController.navigate("registro")
                }
            })
        }

        composable("home") {
            HomeScreen(
                onReservarClick = { navController.navigate("formulario") },
                onMisReservasClick = { navController.navigate("mis_reservas") }
            )
        }

        composable("formulario") {
            ReservaFormScreen(onReservaConfirmada = {
                navController.navigate("mis_reservas")
            })
        }

        composable("mis_reservas") {
            MisReservasScreen(onVerDetalle = { id ->
                navController.navigate("detalle/$id")
            })
        }

        composable("detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0
            ReservaDetalleScreen(reservaId = id, onVolver = { navController.popBackStack() })
        }

        composable("admin") {
            AdminDashboardScreen(onVerReservas = {
                navController.navigate("admin_reservas")
            })
        }

        composable("admin_reservas") {
            AdminReservasScreen()
        }

        composable("registro") {
            RegistroScreen(onRegistrado = {
                navController.navigate("login")
            })
        }
    }
}