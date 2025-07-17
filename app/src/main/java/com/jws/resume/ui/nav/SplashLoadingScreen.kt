package com.jws.resume.ui.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun SplashLoadingScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val currentResumeId by viewModel.currentResumeId.collectAsState()

    LaunchedEffect(currentResumeId) {
        if (currentResumeId != null) {
            navController.navigate("resume/${currentResumeId!!}") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("access_code") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}