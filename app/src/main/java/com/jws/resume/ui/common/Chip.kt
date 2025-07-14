package com.jws.resume.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(size = 8.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier.padding(all = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
        content
    }
}

@Preview(showBackground = true, name = "Chip Preview")
@Composable
fun ChipPreview() {
    ResumeTheme {
        Chip(text = "Sample Text")
    }
}