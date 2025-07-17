package com.jws.resume.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jws.resume.ui.access.AccessCodeScreen
import com.jws.resume.ui.resume.ResumeScreen

@Composable
fun ResumeNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashLoadingScreen(navController = navController) }
        composable("access_code") { AccessCodeScreen(navController = navController) }
        composable(
            route = "resume/{resumeId}",
            arguments = listOf(navArgument("resumeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val resumeId = backStackEntry.arguments?.getString("resumeId")

            if (resumeId == null) {
                navController.navigate("access_code") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            } else {
                ResumeScreen(
                    resumeId = resumeId,
                    navController = navController
                )
            }
        }
    }
}