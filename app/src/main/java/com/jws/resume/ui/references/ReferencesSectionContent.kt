package com.jws.resume.ui.references

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.data.entities.Reference
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun ReferencesSectionContent(
    references: List<Reference>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        references.forEach { reference ->
            ReferenceCard(reference)
            Spacer(Modifier.height(12.dp))
        }
        content
    }
}

@Preview(showBackground = true, name = "Reference Section Content Preview")
@Composable
fun ReferenceSectionContentPreview() {
    ResumeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ReferencesSectionContent(references = mockResumeData.references)
        }
    }
}