package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
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
import com.habittrack.ui.theme.*
import com.habittrack.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    val fecha = LocalDate.now().format(
        DateTimeFormatter.ofPattern("EEEE, d MMM", Locale("es"))
    ).replaceFirstChar { it.uppercase() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        // Glow superior decorativo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(GreenDark1.copy(alpha = 0.25f), Color.Transparent),
                        radius = 600f
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Black100.copy(alpha = 0.92f))
                        .padding(horizontal = 20.dp)
                        .padding(top = 48.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "HabitTrack",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp,
                                style = MaterialTheme.typography.displaySmall.copy(
                                    brush = Brush.horizontalGradient(
                                        listOf(GreenNeon, GreenPrimary)
                                    )
                                )
                            )
                            Text(
                                text = fecha,
                                fontSize = 13.sp,
                                color = White70,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GreenGlass)
                                .border(1.dp, GreenNeon.copy(alpha = 0.4f), CircleShape)
                                .clickable { onStatsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.BarChart,
                                contentDescription = "Estadísticas",
                                tint = GreenNeon,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resumen del día
                    if (uiState.habits.isNotEmpty()) {
                        val completados = uiState.completedToday.size
                        val total = uiState.habits.size
                        val progreso = completados.toFloat() / total.toFloat()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(GreenDark2.copy(alpha = 0.6f), Black70)
                                    )
                                )
                                .border(1.dp, GreenNeon.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                                .padding(14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Progreso de hoy",
                                        color = White70,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        "$completados / $total",
                                        color = GreenNeon,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(White40.copy(alpha = 0.3f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progreso)
                                            .fillMaxHeight()
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(GreenDark1, GreenNeon)
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(GreenPrimary, GreenNeon)
                            )
                        )
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar",
                        tint = Black100,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        ) { padding ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenNeon, strokeWidth = 2.dp)
                    }
                }
                uiState.habits.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(GreenGlass)
                                    .border(1.dp, GreenNeon.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🌱", fontSize = 52.sp)
                            }
                            Text(
                                "Sin hábitos aún",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                color = White100
                            )
                            Text(
                                "Toca + para crear tu primer hábito",
                                fontSize = 14.sp,
                                color = White70
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(
                            start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Mis hábitos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = White70,
                                modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
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
    val accentColor = getCategoryColor(habit.category)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Black70,
                        accentColor.copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        accentColor.copy(alpha = if (isCompleted) 0.7f else 0.25f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Emoji en caja con glow
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                accentColor.copy(alpha = 0.2f),
                                accentColor.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        accentColor.copy(alpha = 0.4f),
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(getCategoryEmoji(habit.category), fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isCompleted) accentColor else White100
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accentColor.copy(alpha = 0.12f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = habit.category,
                            fontSize = 11.sp,
                            color = accentColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (isCompleted) {
                        Text(
                            "· Listo hoy ✓",
                            fontSize = 11.sp,
                            color = GreenNeon.copy(alpha = 0.8f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Barra progreso
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(White40.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (isCompleted) 1f else 0.15f)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(accentColor.copy(alpha = 0.6f), accentColor)
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) accentColor.copy(alpha = 0.2f)
                        else White40.copy(alpha = 0.05f)
                    )
                    .border(
                        1.5.dp,
                        if (isCompleted) accentColor else White40.copy(alpha = 0.3f),
                        CircleShape
                    )
                    .clickable { onCheckClick() },
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Text("✓", color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }
        }
    }
}

fun getCategoryEmoji(category: String): String {
    return when (category.lowercase()) {
        "gimnasio", "gym"            -> "🏋️"
        "lectura"                    -> "📚"
        "hidratación", "hidratacion" -> "💧"
        "meditación", "meditacion"   -> "🧘"
        "idiomas"                    -> "🗣️"
        else                         -> "⭐"
    }
}

fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "gimnasio", "gym"            -> CyanAccent
        "lectura"                    -> PurpleAccent
        "hidratación", "hidratacion" -> BlueAccent
        "meditación", "meditacion"   -> OrangeAccent
        "idiomas"                    -> YellowAccent
        else                         -> GreenAccent
    }
}