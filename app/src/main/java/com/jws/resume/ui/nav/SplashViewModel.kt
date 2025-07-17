package com.jws.resume.ui.nav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jws.resume.data.repos.ResumeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository
) : ViewModel() {

    private val _currentResumeId = MutableStateFlow<String?>(null)
    val currentResumeId: StateFlow<String?> = _currentResumeId.asStateFlow()

    init {
        getCurrentResumeId()
    }

    fun getCurrentResumeId() {
        viewModelScope.launch {
            resumeRepository.getCurrentResumeId().collect { resumeId ->
                _currentResumeId.value = resumeId
            }
        }
    }
}