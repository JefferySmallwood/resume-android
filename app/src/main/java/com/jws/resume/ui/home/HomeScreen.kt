package com.jws.resume.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.jws.resume.R
import com.jws.resume.data.entities.HomeInfo
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.common.ContactItem
import com.jws.resume.ui.common.IconInfo
import com.jws.resume.ui.common.TextIcon
import com.jws.resume.ui.resume.ResumeUiState
import com.jws.resume.ui.theme.DarkColorScheme
import com.jws.resume.ui.theme.ResumeTheme
import com.jws.resume.util.ContactType
import com.jws.resume.util.getDefaultImageRequestBuilder

@Composable
private fun ResizableAsyncImage(homeInfo: HomeInfo) {
    val context = LocalContext.current
    var imageHeight by remember { mutableStateOf<Int?>(null) }
    val imageRequestBuilder = remember(homeInfo.profilePictureUrl) {
        getDefaultImageRequestBuilder(context, homeInfo.profilePictureUrl)
            .listener(
                onSuccess = { request, result ->
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
    }
    AsyncImage(
        model = imageRequestBuilder,
        contentDescription = stringResource(R.string.profile_picture_of, homeInfo.name),
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (imageHeight != null && imageHeight!! > 0) {
                    Modifier.height(imageHeight!!.dp)
                } else {
                    Modifier.fillMaxSize()
                }
            ),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: ResumeUiState = ResumeUiState.Idle,
    content: @Composable () -> Unit = {}
) {

    Crossfade(
        targetState = uiState is ResumeUiState.Loading,
        modifier = modifier,
        animationSpec = tween(durationMillis = 500),
        label = "Crossfade"
    ) { currentlyLoading ->
        if (currentlyLoading) {
            if (uiState is ResumeUiState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            if (uiState is ResumeUiState.Success) {
                val homeInfo: HomeInfo = uiState.resume.resume.homeInfo
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
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                    ) {
                        Text(
                            text = homeInfo.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = homeInfo.tagline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)) {
                            val textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            TextIcon(
                                text = homeInfo.location,
                                modifier = Modifier.weight(1f),
                                start = IconInfo(iconRes = R.drawable.baseline_location_pin_24),
                                iconTint = iconTintOnDarkImage,
                                textStyle = textStyle
                            )

                            TextIcon(
                                text = stringResource(R.string.experience_years, homeInfo.yearsExperience),
                                modifier = Modifier.weight(1f),
                                start = IconInfo(iconRes = R.drawable.baseline_star_24),
                                iconTint = iconTintOnDarkImage,
                                textStyle = textStyle
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ContactItem(
                            contactType = ContactType.EMAIL(
                                    emailAddress = homeInfo.email,
                                    subject = stringResource(R.string.email_subject)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            iconTint = iconTintOnDarkImage
                        )
                        ContactItem(
                            contactType = ContactType.PHONE(phoneNumber = homeInfo.phoneNumber),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            iconTint = iconTintOnDarkImage
                        )
                        ContactItem(
                            contactType = ContactType.Web.LINKEDIN(
                                    userText = stringResource(R.string.linkedin),
                                    url = homeInfo.linkedInUrl
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            iconTint = iconTintOnDarkImage
                        )
                        ContactItem(
                            contactType = ContactType.Web.GITHUB(
                                    userText = stringResource(R.string.github),
                                    url = homeInfo.githubUrl
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            iconTint = iconTintOnDarkImage
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        content
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "HomeScreen Preview")
@Composable
fun HomeScreenPreview() {
    ResumeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(uiState = ResumeUiState.Success(mockResumeData))
        }
    }
}

@Preview(showBackground = true, name = "HomeScreen (Loading) Preview")
@Composable
fun HomeScreenLoadingPreview() {
    ResumeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(uiState = ResumeUiState.Loading)
        }
    }
}
