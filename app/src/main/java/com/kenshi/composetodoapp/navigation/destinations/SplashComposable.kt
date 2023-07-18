//package com.kenshi.composetodoapp.navigation.destinations
//
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.slideOutVertically
//import androidx.navigation.NavGraphBuilder
//import com.google.accompanist.navigation.animation.composable
//import com.kenshi.composetodoapp.ui.screens.splash.SplashScreen
//import com.kenshi.composetodoapp.util.Constants.SPLASH_SCREEN
//
//// navigation.kt 에서 호출됨
//@ExperimentalAnimationApi
//fun NavGraphBuilder.splashComposable(
//    navigateToListScreen: () -> Unit,
//) {
//    composable(
//        route = SPLASH_SCREEN,
//        exitTransition = {
//            slideOutVertically(
//                targetOffsetY = { fullHeight -> -fullHeight },
//                animationSpec = tween(durationMillis = 300)
//            )
//        }
//    ) {
//        SplashScreen(navigateToListScreen = navigateToListScreen)
//    }
//}