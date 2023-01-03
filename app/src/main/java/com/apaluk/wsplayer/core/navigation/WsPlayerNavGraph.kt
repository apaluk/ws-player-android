package com.apaluk.wsplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.DASHBOARD_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.MEDIA_ITEM_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.VIDEO_PLAYER_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs.MEDIA_ID_ARG
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.MEDIA_ITEM_SCREEN
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.VIDEO_PLAYER_SCREEN
import com.apaluk.wsplayer.core.util.base64Decode
import com.apaluk.wsplayer.core.util.base64Encode
import com.apaluk.wsplayer.ui.dashboard.DashboardScreen
import com.apaluk.wsplayer.ui.dashboard.MediaItemScreen
import com.apaluk.wsplayer.ui.login.LoginScreen
import com.apaluk.wsplayer.ui.player.PlayerScreen

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
            route = WsPlayerDestinations.LOGIN_ROUTE
        ) {
            LoginScreen(
                modifier = modifier,
                onSuccessFullLogin = { navController.navigate(route = DASHBOARD_ROUTE)}
            )
        }
        composable(
            route = DASHBOARD_ROUTE
        ) {
            DashboardScreen(
                modifier = modifier,
                onMediaIdSelected = { mediaId -> navController.navigate(route = "$MEDIA_ITEM_SCREEN/$mediaId")}
            )
        }
        composable(
            route = MEDIA_ITEM_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.StringType}
            )
        ) {
            MediaItemScreen(
                onMediaIdSelected = { url -> navController.navigate(route = "${VIDEO_PLAYER_SCREEN}/${url.base64Encode()}")}
            )
        }
        composable(
            route = VIDEO_PLAYER_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.StringType}
            )
        ) { entry ->
            PlayerScreen(requireNotNull(entry.arguments?.getString(MEDIA_ID_ARG)).base64Decode())
        }
    }
}