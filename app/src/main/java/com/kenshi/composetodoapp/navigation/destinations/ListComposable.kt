package com.kenshi.composetodoapp.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.kenshi.composetodoapp.ui.screens.list.ListScreen
import com.kenshi.composetodoapp.ui.viewmodels.SharedViewModel
import com.kenshi.composetodoapp.util.Action
import com.kenshi.composetodoapp.util.Constants.LIST_ARGUMENT_KEY
import com.kenshi.composetodoapp.util.Constants.LIST_SCREEN
import com.kenshi.composetodoapp.util.toAction

@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        //argument will be action
        //that action will be passed from our test screen whenever we trigger one of those actions
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) { navBackStackEntry ->
        //convert String to Action by extension function
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()
        // Log.d("listComposable: ", action.name)

        // this my action will hold no action as default value, and will be saved across configuration changes
        var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        LaunchedEffect(key1 = myAction) {
            if (action != myAction) {
                myAction = action
                // sharedViewModel.action.value = action
                sharedViewModel.updateAction(newAction = action)
            }
        }

        // val databaseAction by sharedViewModel.action
        val databaseAction = sharedViewModel.action

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}