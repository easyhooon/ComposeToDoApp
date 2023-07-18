package com.kenshi.composetodoapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.kenshi.composetodoapp.navigation.destinations.listComposable
import com.kenshi.composetodoapp.navigation.destinations.taskComposable
import com.kenshi.composetodoapp.ui.viewmodels.SharedViewModel
import com.kenshi.composetodoapp.util.Constants.LIST_SCREEN

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetUpNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    // this variable will just keep track of all our composable functions
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    // define our navigation graph
    // to apply accompanist navigation animation
    //NavHost(
    AnimatedNavHost(
        navController = navController,
        startDestination = LIST_SCREEN
    ) {
//        splashComposable(
//            //how to navigate from splash screen
//            navigateToListScreen = screen.splash
//        )
        listComposable(
            //how to navigate from list screen
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            //how to navigate from task screen
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }
}