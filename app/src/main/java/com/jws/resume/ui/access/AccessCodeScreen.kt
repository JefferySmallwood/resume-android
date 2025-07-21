package com.jws.resume.ui.access

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jws.resume.R
import kotlinx.coroutines.launch

@Composable
fun AccessCodeScreen(
    modifier: Modifier = Modifier,
    accessCodeViewModel: AccessCodeViewModel = hiltViewModel(),
    navController: NavHostController,
    content: @Composable () -> Unit = {}
) {
    val uiState by accessCodeViewModel.uiState.collectAsState()
    val downloadedResumes by accessCodeViewModel.downloadedResumes.collectAsState()
    val accessCodeInput by accessCodeViewModel.accessCodeInput.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (uiState is AccessUiState.Success) {
        navController.navigate("resume/${(uiState as AccessUiState.Success).resume.resume.resumeId}")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 24.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                AccessCodeEntry(
                    accessCodeInput = accessCodeInput,
                    uiState = uiState,
                    onValueChange = { accessCode ->
                        accessCodeViewModel.codeChanged(accessCode)
                    },
                ) {
                    accessCodeViewModel.validateAccessCode()
                }
            }

            item {
                val resumeDeletedMessage = stringResource(R.string.resume_deleted)
                val undoLabel = stringResource(R.string.undo)
                DownloadedResumes(
                    downloadedResumes = downloadedResumes,
                    resumeSelected = { resumeId ->
                        accessCodeViewModel.setCurrentResumeId(resumeId)
                        navController.navigate("resume/$resumeId")
                    },
                    onResumeDeleted = { resumeId ->
                        accessCodeViewModel.deleteResume(resumeId)

                        coroutineScope.launch {
                            val snackbarResult = snackbarHostState.showSnackbar(
                                message = resumeDeletedMessage,
                                actionLabel = undoLabel,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                            when (snackbarResult) {
                                SnackbarResult.ActionPerformed -> {
                                    accessCodeViewModel.undoDeleteResume(resumeId)
                                }
                                SnackbarResult.Dismissed -> {
                                    accessCodeViewModel.confirmDeleteResume(resumeId)
                                }
                            }
                        }
                    }
                )
            }
            item {
                content
            }
        }
    }
}
