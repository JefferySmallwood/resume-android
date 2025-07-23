package com.jws.resume.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun ResIcon(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    @StringRes contentDescription: Int? = null
) {
    return Icon(
        painter = painterResource(id = id),
        contentDescription = if (contentDescription == null) null else stringResource(contentDescription),
        modifier = modifier.size(width = 24.dp, height = 24.dp),
        tint = iconTint
    )
}

@Preview(showBackground = true, name = "Res Icon Preview")
@Composable
fun ResIconPreview() {
    ResumeTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            ResIcon(id = R.drawable.baseline_email_24)
        }
    }
}