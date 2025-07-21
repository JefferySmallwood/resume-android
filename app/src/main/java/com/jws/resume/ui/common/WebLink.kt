package com.jws.resume.ui.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme
import com.jws.resume.util.ToastHelper

@Composable
fun WebLink(
    userText: String,
    url: String,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Filled.Share,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    painter: Painter? = null,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val invalidUrl = stringResource(R.string.invalid_url_format)
    val linkError = stringResource(R.string.could_not_open_link)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                openUrlInCustomTab(
                    context,
                    urlString = url,
                    invalidUrl,
                    linkError
                )
            }
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (painter != null) {
            Icon(
                painter,
                contentDescription = stringResource(R.string.open_web_link),
                modifier = Modifier.padding(end = 8.dp).size(width = 24.dp, height = 24.dp),
                tint = iconTint
            )
        } else {
            Icon(
                imageVector = imageVector,
                contentDescription = stringResource(R.string.open_web_link),
                modifier = Modifier.padding(end = 8.dp),
                tint = iconTint
            )
        }

        Text(
            text = userText,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge
        )
        content
    }
}

@Preview(showBackground = true, name = "Email Address Preview")
@Composable
fun WebLinkPreview() {
    ResumeTheme {
        WebLink(modifier = Modifier, userText = "LinkedIn", url = "https://www.linkedin.com")
    }
}

fun openUrlInCustomTab(
    context: Context,
    urlString: String,
    invalidUrl: String,
    linkError: String
) {
    try {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val uri = urlString.toUri()
        if (uri.scheme == null || (!uri.scheme.equals("http") && !uri.scheme.equals("https"))) {
            ToastHelper.showUniqueToast(context, invalidUrl, Toast.LENGTH_SHORT)
            return
        }

        customTabsIntent.launchUrl(context, uri)
    } catch (e: Exception) {
        ToastHelper.showUniqueToast(context, text = linkError, duration = Toast.LENGTH_SHORT)
        Log.e("WebLink", "Error opening Custom Tab", e)
    }
}