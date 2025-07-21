package com.jws.resume.ui.access

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.R
import com.jws.resume.model.Resume
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun DownloadedResumes(
    downloadedResumes: List<Resume>,
    resumeSelected: (String) -> Unit,
    onResumeDeleted: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(32.dp))
    Text(
        text = stringResource(R.string.downloaded_resumes),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(16.dp))
    downloadedResumes.forEach { entry ->
        ResumeRow(
            entry = entry,
            resumeSelected = resumeSelected,
            onResumeDeleted = onResumeDeleted
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, name = "Downloaded Resumes Preview")
@Composable
fun DownloadedResumesPreview() {
    val downloadedResumes = listOf(mockResumeData)
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    DownloadedResumes(
                        downloadedResumes = downloadedResumes,
                        resumeSelected = {},
                        onResumeDeleted = {}
                    )
                }
            }
        }
    }
}