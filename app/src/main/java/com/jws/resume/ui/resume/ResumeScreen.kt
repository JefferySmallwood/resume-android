package com.jws.resume.ui.resume

import SectionHeader
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.jws.resume.R
import com.jws.resume.model.Resume
import com.jws.resume.model.mockResumeData
import com.jws.resume.ui.education.EducationSectionContent
import com.jws.resume.ui.experience.ExperienceSectionContent
import com.jws.resume.ui.fab.ResumeFabMenu
import com.jws.resume.ui.home.HomeScreen
import com.jws.resume.ui.references.ReferencesSectionContent
import com.jws.resume.ui.skills.SkillsSectionContent
import com.jws.resume.ui.theme.ResumeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun ResumeScreen(
    modifier: Modifier = Modifier,
    resumeViewModel: ResumeViewModel = hiltViewModel(),
    navController: NavHostController? = null
) {
    val uiState by resumeViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val currentWindowInsets = LocalView.current.rootWindowInsets
    val statusBarScrimColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    val scrimColor = Color.Black.copy(alpha = 0.6f)

    val sectionKeys = mapOf(
        "Home" to 0,
        "Experience" to 1,
        "Skills" to 2,
        "Education" to 3,
        "References" to 4
    )

    var fabExpanded by remember { mutableStateOf(false) }
    val isHomeScreenAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val statusBarHeightPx = remember(currentWindowInsets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            currentWindowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
        } else{
            with(density) { 56.dp.toPx().toInt() }
            // TODO: Replace hardcoded value with dynamic value. Code below is currently not working
//            val statusBarPaddingValues = WindowInsets.statusBars.asPaddingValues()
//            val statusBarHeightPx = remember(statusBarPaddingValues, density) {
//                with(density) { statusBarPaddingValues.calculateTopPadding().toPx().toInt() }
//            }
        }
    }
    val collapseFab = { action: () -> Unit ->
        {
            action()
            fabExpanded = false
        }
    }

    BackHandler(enabled = fabExpanded) { fabExpanded = false }

    Scaffold(
        floatingActionButton = {
            ResumeFabMenu(
                isExpanded = fabExpanded,
                onToggle = { fabExpanded = !fabExpanded },
                onHomeClick = collapseFab { coroutineScope.launch { listState.animateScrollToItem(index = sectionKeys["Home"]!!) } },
                onExperienceClick = collapseFab { coroutineScope.launch { listState.animateScrollToItem(index = sectionKeys["Experience"]!!, scrollOffset = -statusBarHeightPx) } },
                onSkillsClick = collapseFab { coroutineScope.launch { listState.animateScrollToItem(index = sectionKeys["Skills"]!!, scrollOffset = -statusBarHeightPx) } },
                onEducationClick = collapseFab { coroutineScope.launch { listState.animateScrollToItem(index = sectionKeys["Education"]!!, scrollOffset = -statusBarHeightPx) } },
                onReferencesClick = collapseFab { coroutineScope.launch { listState.animateScrollToItem(index = sectionKeys["References"]!!, scrollOffset = -statusBarHeightPx) } },
                onChangeResumesClick = collapseFab { navController?.navigate(route = "access_code") }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                state = listState,
                contentPadding = PaddingValues(
                    start = paddingValues.calculateLeftPadding(LocalLayoutDirection.current),
                    end = paddingValues.calculateRightPadding(LocalLayoutDirection.current),
                )
            ) {
                // --- Home Section ---
                item(key = "Home") {
                    HomeScreen(
                        modifier = Modifier
                            .height(screenHeight)
                            .fillMaxWidth(),
                        uiState = uiState
                    )
                }

                // --- Experience Section ---
                item(key = "Experience") {
                    if (uiState is ResumeUiState.Success) {
                        SectionHeader(title = stringResource(R.string.experience))
                        ExperienceSectionContent(experience = (uiState as ResumeUiState.Success).resume.experiences)
                    }
                }

                // --- Skills Section ---
                item(key = "Skills") {
                    if (uiState is ResumeUiState.Success) {
                        SectionHeader(title = stringResource(R.string.skills))
                        SkillsSectionContent(skills = (uiState as ResumeUiState.Success).resume.skills)
                    }
                }

                // --- Education Section ---
                item(key = "Education") {
                    if (uiState is ResumeUiState.Success) {
                        SectionHeader(title = stringResource(R.string.education))
                        EducationSectionContent(education = (uiState as ResumeUiState.Success).resume.educationEntries)
                    }
                }

                // --- References Section ---
                item(key = "References") {
                    if (uiState is ResumeUiState.Success) {
                        SectionHeader(title = stringResource(R.string.references))
                        ReferencesSectionContent(references = (uiState as ResumeUiState.Success).resume.references)
                    }
                }

                item { Spacer(Modifier.height(16.dp + paddingValues.calculateBottomPadding())) }
            }

            AnimatedVisibility(
                visible = !isHomeScreenAtTop,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .background(statusBarScrimColor)
                )
            }

            AnimatedVisibility(
                visible = fabExpanded,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(scrimColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { fabExpanded = false }
                        )
                )
            }
        }
    }
}

private class FakeResumeViewModel(state: ResumeUiState): ResumeViewModel(
    resumeRepository = null,
    savedStateHandle = SavedStateHandle()
) {
    override val uiState: StateFlow<ResumeUiState> = MutableStateFlow(state)
    override val currentResume: StateFlow<Resume?> = MutableStateFlow(mockResumeData)
    override fun loadResumeDetails(resumeId: String) { }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Resume (Success) Preview")
@Composable
fun ResumeSuccessPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeScreen(
                resumeViewModel = FakeResumeViewModel(ResumeUiState.Success(resume = mockResumeData)),
                navController = NavHostController(LocalContext.current),
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Resume (Loading) Preview")
@Composable
fun ResumeLoadingPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeScreen(
                resumeViewModel = FakeResumeViewModel(ResumeUiState.Loading),
                navController = NavHostController(LocalContext.current),
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Resume (Error) Preview")
@Composable
fun ResumeErrorPreview() {
    ResumeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ResumeScreen(
                resumeViewModel = FakeResumeViewModel(ResumeUiState.Error("")),
                navController = NavHostController(LocalContext.current),
            )
        }
    }
}
