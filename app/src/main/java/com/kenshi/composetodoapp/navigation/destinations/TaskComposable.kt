package com.kenshi.composetodoapp.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.kenshi.composetodoapp.ui.screens.task.TaskScreen
import com.kenshi.composetodoapp.ui.viewmodels.SharedViewModel
import com.kenshi.composetodoapp.util.Action
import com.kenshi.composetodoapp.util.Constants.TASK_ARGUMENT_KEY
import com.kenshi.composetodoapp.util.Constants.TASK_SCREEN

@ExperimentalAnimationApi
fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    composable(
        route = TASK_SCREEN,
        // argument will be action
        // that action will be passed from our test screen whenever we trigger one of those actions
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        }
    ) { navBackStackEntry ->
        // 왜 전체 task 객체를 전달 하면 안될까? (taskId 만을 전달해야하는 이유)
        // 공식 문서에 그렇게 하라고 되어 있음 (간단한 id, string 같은 값을 전달해야한다고)
        // 그리고 받는 쪽은 그 id 를 이용 해서 request 하는 식으로
        val taskId = navBackStackEntry.arguments!!.getInt(TASK_ARGUMENT_KEY)

        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId = taskId)
            // Log.d("getSelectedTask", taskId.toString())
        }
        // 값이 변 경되고 그 값이 변경 되면 taskScreen recomposition 수행
        val selectedTask by sharedViewModel.selectedTask.collectAsState()

        // whenever taskId changes and this block inside the LaunchedEffect will be triggered
        //LaunchedEffect(key1 = taskId) {

        // whenever selectedTask is changed call updateTaskFields from sharedViewModel which is actually update all those values
        // which in already have in sharedViewModel
        LaunchedEffect(key1 = selectedTask) {
            // only when the selected tasks changes, then only then we will execute this updateTaskFields function
            // this block of code will execute only when the data is successfully collected from these selectedTask
            //Log.d("updateTaskFields", selectedTask.toString())
            if (selectedTask != null || taskId == -1) {
                sharedViewModel.updateTaskFields(selectedTask = selectedTask)
            }
        }

        TaskScreen(
            selectedTask = selectedTask,
            sharedViewModel = sharedViewModel,
            navigateToListScreen = navigateToListScreen
        )
    }
}