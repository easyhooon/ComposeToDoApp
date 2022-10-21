package com.kenshi.composetodoapp.navigation

import androidx.navigation.NavHostController
import com.kenshi.composetodoapp.util.Action
import com.kenshi.composetodoapp.util.Constants.LIST_SCREEN
import com.kenshi.composetodoapp.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION}") {
            // which want to go to
            // inclusive true backstack 에서 제거
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}