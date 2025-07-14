package com.jws.resume.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun EmailAddress(
    emailAddress: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val emailSubject = stringResource(R.string.email_subject)
    val noEmailClientMessage = stringResource(R.string.no_email_application_found)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                openEmailClient(
                    context,
                    emailAddress,
                    subject = emailSubject,
                    noEmailClientMessage = noEmailClientMessage
                )
            }
            .padding(all = 8.dp),

        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = iconTint
        )
        Text(
            text = emailAddress,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge
        )
        content
    }
}

@Preview(showBackground = true, name = "Email Address Preview")
@Composable
fun EmailAddressPreview(
    emailAddress: String = "davidjosiahbrewer@myownpersonaldomain.com"
) {
    ResumeTheme {
        EmailAddress(emailAddress)
    }
}

fun openEmailClient(
    context: Context,
    emailAddress: String,
    subject: String = "",
    noEmailClientMessage: String = ""
) {
    val encodedEmail = Uri.encode(emailAddress)
    val encodedSubject = Uri.encode(subject)

    val mailtoUri = "mailto:$encodedEmail?subject=$encodedSubject".toUri()
    val emailIntent = Intent(Intent.ACTION_SENDTO, mailtoUri)


    if (emailIntent.resolveActivity(context.packageManager) != null) {
        Log.d("EmailClient", "Activity resolved. Starting activity...")
        context.startActivity(emailIntent)
    } else {
        ToastHelper.showUniqueToast(context, text = noEmailClientMessage)
    }
}