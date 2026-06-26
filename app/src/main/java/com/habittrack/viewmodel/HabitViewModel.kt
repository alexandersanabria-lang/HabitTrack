package com.habittrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittrack.data.local.HabitEntity
import com.habittrack.data.local.HabitLogEntity
import com.habittrack.data.remote.QuoteDto
import com.habittrack.domain.repository.FirestoreRepository
import com.habittrack.domain.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HabitUiState(
    val habits: List<HabitEntity> = emptyList(),
    val completedToday: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class QuoteUiState(
    val quote: QuoteDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    private val _quoteState = MutableStateFlow(QuoteUiState())
    val quoteState: StateFlow<QuoteUiState> = _quoteState.asStateFlow()

    private val firestoreRepository = FirestoreRepository()

    init {
        loadHabits()
        loadQuote()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllHabits().collect { habits ->
                val today = java.time.LocalDate.now().toString()
                val logs = repository.getLogsByDate(today)
                val completedIds = logs.filter { it.completed }.map { it.habitId }.toSet()
                _uiState.value = HabitUiState(
                    habits = habits,
                    completedToday = completedIds,
                    isLoading = false
                )
            }
        }
    }

    fun insertHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.insertHabit(habit)
            try { firestoreRepository.syncHabitToCloud(habit) } catch (e: Exception) { }
        }
    }

    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.updateHabit(habit)
            try { firestoreRepository.syncHabitToCloud(habit) } catch (e: Exception) { }
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
            try { firestoreRepository.deleteHabitFromCloud(habit.id) } catch (e: Exception) { }
        }
    }

    fun insertLog(log: HabitLogEntity) {
        viewModelScope.launch {
            repository.insertLog(log)
        }
        loadHabits()
    }

    fun loadQuote() {
        viewModelScope.launch {
            _quoteState.value = QuoteUiState(isLoading = true)
            val result = repository.getRandomQuote()
            result.fold(
                onSuccess = { quote ->
                    _quoteState.value = QuoteUiState(quote = quote)
                },
                onFailure = { error ->
                    _quoteState.value = QuoteUiState(error = error.message)
                }
            )
        }
    }
}