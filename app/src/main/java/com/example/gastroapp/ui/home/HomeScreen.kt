package com.example.gastroapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onReservarClick: () -> Unit,
    onMisReservasClick: () -> Unit
) {

    var visible by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }


    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3E5F5), Color(0xFFEDE7F6), Color(0xFFE8F5E9))
    )

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "GastroApp",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Tu mesa, a un toque de distancia",
                        color = Color(0xFF6A1B9A),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(60.dp))

                    Button(
                        onClick = onReservarClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7B1FA2),
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Reservar Mesa", fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = onMisReservasClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4A148C)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF7B1FA2), Color(0xFF4A148C))
                            )
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Mis Reservas", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}