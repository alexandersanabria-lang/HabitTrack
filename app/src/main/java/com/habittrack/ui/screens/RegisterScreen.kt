package com.habittrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.habittrack.ui.theme.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onGoToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

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
                        colors = listOf(GreenDark1.copy(alpha = 0.3f), Color.Transparent),
                        radius = 700f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🌱", fontSize = 52.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Crear cuenta",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                style = MaterialTheme.typography.displaySmall.copy(
                    brush = Brush.horizontalGradient(listOf(GreenNeon, GreenPrimary))
                )
            )
            Text(
                "Únete y empieza a mejorar",
                fontSize = 14.sp,
                color = White70,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Black70)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        "Regístrate",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = White100
                    )

                    // Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nombre", color = White70) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GreenNeon)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenNeon,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = White100,
                            unfocusedTextColor = White100,
                            cursorColor = GreenNeon,
                            focusedContainerColor = Black80,
                            unfocusedContainerColor = Black80
                        )
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Correo electrónico", color = White70) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = GreenNeon)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenNeon,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = White100,
                            unfocusedTextColor = White100,
                            cursorColor = GreenNeon,
                            focusedContainerColor = Black80,
                            unfocusedContainerColor = Black80
                        )
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Contraseña", color = White70) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = GreenNeon)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenNeon,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = White100,
                            unfocusedTextColor = White100,
                            cursorColor = GreenNeon,
                            focusedContainerColor = Black80,
                            unfocusedContainerColor = Black80
                        )
                    )

                    // Confirmar password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Confirmar contraseña", color = White70) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = GreenNeon)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (confirmPassword.isNotEmpty() && confirmPassword != password)
                                RedError else GreenNeon,
                            unfocusedBorderColor = if (confirmPassword.isNotEmpty() && confirmPassword != password)
                                RedError else GlassBorder,
                            focusedTextColor = White100,
                            unfocusedTextColor = White100,
                            cursorColor = GreenNeon,
                            focusedContainerColor = Black80,
                            unfocusedContainerColor = Black80
                        )
                    )

                    if (confirmPassword.isNotEmpty() && confirmPassword != password) {
                        Text(
                            "Las contraseñas no coinciden",
                            color = RedError,
                            fontSize = 11.sp
                        )
                    }

                    if (errorMsg.isNotEmpty()) {
                        Text(
                            errorMsg,
                            color = RedError,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    val formValid = name.isNotBlank() && email.isNotBlank() &&
                            password.isNotBlank() && password == confirmPassword

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (formValid)
                                    Brush.horizontalGradient(listOf(GreenDark1, GreenNeon))
                                else
                                    Brush.horizontalGradient(listOf(Black70, Black70))
                            )
                            .border(
                                1.dp,
                                if (formValid) GreenNeon.copy(alpha = 0.4f) else GlassBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                if (formValid) {
                                    isLoading = true
                                    errorMsg = ""
                                    auth.createUserWithEmailAndPassword(email.trim(), password)
                                        .addOnSuccessListener { result ->
                                            val profileUpdates = userProfileChangeRequest {
                                                displayName = name
                                            }
                                            result.user?.updateProfile(profileUpdates)
                                            isLoading = false
                                            onRegisterSuccess()
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            errorMsg = when {
                                                it.message?.contains("email") == true ->
                                                    "Correo ya registrado o inválido"
                                                it.message?.contains("password") == true ->
                                                    "La contraseña debe tener al menos 6 caracteres"
                                                else -> "Error al registrar. Intenta de nuevo"
                                            }
                                        }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Black100,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Crear cuenta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (formValid) Black100 else White40
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta? ", color = White70, fontSize = 14.sp)
                Text(
                    "Inicia sesión",
                    color = GreenNeon,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}