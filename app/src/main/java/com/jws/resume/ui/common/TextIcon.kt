package com.jws.resume.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun TextIcon(
    text: String,
    modifier: Modifier = Modifier,
    startIcon: ImageVector? = null,
    startIconDescription: String? = null,
    endIcon: ImageVector? = null,
    endIconDescription: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    content: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.padding(all = 8.dp)
    ) {
        if (startIcon != null) {
            Icon(
                imageVector = startIcon,
                contentDescription = startIconDescription,
                modifier = Modifier.padding(end = 8.dp),
                tint = iconTint
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
        if (endIcon != null) {
            Icon(
                imageVector = endIcon,
                contentDescription = endIconDescription,
                modifier = Modifier.padding(start = 8.dp),
                tint = iconTint
            )
        }
        content
    }
}

@Preview(showBackground = true, name = "Text Icon Preview")
@Composable
fun TextIconPreview() {
    ResumeTheme {
        TextIcon(
            startIcon = Icons.Default.Home,
            text = "Sample Text",
        )
    }
}