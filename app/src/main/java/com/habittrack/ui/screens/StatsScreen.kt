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
import androidx.compose.material.icons.filled.Refresh
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
fun StatsScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val quoteState by viewModel.quoteState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuote()
    }

    val completados = uiState.completedToday.size
    val total = uiState.habits.size
    val progreso = if (total > 0) completados.toFloat() / total.toFloat() else 0f
    val porcentaje = (progreso * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        // Glow decorativo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(GreenDark1.copy(alpha = 0.2f), Color.Transparent),
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
                        "Estadísticas",
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
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // — Hero: anillo de progreso —
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(GreenDark2.copy(alpha = 0.5f), Black70)
                            )
                        )
                        .border(
                            1.dp,
                            GreenNeon.copy(alpha = 0.3f),
                            RoundedCornerShape(28.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Anillo circular
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.fillMaxSize(),
                                color = White40.copy(alpha = 0.15f),
                                strokeWidth = 8.dp
                            )
                            CircularProgressIndicator(
                                progress = { progreso },
                                modifier = Modifier.fillMaxSize(),
                                color = GreenNeon,
                                strokeWidth = 8.dp
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$porcentaje%",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp,
                                    color = GreenNeon
                                )
                                Text(
                                    "hoy",
                                    fontSize = 11.sp,
                                    color = White70
                                )
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Progreso del día",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = White100
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                StatPill(value = "$completados", label = "Hechos", color = GreenNeon)
                                StatPill(value = "${total - completados}", label = "Faltan", color = YellowAccent)
                            }
                            Text(
                                "$total hábitos en total",
                                fontSize = 12.sp,
                                color = White70
                            )
                        }
                    }
                }

                // — Grid de métricas —
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🏆",
                        value = "$total",
                        label = "Hábitos\nactivos",
                        accent = CyanAccent
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🔥",
                        value = "$completados",
                        label = "Completados\nhoy",
                        accent = OrangeAccent
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        emoji = "⚡",
                        value = "${total - completados}",
                        label = "Pendientes\nhoy",
                        accent = YellowAccent
                    )
                }

                // — Por categoría —
                if (uiState.habits.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(Black70)
                            .border(1.dp, GlassBorder, RoundedCornerShape(22.dp))
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                "Por categoría",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = White100
                            )
                            val grouped = uiState.habits.groupBy { it.category }
                            grouped.forEach { (cat, habits) ->
                                val catColor = getCategoryColor(cat)
                                val catEmoji = getCategoryEmoji(cat)
                                val catTotal = habits.size
                                val catProgreso = habits.count {
                                    uiState.completedToday.contains(it.id)
                                }.toFloat() / catTotal.toFloat()

                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(catColor.copy(alpha = 0.15f))
                                                .border(
                                                    1.dp,
                                                    catColor.copy(alpha = 0.3f),
                                                    RoundedCornerShape(10.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(catEmoji, fontSize = 16.sp)
                                        }
                                        Text(
                                            cat,
                                            color = White100,
                                            fontSize = 14.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            "${(catProgreso * 100).toInt()}%",
                                            color = catColor,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    // Barra por categoría
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(White40.copy(alpha = 0.1f))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(catProgreso)
                                                .fillMaxHeight()
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(catColor.copy(alpha = 0.5f), catColor)
                                                    )
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // — Frase motivacional —
                Text(
                    "Motivación del día",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = White100
                )

                when {
                    quoteState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Black70)
                                .border(1.dp, GlassBorder, RoundedCornerShape(22.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = GreenNeon,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    quoteState.error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(Black70)
                                .border(1.dp, RedError.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                                .padding(24.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("😕", fontSize = 32.sp)
                                Text(
                                    "No se pudo cargar la frase",
                                    color = RedError,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(GreenDark1.copy(alpha = 0.3f))
                                        .border(1.dp, GreenNeon.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                        .clickable { viewModel.loadQuote() }
                                        .padding(horizontal = 20.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = GreenNeon,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Reintentar",
                                            color = GreenNeon,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        val quote = quoteState.quote
                        if (quote != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                GreenDark2.copy(alpha = 0.5f),
                                                Black70
                                            )
                                        )
                                    )
                                    .border(
                                        1.dp,
                                        GreenNeon.copy(alpha = 0.25f),
                                        RoundedCornerShape(22.dp)
                                    )
                                    .padding(24.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Text("💬", fontSize = 30.sp)
                                    Text(
                                        text = "\"${quote.quote}\"",
                                        color = White100,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(GreenNeon.copy(alpha = 0.1f))
                                            .border(
                                                1.dp,
                                                GreenNeon.copy(alpha = 0.2f),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .padding(horizontal = 14.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            "— ${quote.author}",
                                            color = GreenNeon,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun StatPill(value: String, label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = color
            )
            Text(
                label,
                fontSize = 10.sp,
                color = White70
            )
        }
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    emoji: String,
    value: String,
    label: String,
    accent: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    listOf(accent.copy(alpha = 0.1f), Black70)
                )
            )
            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 22.sp)
            Text(
                value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = accent
            )
            Text(
                label,
                fontSize = 10.sp,
                color = White70,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}