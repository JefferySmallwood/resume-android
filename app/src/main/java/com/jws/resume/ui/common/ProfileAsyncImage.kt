package com.jws.resume.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme
import com.jws.resume.util.getDefaultImageRequestBuilder

@Composable
fun ProfileAsyncImage(
    url: String?,
    name: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageRequestBuilder = remember(url) {
        getDefaultImageRequestBuilder(context, url).build()
    }
    AsyncImage(
        model = imageRequestBuilder,
        contentDescription = stringResource(R.string.profile_picture_of, name),
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true, name = "Profile Async Image Preview")
@Composable
fun ProfileAsyncImagePreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.padding(all = 16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            ProfileAsyncImage(
                url = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fprofile-loading.jpg?alt=media&token=cd70ba8d-bf5d-4403-b83d-1e44d8976a40",
                name = "John Doe"
            )
        }
    }
}