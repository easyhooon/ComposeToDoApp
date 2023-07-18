package com.kenshi.composetodoapp.util

object Constants {

    // database
    const val DATABASE_TABLE = "todo_table"
    const val DATABASE_NAME = "todo_database"

    // navigation

    // without argument
    // const val SPLASH_SCREEN = "values/splash"
    // with argument
    const val LIST_SCREEN= "list/{action}"
    const val TASK_SCREEN = "task/{taskId}"

    const val LIST_ARGUMENT_KEY = "action"
    const val TASK_ARGUMENT_KEY = "taskId"

    // preference datastore
    const val PREFERENCE_NAME = "todo_preferences"
    const val PREFERENCE_KEY = "sort_state"

    const val MAX_TITLE_LENGTH = 20
    // const val SPLASH_SCREEN_DELAY = 3000L

}