package com.habittrack.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
    CategoryOption("Gimnasio",    "🏋️", CyanAccent),
    CategoryOption("Lectura",     "📚", PurpleAccent),
    CategoryOption("Hidratación", "💧", BlueAccent),
    CategoryOption("Meditación",  "🧘", OrangeAccent),
    CategoryOption("Idiomas",     "🗣️", YellowAccent),
    CategoryOption("Nutrición",   "🥗", GreenAccent),
    CategoryOption("Sueño",       "😴", Color(0xFF9C27B0)),
    CategoryOption("Otro",        "⭐", GreenNeon)
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

    val animatedAccent by animateColorAsState(
        targetValue = accentColor,
        animationSpec = spring(),
        label = "accent"
    )

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(animatedAccent.copy(alpha = 0.2f), Color.Transparent),
                        radius = 700f
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Black100.copy(alpha = 0.88f))
                        .padding(horizontal = 8.dp)
                        .padding(top = 44.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GlassSurface)
                            .border(1.dp, GlassBorder, CircleShape)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = White100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (isEditing) "Editar hábito" else "Nuevo hábito",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            style = MaterialTheme.typography.titleLarge.copy(
                                brush = Brush.horizontalGradient(
                                    listOf(animatedAccent, animatedAccent.copy(alpha = 0.6f))
                                )
                            )
                        )
                        Text(
                            text = if (isEditing) "Modifica los datos de tu hábito"
                            else "Define tu próximo hábito ganador",
                            fontSize = 12.sp,
                            color = White70
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // — Nombre —
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(animatedAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "NOMBRE DEL HÁBITO",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = animatedAccent,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Black70)
                            .border(
                                width = if (name.isNotBlank()) 1.5.dp else 1.dp,
                                color = if (name.isNotBlank()) animatedAccent.copy(alpha = 0.6f)
                                else GlassBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "Ej: Correr 30 minutos al día",
                                    color = White40,
                                    fontSize = 15.sp
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = White100
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = White100,
                                unfocusedTextColor = White100,
                                cursorColor = animatedAccent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                    }
                }

                // — Categoría carrusel —
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(animatedAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "CATEGORÍA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = animatedAccent,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "← desliza →",
                            fontSize = 10.sp,
                            color = White40
                        )
                    }

                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        itemsIndexed(categoryOptions) { _, option ->
                            val isSelected = option.name.lowercase() == category.lowercase()

                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.1f else 0.92f,
                                animationSpec = spring(dampingRatio = 0.6f),
                                label = "scale"
                            )
                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) option.color.copy(alpha = 0.2f)
                                else Black70,
                                animationSpec = spring(),
                                label = "bg"
                            )

                            Column(
                                modifier = Modifier
                                    .scale(scale)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(bgColor)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) option.color
                                        else GlassBorder,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .clickable { category = option.name }
                                    .padding(vertical = 20.dp, horizontal = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Emoji con fondo
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected)
                                                Brush.radialGradient(
                                                    listOf(
                                                        option.color.copy(alpha = 0.4f),
                                                        option.color.copy(alpha = 0.1f)
                                                    )
                                                )
                                            else
                                                Brush.radialGradient(
                                                    listOf(
                                                        White40.copy(alpha = 0.08f),
                                                        Color.Transparent
                                                    )
                                                )
                                        )
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) option.color.copy(alpha = 0.6f)
                                            else GlassBorder,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(option.emoji, fontSize = 24.sp)
                                }

                                Text(
                                    option.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold
                                    else FontWeight.Normal,
                                    color = if (isSelected) option.color else White70,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )

                                // Indicador selección
                                Box(
                                    modifier = Modifier
                                        .height(3.dp)
                                        .width(if (isSelected) 40.dp else 0.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(option.color)
                                )
                            }
                        }
                    }

                    // Indicadores de posición
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        categoryOptions.forEach { option ->
                            val isSelected = option.name.lowercase() == category.lowercase()
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(if (isSelected) 8.dp else 5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) animatedAccent
                                        else White40.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                }

                // — Preview —
                if (name.isNotBlank()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(animatedAccent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "VISTA PREVIA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = animatedAccent,
                                letterSpacing = 1.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Black70, animatedAccent.copy(alpha = 0.08f))
                                    )
                                )
                                .border(
                                    1.5.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            animatedAccent.copy(alpha = 0.6f),
                                            animatedAccent.copy(alpha = 0.1f)
                                        )
                                    ),
                                    RoundedCornerShape(22.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    animatedAccent.copy(alpha = 0.25f),
                                                    animatedAccent.copy(alpha = 0.08f)
                                                )
                                            )
                                        )
                                        .border(
                                            1.dp,
                                            animatedAccent.copy(alpha = 0.5f),
                                            RoundedCornerShape(15.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(selectedOption.emoji, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        name,
                                        fontWeight = FontWeight.Bold,
                                        color = White100,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(animatedAccent.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            category,
                                            fontSize = 11.sp,
                                            color = animatedAccent,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .height(3.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(
                                                        animatedAccent.copy(alpha = 0.8f),
                                                        animatedAccent.copy(alpha = 0.2f)
                                                    )
                                                )
                                            )
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .border(
                                            1.5.dp,
                                            animatedAccent.copy(alpha = 0.4f),
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }

                // — Botón guardar —
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (name.isNotBlank())
                                Brush.horizontalGradient(
                                    listOf(animatedAccent.copy(alpha = 0.8f), animatedAccent)
                                )
                            else
                                Brush.horizontalGradient(listOf(Black70, Black70))
                        )
                        .border(
                            1.dp,
                            if (name.isNotBlank()) animatedAccent.copy(alpha = 0.5f)
                            else GlassBorder,
                            RoundedCornerShape(20.dp)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (name.isNotBlank()) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = Black100,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = if (isEditing) "Guardar cambios" else "Crear hábito",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = if (name.isNotBlank()) Black100 else White40
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}