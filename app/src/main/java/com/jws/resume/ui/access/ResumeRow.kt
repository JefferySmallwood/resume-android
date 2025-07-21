package com.jws.resume.ui.access

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jws.resume.R
import com.jws.resume.model.Resume
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun ResumeRow(
    entry: Resume,
    modifier: Modifier = Modifier,
    resumeSelected: (String) -> Unit,
    onResumeDeleted: (String) -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                resumeSelected(entry.resume.resumeId)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(entry.resume.homeInfo.profilePictureUrl)
                    .placeholder(R.drawable.profile_loading)
                    .error(R.drawable.profile_error)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.profile_picture_of, entry.resume.homeInfo.name),
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp, top = 16.dp, bottom = 16.dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentScale = ContentScale.Crop
            )
            Column {
                Row {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 4.dp),
                        text = entry.resume.homeInfo.name,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    IconButton(onClick = { onResumeDeleted(entry.resume.resumeId) }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.delete_resume_description, entry.resume.homeInfo.name),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    text = entry.resume.homeInfo.tagline,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                )
            }
        }

    }
}

@Preview(showBackground = true, name = "Resume Row Preview")
@Composable
fun ResumeRowPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.padding(all = 16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeRow(
                entry = mockResumeData,
                resumeSelected = { },
                onResumeDeleted = { }
            )
        }
    }
}