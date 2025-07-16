package com.jws.resume.ui.education

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jws.resume.R
import com.jws.resume.data.entities.EducationEntry
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun EducationCard(
    entry: EducationEntry,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    var imageAspectRatio by remember { mutableStateOf<Float?>(null) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (entry.imageUrl != null) {
            val imageRequestBuilder = ImageRequest.Builder(context)
                .data(entry.imageUrl)
                .placeholder(R.drawable.profile_loading)
                .error(R.drawable.profile_error)
                .crossfade(true)
                .listener(
                    onSuccess = { _, result ->
                        val drawable = result.drawable
                        val width = drawable.intrinsicWidth
                        val height = drawable.intrinsicHeight
                        if (width > 0 && height > 0) {
                            imageAspectRatio = width.toFloat() / height.toFloat()
                        }
                    },
                    onError = { _, _ ->
                        imageAspectRatio = null
                    }
                )
                .build()
            AsyncImage(
                model = imageRequestBuilder,
                contentDescription = stringResource(R.string.picture_of, entry.institution),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (imageAspectRatio != null && imageAspectRatio!! > 0) {
                            Modifier.aspectRatio(imageAspectRatio!!)
                        } else {
                            Modifier
                        }
                    ),
                contentScale = ContentScale.Fit,
            )
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.institution,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.degree,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.yearsAttended,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (entry.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        content
    }
}

@Preview(showBackground = true, name = "Education Card Preview")
@Composable
fun EducationCardPreview() {
    ResumeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
        ) {
            val entry = mockResumeData.educationEntries[0]
            EducationCard(entry)
        }
    }
}