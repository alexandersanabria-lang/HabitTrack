package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habittrack.data.local.HabitEntity
import com.habittrack.data.local.HabitLogEntity
import com.habittrack.viewmodel.HabitViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    viewModel: HabitViewModel,
    onHabitClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onStatsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val today = LocalDate.now().toString()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "HabitTrack",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    },
                    actions = {
                        IconButton(onClick = onStatsClick) {
                            Icon(
                                Icons.Default.QueryStats,
                                contentDescription = "Estadísticas",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = Color(0xFF43A047),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                }
            }
        ) { padding ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF43A047))
                    }
                }
                uiState.habits.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("🌱", fontSize = 64.sp)
                            Text(
                                "No tienes hábitos aún",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Text(
                                "Pulsa + para agregar uno",
                                fontSize = 14.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Mis hábitos (${uiState.habits.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF43A047),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        items(uiState.habits) { habit ->
                            val isCompleted = uiState.completedToday.contains(habit.id)
                            HabitCard(
                                habit = habit,
                                isCompleted = isCompleted,
                                onClick = { onHabitClick(habit.id) },
                                onCheckClick = {
                                    if (!isCompleted) {
                                        viewModel.insertLog(
                                            HabitLogEntity(
                                                habitId = habit.id,
                                                date = today,
                                                completed = true
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitEntity,
    isCompleted: Boolean,
    onClick: () -> Unit,
    onCheckClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isCompleted) Color(0xFF1A2E1A) else Color(0xFF1E1E1E)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = getCategoryEmoji(habit.category), fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isCompleted) Color(0xFF43A047) else Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isCompleted) "✅ Completado hoy" else habit.category,
                    fontSize = 13.sp,
                    color = if (isCompleted) Color(0xFF43A047) else Color(0xFF888888)
                )
            }
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { onCheckClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF43A047),
                    uncheckedColor = Color(0xFF555555)
                )
            )
        }
    }
}

fun getCategoryEmoji(category: String): String {
    return when (category.lowercase()) {
        "gimnasio", "gym" -> "🏋️"
        "lectura" -> "📚"
        "hidratación", "hidratacion" -> "💧"
        "meditación", "meditacion" -> "🧘"
        "idiomas" -> "🗣️"
        else -> "⭐"
    }
}