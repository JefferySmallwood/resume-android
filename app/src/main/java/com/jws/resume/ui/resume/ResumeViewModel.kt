package com.jws.resume.ui.resume
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jws.resume.data.repos.ResumeRepository
import com.jws.resume.model.Resume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ResumeUiState {
    object Idle : ResumeUiState
    object Loading : ResumeUiState
    data class Success(val resume: Resume) : ResumeUiState
    data class Error(val message: String) : ResumeUiState
}

@HiltViewModel
open class ResumeViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository?,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val RESUME_ID_ARG_KEY = "resumeId"
    }

    private val _uiState = MutableStateFlow<ResumeUiState>(ResumeUiState.Idle)
    open val uiState: StateFlow<ResumeUiState> = _uiState.asStateFlow()

    private val _currentResume = MutableStateFlow<Resume?>(null)
    open val currentResume: StateFlow<Resume?> = _currentResume.asStateFlow()

    private val resumeId: String? = savedStateHandle.get<String>(RESUME_ID_ARG_KEY)

    init {
        resumeId?.let { id ->
            if (id.isNotBlank()) {
                loadResumeDetails(id)
            } else {
                _uiState.value = ResumeUiState.Error("Invalid Resume ID")
            }
        } ?: run {
            _uiState.value = ResumeUiState.Error("Resume ID not found")
        }
    }

    open fun loadResumeDetails(resumeId: String) {
        viewModelScope.launch {
            _uiState.value = ResumeUiState.Loading

            try {
                resumeRepository?.getResumeById(resumeId)?.collect { resume ->
                    if (resume != null) {
                        _uiState.value = ResumeUiState.Success(resume)
                        _currentResume.value = resume
                    } else {
                        _uiState.value = ResumeUiState.Error("Resume details not found.")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ResumeUiState.Error("Error loading resume details: ${e.message}")
            }
        }
    }
}
