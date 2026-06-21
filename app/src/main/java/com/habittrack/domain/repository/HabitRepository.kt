package com.habittrack.domain.repository

import com.habittrack.data.local.AppDatabase
import com.habittrack.data.local.HabitEntity
import com.habittrack.data.local.HabitLogEntity
import com.habittrack.data.remote.QuoteDto
import com.habittrack.data.remote.ZenQuotesApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabitRepository(private val database: AppDatabase) {

    private val habitDao = database.habitDao()

    private val apiService: ZenQuotesApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Android)")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://zenquotes.io/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenQuotesApiService::class.java)
    }

    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)
    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)
    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)
    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()
    suspend fun getHabitById(id: Int): HabitEntity? = habitDao.getHabitById(id)
    suspend fun insertLog(log: HabitLogEntity) = habitDao.insertLog(log)
    fun getLogsForHabit(habitId: Int): Flow<List<HabitLogEntity>> = habitDao.getLogsForHabit(habitId)
    suspend fun getLogsByDate(date: String): List<HabitLogEntity> = habitDao.getLogsByDate(date)
    fun getAllLogs(): Flow<List<HabitLogEntity>> = habitDao.getAllLogs()
    suspend fun deleteLogByDate(habitId: Int, date: String) = habitDao.deleteLogByDate(habitId, date)

    suspend fun toggleHabitCompletion(habitId: Int, date: String, isCurrentlyCompleted: Boolean) {
        if (isCurrentlyCompleted) {
            habitDao.deleteLogByDate(habitId, date)
        } else {
            habitDao.insertLog(
                HabitLogEntity(habitId = habitId, date = date, completed = true)
            )
        }
    }

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