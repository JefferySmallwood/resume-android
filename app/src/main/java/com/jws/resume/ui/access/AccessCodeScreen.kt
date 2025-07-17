package com.jws.resume.ui.access

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.functions.FirebaseFunctionsException
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun AccessCodeScreen(
    modifier: Modifier = Modifier,
    accessCodeViewModel: AccessCodeViewModel = hiltViewModel(),
    navController: NavHostController,
    content: @Composable () -> Unit = {}
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val uiState by accessCodeViewModel.uiState.collectAsState()
    val downloadedResumes by accessCodeViewModel.downloadedResumes.collectAsState()
    var accessCodeInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = stringResource(R.string.please_enter_access_code),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = accessCodeInput,
                onValueChange = {
                    // TODO: Clear error when new text is entered
                    if (it.length < 20) {
                        accessCodeInput = it
                    }
                    errorMessage = null
                },
                label = { Text(text = stringResource(R.string.enter_access_code)) },
                placeholder = { Text(text = stringResource(R.string.example_access_code)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (accessCodeInput.isNotBlank() && uiState is AccessUiState.Idle) {
                            accessCodeViewModel.fetchResumeByAccessCode(accessCodeInput)
                            keyboardController?.hide()
                        }
                    }
                ),
                isError = errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (accessCodeInput.isNotBlank() && uiState is AccessUiState.Idle) {
                        accessCodeViewModel.fetchResumeByAccessCode(accessCodeInput)
                        keyboardController?.hide()
                    }
                },
                enabled = uiState is AccessUiState.Idle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (uiState is AccessUiState.Loading) {
                        stringResource(R.string.verifying)
                    } else {
                        stringResource(R.string.submit_code)
                    }
                )
            }

             when (val state = uiState) {
                 AccessUiState.Idle -> {}
                 AccessUiState.Loading -> {} // TODO: Implement loading
                 is AccessUiState.Success -> {
                     Text("Success! Resume Name: ${state.resume.resume.homeInfo.name}")
                     accessCodeViewModel.setCurrentResumeId(state.resume.resume.resumeId)
                     navController.navigate("resume/${state.resume.resume.resumeId}")

                 }
                 is AccessUiState.Error -> {
                     Text("Error: ${state.message}", color = Color.Red)
                 }
             }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Resumes",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            downloadedResumes.forEach { entry ->
                Text(
                    text = entry.resume.homeInfo.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            accessCodeViewModel.setCurrentResumeId(entry.resume.resumeId)
                            navController.navigate("resume/${entry.resume.resumeId}")
                        }
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        item {
            content
        }
    }
}

// TODO: Reimplement Error handling
private fun handleException(e: Exception, context: Context): String {
    return when (e) {
        is FirebaseFunctionsException -> {
            when (e.code) {
                FirebaseFunctionsException.Code.INVALID_ARGUMENT ->
                    getString(context, R.string.error_invalid_request)
                FirebaseFunctionsException.Code.NOT_FOUND ->
                    getString(context, R.string.error_invalid_or_not_found)
                FirebaseFunctionsException.Code.PERMISSION_DENIED ->
                    getString(context, R.string.error_access_denied)
                FirebaseFunctionsException.Code.INTERNAL ->
                    getString(context, R.string.error_server)
                else -> context.getString(R.string.error_unknown_server, e.code)
            }
        }

        else -> {
            if (e.message?.contains(other = getString(context, R.string.error_type_invalid_access_code), ignoreCase = true) == true ||
                e.message?.contains(other = getString(context, R.string.error_type_access_code_is_required), ignoreCase = true) == true ||
                e.message?.contains(other = getString(context, R.string.error_type_access_code_is_inactive), ignoreCase = true) == true ||
                e.message?.contains(other = getString(context, R.string.error_type_access_code_has_no_uses_remaining), ignoreCase = true) == true ||
                e.message?.contains(other = getString(context, R.string.error_type_access_code_has_expired), ignoreCase = true) == true ||
                e.message?.contains(other = getString(context, R.string.error_type_resume_data_not_found_for_this_user), ignoreCase = true) == true
            ) {
                getString(context, R.string.error_invalid_access_or_permission)
            } else {
                getString(context, R.string.error_unexpected)
            }
        }
    }
}

@Preview(showBackground = true, name = "Access Code Screen Preview")
@Composable
fun AccessCodeScreenPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AccessCodeScreen(navController = NavHostController(LocalContext.current))
        }
    }
}
