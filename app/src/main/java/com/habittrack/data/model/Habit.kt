package com.habittrack.data.model

data class Habit(
    val id: Int = 0,
    val name: String,
    val category: String,
    val color: String,
    val frequencyDays: List<Int>,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)