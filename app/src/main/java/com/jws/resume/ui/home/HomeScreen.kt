package com.jws.resume.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jws.resume.R
import com.jws.resume.data.entities.HomeInfo
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.common.EmailAddress
import com.jws.resume.ui.common.PhoneNumber
import com.jws.resume.ui.common.TextIcon
import com.jws.resume.ui.common.WebLink
import com.jws.resume.ui.theme.DarkColorScheme
import com.jws.resume.ui.theme.ResumeTheme

@Composable
private fun ResizableAsyncImage(homeInfo: HomeInfo) {
    val context = LocalContext.current
    var imageHeight by remember { mutableStateOf<Int?>(null) }
    val imageRequestBuilder = ImageRequest.Builder(context)
        .data(homeInfo.profilePictureUrl)
        .placeholder(R.drawable.profile_loading)
        .error(R.drawable.profile_error)
        .crossfade(true)
        .listener(
            onSuccess = { _, result ->
                val height = result.drawable.intrinsicHeight
                if (height > 0) {
                    imageHeight = height
                }
            },
            onError = { _, _ ->
                imageHeight = null
            }
        )
        .build()
    AsyncImage(
        model = imageRequestBuilder,
        contentDescription = stringResource(R.string.profile_picture_of, homeInfo.name),
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (imageHeight != null) {
                    Modifier.height(imageHeight!!.dp)
                } else {
                    Modifier
                }
            ),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun HomeScreen(
    homeInfo: HomeInfo,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val iconTintOnDarkImage = DarkColorScheme.onSurface

    Box(modifier = modifier.fillMaxWidth()) {
        ResizableAsyncImage(homeInfo)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color.Transparent,
                        0.55f to Color.Transparent,
                        0.65f to Color.Black.copy(alpha = 0.7f),
                        1.0f to Color.Black.copy(alpha = 0.8f)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Text(
                text = homeInfo.name,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 2.dp),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = homeInfo.tagline,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 0.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                TextIcon(
                    text = homeInfo.location,
                    modifier = Modifier.weight(1f),
                    startIcon = Icons.Filled.Place,
                    iconTint = iconTintOnDarkImage
                )
                TextIcon(
                    text = stringResource(R.string.experience_years, homeInfo.yearsExperience),
                    modifier = Modifier.weight(1f),
                    startIcon = Icons.Filled.Star,
                    iconTint = iconTintOnDarkImage
                )
            }
            EmailAddress(
                emailAddress = homeInfo.email,
                iconTint = iconTintOnDarkImage
            )
            PhoneNumber(
                phoneNumber = homeInfo.phoneNumber,
                iconTint = iconTintOnDarkImage
            )
            WebLink(
                userText = stringResource(R.string.linkedin),
                url = homeInfo.linkedInUrl,
                iconTint = iconTintOnDarkImage
            )
            WebLink(
                userText = stringResource(R.string.github),
                url = homeInfo.githubUrl,
                iconTint = iconTintOnDarkImage
            )
            content
        }
    }
}

@Preview(showBackground = true, name = "HomeScreen Preview")
@Composable
fun HomeScreenPreview() {
    ResumeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(homeInfo = mockResumeData.resume.homeInfo)
        }
    }
}
