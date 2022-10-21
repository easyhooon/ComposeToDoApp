package com.kenshi.composetodoapp.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

// 확장 함수를 통해 코드 간결화
fun String?.toAction(): Action {
    return if (this.isNullOrEmpty()) Action.NO_ACTION else Action.valueOf(this)
//    return when {
//        this == "ADD" -> {
//            Action.ADD
//        }
//        this == "UPDATE" -> {
//            Action.UPDATE
//        }
//        this == "DELETE" -> {
//            Action.DELETE
//        }
//        this == "DELETE_ALL" -> {
//            Action.DELETE_ALL
//        }
//        this == "UNDO" -> {
//            Action.UNDO
//        }
//        else -> {
//            Action.NO_ACTION
//        }
//    }
}