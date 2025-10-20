package com.example.sharkflow.viewmodel

import androidx.lifecycle.*
import com.example.sharkflow.domain.usecase.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            _isDarkTheme.value = getThemeUseCase()
        }
    }

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        setThemeUseCase(newValue)
    }

    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        setThemeUseCase(isDark)
    }
}
