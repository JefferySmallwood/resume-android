package com.jws.resume.ui.access

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.google.firebase.functions.FirebaseFunctionsException
import com.jws.resume.R
import com.jws.resume.fetchResumeData
import com.jws.resume.model.Resume
import com.jws.resume.ui.theme.ResumeTheme
import kotlinx.coroutines.launch


@Composable
fun AccessScreen(
    modifier: Modifier = Modifier,
    onNavigateToSuccessScreen: (resume: Resume) -> Unit = {},
    onShowErrorMessage: (String) -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    var accessCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // TODO: Move function call to a ViewModel
    fun verifyCode(code: String) {
        isLoading = true
        errorMessage = null
        keyboardController?.hide()

         scope.launch {
             try {
                 val resume = fetchResume(code)
                 if (resume == null) {
                     errorMessage = getString(context, R.string.error_failed_resume_fetch)
                     onShowErrorMessage(errorMessage!!)
                 } else {
                     onNavigateToSuccessScreen(resume)
                 }
             } catch (e: Exception) {
                 errorMessage = handleException(e, context)
                 onShowErrorMessage(errorMessage!!)
             }
             isLoading = false
         }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.please_enter_access_code),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = accessCode,
            onValueChange = {
                if (it.length < 20) {
                    accessCode = it
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
                    if (accessCode.isNotBlank() && !isLoading) {
                        verifyCode(accessCode)
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
                if (accessCode.isNotBlank() && !isLoading) {
                    verifyCode(accessCode)
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) {
                    stringResource(R.string.verifying)
                } else {
                    stringResource(R.string.submit_code)
                }
            )
        }

        content
    }
}

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

// TODO: Move function call to a ViewModel
private suspend fun fetchResume(code: String): Resume? {
    return fetchResumeData(accessCode = code)
}

@Preview(showBackground = true, name = "Access Screen Preview")
@Composable
fun AccessScreenPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AccessScreen(
                onNavigateToSuccessScreen = { println("Preview: Navigate to success!") },
                onShowErrorMessage = { message -> println("Preview Error: $message") }
            )
        }
    }
}
