package com.habittrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.habittrack.ui.screens.HabitDetailScreen
import com.habittrack.ui.screens.HabitFormScreen
import com.habittrack.ui.screens.HabitListScreen
import com.habittrack.ui.screens.StatsScreen
import com.habittrack.viewmodel.HabitViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: HabitViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "habits/list"
    ) {
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