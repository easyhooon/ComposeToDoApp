//package com.kenshi.composetodoapp.ui.screens.splash
//
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.kenshi.composetodoapp.R
//import com.kenshi.composetodoapp.ui.theme.LOGO_HEIGHT
//import com.kenshi.composetodoapp.ui.theme.splashScreenBackgroud
//import com.kenshi.composetodoapp.util.Constants.SPLASH_SCREEN_DELAY
//import kotlinx.coroutines.delay
//
//@Composable
//fun SplashScreen(
//    navigateToListScreen: () -> Unit
//) {
//    var startAnimation by remember { mutableStateOf(false) }
//    val offsetState by animateDpAsState(
//        // from 100 to zero
//        targetValue = if (startAnimation) 0.dp else 100.dp,
//        animationSpec = tween(
//            durationMillis = 1000
//        )
//    )
//    val alphaState by animateFloatAsState(
//        // from zero to one
//        targetValue = if (startAnimation) 1f else 0f,
//        animationSpec = tween(
//            durationMillis = 1000
//        )
//    )
//
//    LaunchedEffect(key1 = true) {
//        startAnimation = true
//        delay(SPLASH_SCREEN_DELAY)
//        navigateToListScreen()
//    }
//
//    Splash(offsetState = offsetState, alphaState = alphaState)
//}
//
//@Composable
//fun Splash(
//    offsetState: Dp,
//    alphaState: Float
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colors.splashScreenBackgroud),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            modifier = Modifier
//                .size(LOGO_HEIGHT)
//                //animation
//                //y -> vertical axis
//                .offset(y = offsetState)
//                .alpha(alpha = alphaState),
//            painter = painterResource(id = getLogo()),
//            contentDescription = stringResource(R.string.to_do_logo)
//        )
//    }
//}
//
//@Composable
////theme 에 따라 로고 선택해서 넣어주기
//fun getLogo(): Int {
//    return if (isSystemInDarkTheme()) {
//        R.drawable.ic_logo_dark
//    } else {
//        R.drawable.ic_logo_light
//    }
//}
//
//@Composable
//@Preview
//private fun SplashScreenPreview() {
//    Splash(offsetState = 0.dp, alphaState = 1f)
//}
//
//// dark theme preview 보는법
//// logo dark theme 아닌 이유 -> getLogo 함수를 사용하지 않기때문
////@Composable
////@Preview
////private fun SplashScreenPreview2() {
////    ComposeToDoAppTheme(darkTheme = true) {
////        SplashScreen()
////    }
////}