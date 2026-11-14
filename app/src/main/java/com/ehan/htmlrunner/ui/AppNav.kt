package com.ehan.htmlrunner.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ehan.htmlrunner.MainViewModel

@Composable
fun AppNav() {
    val vm: MainViewModel = viewModel()
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(vm = vm, onOpen = { id -> nav.navigate("run/$id") })
        }
        composable(
            route = "run/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            RunnerScreen(vm = vm, projectId = id, onBack = { nav.popBackStack() })
        }
    }
}
