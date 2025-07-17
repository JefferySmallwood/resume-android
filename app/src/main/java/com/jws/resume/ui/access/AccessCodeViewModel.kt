package com.jws.resume.ui.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jws.resume.data.repos.ResumeRepository
import com.jws.resume.model.Resume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AccessUiState {
    object Idle : AccessUiState
    object Loading : AccessUiState
    data class Success(val resume: Resume) : AccessUiState
    data class Error(val message: String) : AccessUiState
}

@HiltViewModel
class AccessCodeViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccessUiState>(AccessUiState.Idle)
    val uiState: StateFlow<AccessUiState> = _uiState.asStateFlow()

    private val _downloadedResumes = MutableStateFlow<List<Resume>>(emptyList())
    val downloadedResumes: StateFlow<List<Resume>> = _downloadedResumes.asStateFlow()

    init {
        loadAllDownloadedResumes()
    }

    // TODO: Convert strings to string resources
    fun fetchResumeByAccessCode(accessCode: String) {
        if (accessCode.isBlank()) {
            _uiState.value = AccessUiState.Error("Access code cannot be empty.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AccessUiState.Loading
            try {
                val resume = resumeRepository.fetchAndStoreResumeFromApi(accessCode)
                if (resume != null) {
                    _uiState.value = AccessUiState.Success(resume)
                    loadAllDownloadedResumes()
                } else {
                    _uiState.value =
                        AccessUiState.Error("Resume not found for the given access code.")
                }
            } catch (e: IOException) {
                _uiState.value =
                    AccessUiState.Error("Network error: ${e.message ?: "Unknown network error"}")
            } catch (e: Exception) {
                _uiState.value =
                    AccessUiState.Error("An error occurred: ${e.message ?: "Unknown error"}")
            }
        }
    }

    fun setCurrentResumeId(resumeId: String) {
        viewModelScope.launch {
            resumeRepository.setCurrentResumeId(resumeId)
        }
    }

    private fun loadAllDownloadedResumes() {
        viewModelScope.launch {
            resumeRepository.getAllResumes().collect { resumes ->
                _downloadedResumes.value = resumes
            }
        }
    }

    fun resetUiStateToIdle() {
        _uiState.value = AccessUiState.Idle
    }
}
