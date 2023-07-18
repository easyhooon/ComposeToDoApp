package com.kenshi.composetodoapp.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kenshi.composetodoapp.R
import com.kenshi.composetodoapp.ui.theme.fabBackgroundColor
import com.kenshi.composetodoapp.ui.viewmodels.SharedViewModel
import com.kenshi.composetodoapp.util.Action
import com.kenshi.composetodoapp.util.SearchAppBarState


@ExperimentalMaterialApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    // 앱 실행시 호출
    // whenever the state of variable changes then trigger launched effect block
    // when configuration change, this launchedEffect triggered again
    // this function will triggered in sharedViewModel init block
//    LaunchedEffect(key1 = true) {
//        //Log.d("ListScreen", "LaunchedEffect Triggered!")
//        sharedViewModel.getAllTasks()
//        sharedViewModel.readSortState()
//    }

    //whenever the action changes, them we call handleDa  databaseActions functions(trigger)
    LaunchedEffect(key1 = action) {
        Log.d("ListScreen", "$action triggered")
        sharedViewModel.handleDatabaseActions(action = action)
    }

    // listComposable 로 옮김
    //val action by sharedViewModel.action

    // val allTasks = sharedViewModel.allTasks.collectAsState()
    val allTasks by sharedViewModel.allTasks.collectAsState()
    // by 키워드를 통해 .value 로 값을 꺼낼 필요없이 즉시 상태를 전달할 수 있음(Immediately transfer that state into a list of toDoTask
    // collectAsState() will observe Flow from the composable function

    // 데이터베이스가 변경 될때 마다 notify -> ListContent recompose (parameter 인 allTasks 가 변경되었기 때문)
//    for (task in allTasks) {
//        Log.d("ListScreen", task.title)
//    }

    // observing variable (in sharedViewModel)
    // whenever that variable changes, we are immediately updating our ListContent
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    // observing state value
    // whenever it changes, it will recomposed listAppBar with new value
    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    // lambda 로 넘겨주는 것들은 실제 사용할때 구현해주면 된다.
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        //handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        // onComplete -> reset our action to be NO_ACTION
        onComplete = { sharedViewModel.updateAction(newAction = it) },
        onUndoClicked = { sharedViewModel.updateAction(newAction = it) },
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        //without this scaffoldState parameter, not be able to display snackBar
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
//        content = { padding ->
//            Column(
//                modifier = Modifier
//                    .padding(padding)
//            )
//        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    // trigger recomposition and display snackBar and also call handleDatabaseActions function to actually delete task
                    //sharedViewModel.action.value = action
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            // -1 -> 기존에 존재 하는 task 를 열지 않음을 의미
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    // handleDatabaseActions: () -> Unit,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    // handleDatabaseActions function action lambda will basically trigger
    // this handle database actions function from our shared viewModel
    // bad thing: outside of launchedEffect ->
    // mean : this function will be triggered on each every recomposition inside our composable tree
    // handleDatabaseActions()

    // val scope = rememberCoroutineScope()
    // whenever the action changes, then Launched Effect will be triggered

    //onCompleted(replace handleDatabaseAction) will be called in LaunchedEffect
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            // show snackBar
//            scope.launch {
//                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
//                    message = setMessage(action = action, taskTitle = taskTitle),
//                    actionLabel = setActionLabel(action = action)
//                )
//                undoDeletedTask(
//                    action = action,
//                    snackBarResult = snackBarResult,
//                    onUndoClicked = onUndoClicked
//                )
//            }
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = setMessage(action = action, taskTitle = taskTitle),
                actionLabel = setActionLabel(action = action)
            )
            undoDeletedTask(
                action = action,
                snackBarResult = snackBarResult,
                onUndoClicked = onUndoClicked
            )
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All Tasks Removed."
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}