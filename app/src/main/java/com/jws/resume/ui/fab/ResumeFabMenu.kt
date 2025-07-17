package com.jws.resume.ui.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.jws.resume.R
import com.jws.resume.ui.theme.ResumeTheme

private data class FabMenuItem(
    val label: String,
    val icon: ImageVector,
    val onClickAction: () -> Unit
)

@Composable
fun ResumeFabMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onToggle: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onExperienceClick: () -> Unit = {},
    onSkillsClick: () -> Unit = {},
    onEducationClick: () -> Unit = {},
    onReferencesClick: () -> Unit = {}
) {
    val workIcon = ImageVector.vectorResource(id = R.drawable.baseline_work_history_24)
    val skillsIcon = ImageVector.vectorResource(id = R.drawable.baseline_psychology_24)
    val schoolIcon = ImageVector.vectorResource(id = R.drawable.baseline_school_24)
    val groupIcon = ImageVector.vectorResource(id = R.drawable.baseline_groups_24)

    val fabItems = remember {
        listOf(
            FabMenuItem(label = "Home", Icons.Default.Home, onClickAction = onHomeClick),
            FabMenuItem(label = "Experience", workIcon, onClickAction = onExperienceClick),
            FabMenuItem(label = "Skills", skillsIcon, onClickAction = onSkillsClick),
            FabMenuItem(label = "Education", schoolIcon, onClickAction = onEducationClick),
            FabMenuItem(label = "References", groupIcon, onClickAction = onReferencesClick)
        )
    }

    val containerColor = MaterialTheme.colorScheme.secondaryContainer
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(durationMillis = 150)) + expandVertically(
                expandFrom = Alignment.Bottom,
                animationSpec = tween(durationMillis = 300)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 150)) + shrinkVertically(
                shrinkTowards = Alignment.Bottom,
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            SubcomposeLayout { constraints ->
                var maxItemWidth = 0
                subcompose(slotId = "itemsToMeasure") {
                    fabItems.forEach { item ->
                        ExtendedFloatingActionButton(
                            onClick = {},
                            text = { Text(text = item.label) },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                            containerColor = containerColor,
                            contentColor = contentColor,
                        )
                    }
                }.map { measurable ->
                    val placeable = measurable.measure(Constraints())
                    maxItemWidth = maxOf(maxItemWidth, placeable.width)
                    placeable
                }

                val finalItemPlaceables = subcompose(slotId = "itemsToLayout") {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fabItems.forEach { item ->
                            FloatingActionButton(
                                onClick = item.onClickAction,
                                modifier = Modifier.width(with(LocalDensity.current) { maxItemWidth.toDp() }),
                                shape = FloatingActionButtonDefaults.extendedFabShape,
                                containerColor = containerColor,
                                contentColor = contentColor,
                                elevation = FloatingActionButtonDefaults.elevation()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(text = item.label)
                                }
                            }
                        }
                    }
                }.map { it.measure(constraints) }

                val totalHeight = finalItemPlaceables.sumOf { it.height }
                val totalWidth = finalItemPlaceables.maxOfOrNull { it.width } ?: 0

                layout(totalWidth, totalHeight) {
                    finalItemPlaceables.forEach { it.placeRelative(x = 0, y = 0) }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        FloatingActionButton(
            onClick = onToggle,
            containerColor = containerColor,
            contentColor = contentColor,
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = if (isExpanded) {
                    stringResource(R.string.close_menu)
                } else {
                    stringResource(R.string.open_menu)
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Resume Fab Menu Preview")
@Composable
fun ResumeFabMenuPreview() {
    ResumeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                ResumeFabMenu (
                    isExpanded = true,
                    onToggle = {},
                    onHomeClick = { println("Home Clicked") },
                    onExperienceClick = { println("Experience Clicked") },
                    onSkillsClick = { println("Skills Clicked") },
                    onEducationClick = { println("Education Clicked") },
                    onReferencesClick = { println("References Clicked") }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Text(text = "Screen Content Behind", Modifier.align(Alignment.Center))
            }
        }
    }
}
