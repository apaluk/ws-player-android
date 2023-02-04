package com.apaluk.wsplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.DASHBOARD_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.LOGIN_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.MEDIA_DETAIL_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.SEARCH_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.VIDEO_PLAYER_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs.MEDIA_ID_ARG
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs.WEBSHARE_IDENT_ARG
import com.apaluk.wsplayer.core.util.base64Decode
import com.apaluk.wsplayer.ui.dashboard.DashboardScreen
import com.apaluk.wsplayer.ui.login.LoginScreen
import com.apaluk.wsplayer.ui.media_detail.MediaDetailScreen
import com.apaluk.wsplayer.ui.player.PlayerScreen
import com.apaluk.wsplayer.ui.search.SearchScreen

@Composable
fun WsPlayerNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActions: WsPlayerNavActions = remember { WsPlayerNavActions(navController) }
) {
    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE,
        modifier = modifier
    ) {
        composable(
            route = LOGIN_ROUTE
        ) {
            LoginScreen(
                modifier = modifier,
                navActions = navActions
            )
        }
        composable(
            route = DASHBOARD_ROUTE
        ) {
            DashboardScreen(
                modifier = modifier,
                navActions = navActions
            )
        }
        composable(
            route = SEARCH_ROUTE
        ) {
            SearchScreen(
                modifier = modifier,
                navActions = navActions
            )
        }
        composable(
            route = MEDIA_DETAIL_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.StringType}
            )
        ) {
            MediaDetailScreen(
                modifier = modifier,
                navActions = navActions,
            )
        }
        composable(
            route = VIDEO_PLAYER_ROUTE,
            arguments = listOf(
                navArgument(WEBSHARE_IDENT_ARG) { type = NavType.StringType}
            )
        ) {
            PlayerScreen(modifier = modifier)
        }
    }
}