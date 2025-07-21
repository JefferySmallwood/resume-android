package com.jws.resume.ui.access

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.R
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun AccessCodeEntry(
    accessCodeInput: String,
    uiState: AccessUiState = AccessUiState.Idle,
    onValueChange: (String) -> Unit = {},
    validateAccessCode: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(
        text = stringResource(R.string.please_enter_access_code),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(24.dp))
    OutlinedTextField(
        value = accessCodeInput,
        onValueChange = {
            onValueChange(it)
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
                    validateAccessCode()
                    keyboardController?.hide()
                }
            }
        ),
        isError = uiState is AccessUiState.Error,
        modifier = Modifier.fillMaxWidth()
    )

    if (uiState is AccessUiState.Error) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (uiState.accessCodeError.formatArgs == null) {
                stringResource(uiState.accessCodeError.messageResId)
            } else {
                stringResource(uiState.accessCodeError.messageResId, uiState.accessCodeError.formatArgs)
            },
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = {
            if (accessCodeInput.isNotBlank() && uiState is AccessUiState.Idle) {
                validateAccessCode()
                keyboardController?.hide()
            }
        },
        enabled = uiState is AccessUiState.Idle && accessCodeInput.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = when (uiState) {
                is AccessUiState.Loading -> stringResource(R.string.verifying)
                is AccessUiState.Success -> stringResource(R.string.success)
                else -> stringResource(R.string.submit_code)
            }
        )
    }
}

@Preview(showBackground = true, name = "Access Code Entry (Idle) Preview")
@Composable
fun AccessCodeEntryIdlePreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AccessCodeEntry(
                        accessCodeInput = "Code 123",
                        uiState = AccessUiState.Idle,
                        onValueChange = {},
                    ) { }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Access Code Entry (Loading) Preview")
@Composable
fun AccessCodeEntryLoadingPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AccessCodeEntry(
                        accessCodeInput = "Code 123",
                        uiState = AccessUiState.Loading,
                        onValueChange = {},
                    ) { }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Access Code Entry (Error) Preview")
@Composable
fun AccessCodeEntryErrorPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AccessCodeEntry(
                        accessCodeInput = "Code 123",
                        uiState = AccessUiState.Error(AccessCodeError.UnknownError()),
                        onValueChange = {},
                    ) { }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Access Code Entry (Success) Preview")
@Composable
fun AccessCodeEntrySuccessPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    AccessCodeEntry(
                        accessCodeInput = "Code 123",
                        uiState = AccessUiState.Success(resume = mockResumeData),
                        onValueChange = {},
                    ) { }
                }
            }
        }
    }
}
