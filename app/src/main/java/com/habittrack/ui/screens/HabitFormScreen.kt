package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habittrack.data.local.HabitEntity
import com.habittrack.viewmodel.HabitViewModel

val GreenDark = Color(0xFF1B5E20)
val GreenMid = Color(0xFF2E7D32)
val GreenLight = Color(0xFF43A047)
val CardBg = Color(0xFF1E1E1E)
val SurfaceBg = Color(0xFF121212)

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
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Gimnasio", "Lectura", "Hidratación", "Meditación", "Idiomas", "Otro")
    val isEditing = habitId != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (isEditing) "Editar hábito" else "Nuevo hábito",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GreenMid,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardBg)
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Información del hábito",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = GreenLight
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GreenLight,
                                unfocusedBorderColor = Color(0xFF333333),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = GreenLight,
                            )
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = getCategoryEmoji(category) + "  " + category,
                                onValueChange = {},
                                readOnly = true,
                                label = null,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenLight,
                                    unfocusedBorderColor = Color(0xFF333333),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = GreenLight,
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(CardBg)
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                getCategoryEmoji(cat) + "  " + cat,
                                                color = Color.White
                                            )
                                        },
                                        onClick = {
                                            category = cat
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (name.isNotBlank())
                                Brush.horizontalGradient(listOf(GreenDark, GreenLight))
                            else
                                Brush.horizontalGradient(listOf(Color(0xFF2A2A2A), Color(0xFF2A2A2A)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
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
                        enabled = name.isNotBlank(),
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = if (isEditing) "✅  Guardar cambios" else "✨  Crear hábito",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (name.isNotBlank()) Color.White else Color(0xFF555555)
                        )
                    }
                }
            }
        }
    }
}