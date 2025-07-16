package com.jws.resume.ui.skills

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jws.resume.data.entities.Skill
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.common.Chip
import com.jws.resume.ui.theme.ResumeTheme

@Composable
fun SkillsSectionContent(
    skills: List<Skill>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        val groupedSkills = skills.groupBy { it.category }
        groupedSkills.forEach { (category, skillsInCategory) ->
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                skillsInCategory.forEach { skill ->
                    Chip(skill.name)
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        content
    }
}

@Preview(showBackground = true, name = "Skills Section Content Preview")
@Composable
fun SkillsSectionContentPreview() {
    ResumeTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            SkillsSectionContent(skills = mockResumeData.skills)
        }
    }
}