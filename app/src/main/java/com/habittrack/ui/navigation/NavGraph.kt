package com.habittrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.habittrack.ui.screens.HabitDetailScreen
import com.habittrack.ui.screens.HabitFormScreen
import com.habittrack.ui.screens.HabitListScreen
import com.habittrack.ui.screens.LoginScreen
import com.habittrack.ui.screens.RegisterScreen
import com.habittrack.ui.screens.StatsScreen
import com.habittrack.viewmodel.HabitViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: HabitViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "habits/list" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("habits/list") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("habits/list") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("habits/list") {
            HabitListScreen(
                viewModel = viewModel,
                onHabitClick = { habitId ->
                    navController.navigate("habits/$habitId")
                },
                onAddClick = {
                    navController.navigate("habits/form")
                },
                onStatsClick = {
                    navController.navigate("stats")
                },
                onLogout = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "habits/{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.IntType })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId") ?: return@composable
            HabitDetailScreen(
                habitId = habitId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("habits/form?habitId=$habitId") }
            )
        }

        composable(
            route = "habits/form?habitId={habitId}",
            arguments = listOf(navArgument("habitId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId") ?: -1
            HabitFormScreen(
                habitId = if (habitId == -1) null else habitId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("stats") {
            StatsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}