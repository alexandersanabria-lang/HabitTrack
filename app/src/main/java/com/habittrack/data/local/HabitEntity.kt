package com.habittrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val color: String,
    val frequencyDays: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)