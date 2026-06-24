package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.habittrack.data.local.HabitEntity
import com.habittrack.ui.theme.*
import com.habittrack.viewmodel.HabitViewModel

data class CategoryOption(
    val name: String,
    val emoji: String,
    val color: Color
)

val categoryOptions = listOf(
    CategoryOption("Gimnasio",   "🏋️", CyanAccent),
    CategoryOption("Lectura",    "📚", PurpleAccent),
    CategoryOption("Hidratación","💧", BlueAccent),
    CategoryOption("Meditación", "🧘", OrangeAccent),
    CategoryOption("Idiomas",    "🗣️", YellowAccent),
    CategoryOption("Otro",       "⭐", GreenAccent)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitFormScreen(
    habitId: Int?,
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val existingHabit = habitId?.let { id -> uiState.habits.find { it.id == id } }

    var name by remember { mutableStateOf(existingHabit?.name ?: "") }
    var category by remember { mutableStateOf(existingHabit?.category ?: "Gimnasio") }

    val isEditing = habitId != null
    val selectedOption = categoryOptions.find {
        it.name.lowercase() == category.lowercase()
    } ?: categoryOptions[0]
    val accentColor = selectedOption.color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        // Glow decorativo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.12f), Color.Transparent),
                        radius = 500f
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
                        text = if (isEditing) "Editar hábito" else "Nuevo hábito",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = White100
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // — Nombre —
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Nombre",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = White70,
                        letterSpacing = 1.sp
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Ej: Correr 30 minutos", color = White40, fontSize = 15.sp)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = White100
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = White100,
                            unfocusedTextColor = White100,
                            cursorColor = accentColor,
                            focusedContainerColor = Black70,
                            unfocusedContainerColor = Black70
                        )
                    )
                }

                // — Categoría en grid de chips —
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Categoría",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = White70,
                        letterSpacing = 1.sp
                    )

                    // Grid 2 columnas
                    val rows = categoryOptions.chunked(2)
                    rows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row.forEach { option ->
                                val isSelected = option.name.lowercase() == category.lowercase()
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            if (isSelected)
                                                Brush.linearGradient(
                                                    listOf(
                                                        option.color.copy(alpha = 0.25f),
                                                        option.color.copy(alpha = 0.08f)
                                                    )
                                                )
                                            else
                                                Brush.linearGradient(
                                                    listOf(Black70, Black70)
                                                )
                                        )
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) option.color else GlassBorder,
                                            shape = RoundedCornerShape(18.dp)
                                        )
                                        .clickable { category = option.name }
                                        .padding(vertical = 16.dp, horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(option.emoji, fontSize = 28.sp)
                                        Text(
                                            option.name,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) option.color else White70,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            // Si la fila tiene 1 solo elemento, rellena el espacio
                            if (row.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // — Preview —
                if (name.isNotBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Vista previa",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = White70,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Black70, accentColor.copy(alpha = 0.07f))
                                    )
                                )
                                .border(
                                    1.dp,
                                    Brush.linearGradient(
                                        listOf(accentColor.copy(alpha = 0.5f), Color.Transparent)
                                    ),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(accentColor.copy(alpha = 0.15f))
                                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(selectedOption.emoji, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        name,
                                        fontWeight = FontWeight.Bold,
                                        color = White100,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(accentColor.copy(alpha = 0.12f))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            category,
                                            fontSize = 11.sp,
                                            color = accentColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, White40.copy(alpha = 0.3f), CircleShape)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // — Botón guardar —
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (name.isNotBlank())
                                Brush.horizontalGradient(listOf(GreenDark1, GreenNeon))
                            else
                                Brush.horizontalGradient(listOf(Black70, Black70))
                        )
                        .border(
                            1.dp,
                            if (name.isNotBlank()) GreenNeon.copy(alpha = 0.4f) else GlassBorder,
                            RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            if (name.isNotBlank()) {
                                if (isEditing && existingHabit != null) {
                                    viewModel.updateHabit(
                                        existingHabit.copy(name = name, category = category)
                                    )
                                } else {
                                    viewModel.insertHabit(
                                        HabitEntity(
                                            name = name,
                                            category = category,
                                            color = "#2E7D32",
                                            frequencyDays = "[0,1,2,3,4]"
                                        )
                                    )
                                }
                                onBack()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEditing) "Guardar cambios" else "Crear hábito",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (name.isNotBlank()) Black100 else White40
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}