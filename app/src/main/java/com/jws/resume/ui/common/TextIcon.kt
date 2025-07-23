package com.jws.resume.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme

data class IconInfo(
    @field:DrawableRes val iconRes: Int,
    @field:StringRes val contentDescription: Int? = null
)

@Composable
fun TextIcon(
    text: String,
    modifier: Modifier = Modifier,
    start: IconInfo? = null,
    end: IconInfo? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    content: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        start?.let {
            ResIcon(
                id = it.iconRes,
                modifier = Modifier.padding(end = 8.dp),
                iconTint,
                it.contentDescription
            )
        }
        Text(text, style = textStyle)
        end?.let {
            ResIcon(
                id = it.iconRes,
                modifier = Modifier.padding(start = 8.dp),
                iconTint,
                it.contentDescription
            )
        }
        content
    }
}

@Preview(showBackground = true, name = "Text Icon (Start) Preview")
@Composable
fun TextIconStartPreview() {
    ResumeTheme {
            TextIcon(
                text = "Sample Text",
                start = IconInfo(iconRes = R.drawable.baseline_email_24),
                iconTint = MaterialTheme.colorScheme.onSurface
            )
    }
}

@Preview(showBackground = true, name = "Text Icon (End) Preview")
@Composable
fun TextIconEndPreview() {
    ResumeTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TextIcon(
                text = "Sample Text",
                end = IconInfo(iconRes = R.drawable.baseline_email_24),
                iconTint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, name = "Text Icon (Both) Preview")
@Composable
fun TextIconBothPreview() {
    ResumeTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TextIcon(
                text = "Sample Text",
                start = IconInfo(iconRes = R.drawable.baseline_email_24),
                end = IconInfo(iconRes = R.drawable.baseline_email_24),
                iconTint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, name = "Text Icon (None) Preview")
@Composable
fun TextIconPreview() {
    ResumeTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TextIcon(text = "Sample Text")
        }
    }
}
