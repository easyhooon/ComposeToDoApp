package com.kenshi.composetodoapp.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.kenshi.composetodoapp.R
import com.kenshi.composetodoapp.data.models.Priority
import com.kenshi.composetodoapp.data.models.ToDoTask
import com.kenshi.composetodoapp.ui.viewmodels.SharedViewModel
import com.kenshi.composetodoapp.util.Action

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    //we don't need to by keyword
//    val title: String by sharedViewModel.title
//    val description: String by sharedViewModel.description
//    val priority: Priority by sharedViewModel.priority
    val title: String = sharedViewModel.title
    val description: String = sharedViewModel.description
    val priority: Priority = sharedViewModel.priority

    //composable function 에서 context 를 가져오는 방법
    val context = LocalContext.current

    // 뒤고 가기 구현
    // BackHandler(onBackPressed = { navigateToListScreen(Action.NO_ACTION) })
    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                }
            )
        },

        //whenever those values change, then our task content will be notified and immediately recomposition
        content = {
            TaskContent(
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(it)
                },
                description = description,
                onDescriptionChange = {
                    //sharedViewModel.description = it
                    sharedViewModel.updateDescription(newDescription = it)
                },
                priority = priority,
                onPrioritySelected = {
                    //sharedViewModel.priority.value = it
                    sharedViewModel.updatePriority(newPriority = it)
                }
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.fields_empty),
        Toast.LENGTH_SHORT
    ).show()
}

// 이 함수와 똑같은 동작을 하는 함수가 제공됨 이름도 같음
//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher? =
//        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//) {
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember {
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher) {
//        backDispatcher?.addCallback(backCallBack)
//
//        onDispose {
//            backCallBack.remove()
//        }
//    }
//}