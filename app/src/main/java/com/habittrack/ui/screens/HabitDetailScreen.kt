package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habittrack.ui.theme.*
import com.habittrack.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Int,
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val habit = uiState.habits.find { it.id == habitId }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val accentColor = habit?.let { getCategoryColor(it.category) } ?: GreenNeon
    val selectedOption = categoryOptions.find {
        it.name.lowercase() == (habit?.category?.lowercase() ?: "")
    } ?: categoryOptions[0]

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Black80,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    "¿Eliminar hábito?",
                    color = White100,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    "Esta acción no se puede deshacer.",
                    color = White70,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RedError.copy(alpha = 0.15f))
                        .border(1.dp, RedError.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .clickable {
                            habit?.let { viewModel.deleteHabit(it) }
                            showDeleteDialog = false
                            onBack()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Eliminar", color = RedError, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(GlassSurface)
                        .clickable { showDeleteDialog = false }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Cancelar", color = White70)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        // Glow decorativo superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.15f), Color.Transparent),
                        radius = 600f
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Black100.copy(alpha = 0.92f))
                        .padding(horizontal = 8.dp)
                        .padding(top = 44.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = White100
                        )
                    }
                    Text(
                        text = "Detalle",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = White100,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onEdit) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.15f))
                                .border(1.dp, accentColor.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RedError.copy(alpha = 0.1f))
                                .border(1.dp, RedError.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = RedError,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        ) { padding ->
            if (habit == null) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GreenNeon, strokeWidth = 2.dp)
                }
            } else {
                val isCompletedToday = uiState.completedToday.contains(habit.id)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // Hero card
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            accentColor.copy(alpha = 0.2f),
                                            Black70
                                        )
                                    )
                                )
                                .border(
                                    1.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            accentColor.copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    ),
                                    RoundedCornerShape(28.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(accentColor.copy(alpha = 0.2f))
                                        .border(
                                            1.5.dp,
                                            accentColor.copy(alpha = 0.5f),
                                            RoundedCornerShape(22.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(selectedOption.emoji, fontSize = 36.sp)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = habit.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 22.sp,
                                        color = White100
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(accentColor.copy(alpha = 0.15f))
                                            .border(
                                                1.dp,
                                                accentColor.copy(alpha = 0.3f),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = habit.category,
                                            fontSize = 12.sp,
                                            color = accentColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Estado hoy
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    if (isCompletedToday)
                                        Brush.linearGradient(
                                            listOf(GreenDark2.copy(alpha = 0.5f), Black70)
                                        )
                                    else
                                        Brush.linearGradient(listOf(Black70, Black70))
                                )
                                .border(
                                    1.dp,
                                    if (isCompletedToday) GreenNeon.copy(alpha = 0.4f)
                                    else GlassBorder,
                                    RoundedCornerShape(22.dp)
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    if (isCompletedToday) "🎉" else "⏳",
                                    fontSize = 36.sp
                                )
                                Column {
                                    Text(
                                        if (isCompletedToday) "¡Completado hoy!" else "Pendiente para hoy",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = if (isCompletedToday) GreenNeon else White100
                                    )
                                    Text(
                                        if (isCompletedToday) "Sigue así, vas muy bien 💪"
                                        else "Aún tienes tiempo de hacerlo",
                                        fontSize = 12.sp,
                                        color = White70
                                    )
                                }
                            }
                        }
                    }

                    // Info
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(Black70)
                                .border(1.dp, GlassBorder, RoundedCornerShape(22.dp))
                                .padding(20.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    "Información",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = accentColor
                                )
                                DetailInfoRow(
                                    label = "Frecuencia",
                                    value = habit.frequencyDays,
                                    accent = accentColor
                                )
                                DetailInfoRow(
                                    label = "Estado",
                                    value = if (habit.isActive) "Activo ✅" else "Inactivo",
                                    accent = accentColor
                                )
                                DetailInfoRow(
                                    label = "Creado",
                                    value = java.time.Instant
                                        .ofEpochMilli(habit.createdAt)
                                        .atZone(java.time.ZoneId.systemDefault())
                                        .toLocalDate()
                                        .toString(),
                                    accent = accentColor
                                )
                            }
                        }
                    }

                    // Motivación
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(accentColor.copy(alpha = 0.08f), Black70)
                                    )
                                )
                                .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
                                .padding(20.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("✨", fontSize = 28.sp)
                                Text(
                                    "\"La constancia es la clave del éxito\"",
                                    color = White70,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoRow(label: String, value: String, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = White70, fontSize = 13.sp)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(accent.copy(alpha = 0.1f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                value,
                color = accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}