package com.kenshi.composetodoapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF141414)

val LowPriorityColor = Color(0xFF00C980)
val MediumPriorityColor = Color(0xFFFFC114)
val HighPriorityColor = Color(0xFFFF4646)
val NonePriority = MediumGray

//val Colors.splashScreenBackgroud: Color
//    //@Composable
//    get() = if(isLight) Purple700 else Color.Black

// 이런식으로 attr을 대체할 수 있는건가
val Colors.taskItemTextColor: Color
    //@Composable
    get() = if (isLight) Color.DarkGray else LightGray

val Colors.taskItemBackgroundColor: Color
    //@Composable
    get() = if (isLight) Color.White else DarkGray

val Colors.fabBackgroundColor: Color
    //custom getter
    //@Composable
    get() = if (isLight) Teal200 else Purple700

val Colors.topAppBarContentColor: Color
    //custom getter
    //@Composable
    get() = if (isLight) Color.White else LightGray

val Colors.topAppBarBackgroundColor: Color
    //custom getter
    //@Composable
    get() = if (isLight) Purple500 else Color.Black