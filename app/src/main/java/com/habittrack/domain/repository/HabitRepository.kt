package com.habittrack.domain.repository

import com.habittrack.data.local.AppDatabase
import com.habittrack.data.local.HabitEntity
import com.habittrack.data.local.HabitLogEntity
import com.habittrack.data.remote.QuoteDto
import com.habittrack.data.remote.ZenQuotesApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabitRepository(private val database: AppDatabase) {

    private val habitDao = database.habitDao()

    private val apiService: ZenQuotesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenQuotesApiService::class.java)
    }

    // ── Room ──────────────────────────────────────────────────────

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)

    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)

    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)

    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    suspend fun getHabitById(id: Int): HabitEntity? = habitDao.getHabitById(id)

    suspend fun insertLog(log: HabitLogEntity) = habitDao.insertLog(log)

    fun getLogsForHabit(habitId: Int): Flow<List<HabitLogEntity>> =
        habitDao.getLogsForHabit(habitId)

    // ── API ───────────────────────────────────────────────────────

    suspend fun getRandomQuote(): Result<QuoteDto> {
        return try {
            val quotes = apiService.getRandomQuote()
            if (quotes.isNotEmpty()) Result.success(quotes[0])
            else Result.failure(Exception("No quotes found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}