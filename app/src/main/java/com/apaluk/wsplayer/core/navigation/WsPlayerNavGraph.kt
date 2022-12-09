package com.apaluk.wsplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apaluk.wsplayer.ui.login.LoginScreen

@Composable
fun WsPlayerNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    NavHost(
        navController = navController,
        startDestination = WsPlayerDestinations.LOGIN_ROUTE,
        modifier = modifier
    ) {
        composable(
            WsPlayerDestinations.LOGIN_ROUTE
        ) {
            LoginScreen(modifier = modifier)
        }
    }
}