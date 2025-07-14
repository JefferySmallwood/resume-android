package com.jws.resume.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
fun PhoneNumber(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val noPhoneClientMessage = stringResource(R.string.no_application_found_to_make_calls)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                openPhoneDialer(
                    context,
                    phoneNumber,
                    noPhoneClientMessage
                )
            }
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Phone,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
            tint = iconTint
        )
        Text(
            text = phoneNumber,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge
        )
        content
    }
}

@Preview(showBackground = true, name = "Phone Number Preview")
@Composable
fun PhoneNumberPreview() {
    ResumeTheme {
        PhoneNumber(phoneNumber = "+1 (555) 123-4567")
    }
}

fun openPhoneDialer(
    context: Context,
    phoneNumber: String,
    noPhoneClientMessage: String = ""
) {
    val encodedPhoneNumber = Uri.encode(phoneNumber)
    val dialIntent = Intent(Intent.ACTION_DIAL, "tel:$encodedPhoneNumber".toUri())

    if (dialIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(dialIntent)
    } else {
        ToastHelper.showUniqueToast(context, text = noPhoneClientMessage)
    }
}