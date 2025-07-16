package com.jws.resume

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.jws.resume.model.Resume
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.ResumeScreen
import com.jws.resume.ui.access.AccessScreen
import com.jws.resume.ui.theme.ResumeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var resumeDataForUI by remember { mutableStateOf<Resume?>(null) }
            ResumeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (resumeDataForUI == null) {
                        AccessScreen(
                            onNavigateToSuccessScreen = { resume ->
                                resumeDataForUI = resume
                            },
                            onShowErrorMessage = { message ->
                                Log.e("MainActivity", "Error: $message")
                            }
                        )
                    } else {
                        ResumeScreen(resume = resumeDataForUI!!)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Main Activity Preview")
@Composable
fun MainActivityPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeScreen(resume = mockResumeData)
        }
    }
}
