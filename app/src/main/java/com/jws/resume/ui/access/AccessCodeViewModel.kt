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
import java.util.Locale.getDefault
import javax.inject.Inject


sealed interface AccessUiState {
    object Idle : AccessUiState
    object Loading : AccessUiState
    data class Success(val resume: Resume) : AccessUiState
    data class Error(val accessCodeError: AccessCodeError) : AccessUiState
}

@HiltViewModel
class AccessCodeViewModel @Inject constructor(
    private val resumeRepository: ResumeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccessUiState>(AccessUiState.Idle)
    val uiState: StateFlow<AccessUiState> = _uiState.asStateFlow()

    private val _downloadedResumes = MutableStateFlow<List<Resume>>(emptyList())
    val downloadedResumes: StateFlow<List<Resume>> = _downloadedResumes.asStateFlow()

    private val _accessCodeInput = MutableStateFlow("")
    val accessCodeInput: StateFlow<String> = _accessCodeInput.asStateFlow()

     private var recentlyDeletedItem: Pair<Int, Resume>? = null


    init {
        loadAllDownloadedResumes()
    }

    fun validateAccessCode() {
        if (_accessCodeInput.value.isBlank()) {
            _uiState.value = AccessUiState.Error(AccessCodeError.EmptyNotAllowed())
            return
        }

        viewModelScope.launch {
            _uiState.value = AccessUiState.Loading
            try {
                val resume = resumeRepository.fetchAndStoreResumeFromApi(_accessCodeInput.value)
                if (resume != null) {
                    resumeRepository.setCurrentResumeId(resume.resume.resumeId)
                    _uiState.value = AccessUiState.Success(resume)
                    loadAllDownloadedResumes()
                } else {
                    _uiState.value = AccessUiState.Error(AccessCodeError.NotFound())
                }
            } catch (e: Exception) {
                _uiState.value = AccessUiState.Error(handleException(e))
            }
        }
    }

    fun setCurrentResumeId(resumeId: String) {
        viewModelScope.launch {
            resumeRepository.setCurrentResumeId(resumeId)
        }
    }

    fun codeChanged(accessCode: String) {
        _accessCodeInput.value = accessCode.uppercase (locale = getDefault()).trim()
        _uiState.value = AccessUiState.Idle
    }

    fun deleteResume(resumeId: String) {
        viewModelScope.launch {
            val currentList = _downloadedResumes.value
            val itemToDelete = currentList.find { it.resume.resumeId == resumeId }
            val itemIndex = currentList.indexOf(itemToDelete)

            if (itemToDelete != null && itemIndex != -1) {
                recentlyDeletedItem = Pair(itemIndex, itemToDelete)
                _downloadedResumes.value = currentList.filterNot { it.resume.resumeId == resumeId }
            }
        }
    }

    fun undoDeleteResume(resumeId: String) {
        viewModelScope.launch {
            recentlyDeletedItem?.let { (index, item) ->
                if (item.resume.resumeId == resumeId) {
                    val currentList = _downloadedResumes.value.toMutableList()
                    if (index >= 0 && index <= currentList.size) {
                        currentList.add(index, item)
                        _downloadedResumes.value = currentList
                    } else {
                        _downloadedResumes.value = currentList + item
                        recentlyDeletedItem = null
                    }
                }
            }
        }
    }

    fun confirmDeleteResume(resumeId: String) {
        viewModelScope.launch {
            resumeRepository.deleteResumeById(resumeId)
            recentlyDeletedItem = null
            resumeRepository.getCurrentResumeId().collect { currentResumeId ->
                if (currentResumeId == resumeId) {
                    resumeRepository.clearCurrentResumeId()
                }
            }
        }
    }


    private fun loadAllDownloadedResumes() {
        viewModelScope.launch {
            resumeRepository.getAllResumes().collect { resumes ->
                _downloadedResumes.value = resumes
                if (resumes.isEmpty()) {
                    fetchSampleResume()
                }
            }
        }
    }

    private suspend fun fetchSampleResume() {
        val sampleUserAccessCode = "SAMPLEUSER1"
        resumeRepository.fetchAndStoreResumeFromApi(accessCode = sampleUserAccessCode)
    }

    fun resetUiStateToIdle() {
        _uiState.value = AccessUiState.Idle
    }
}
