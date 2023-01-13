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
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.MEDIA_ITEM_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.SEARCH_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerDestinations.VIDEO_PLAYER_ROUTE
import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs.MEDIA_ID_ARG
import com.apaluk.wsplayer.core.util.base64Decode
import com.apaluk.wsplayer.ui.dashboard.DashboardScreen
import com.apaluk.wsplayer.ui.dashboard.MediaItemScreen
import com.apaluk.wsplayer.ui.login.LoginScreen
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
                onSuccessFullLogin = { navActions.navigateToDashboard() }
            )
        }
        composable(
            route = DASHBOARD_ROUTE
        ) {
            DashboardScreen(
                modifier = modifier,
                onSearch = { navActions.navigateToSearch() }
            )
        }
        composable(
            route = SEARCH_ROUTE
        ) {
            SearchScreen(
                modifier = modifier,
                navController = navController   // TODO needs refactor?
            )
        }
        composable(
            route = MEDIA_ITEM_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.StringType}
            )
        ) {
            MediaItemScreen(
                onMediaIdSelected = { navActions.navigateToPlayer(url = it) }
            )
        }
        composable(
            route = VIDEO_PLAYER_ROUTE,
            arguments = listOf(
                navArgument(MEDIA_ID_ARG) { type = NavType.StringType}
            )
        ) { entry ->
            val videoUrl = requireNotNull(entry.arguments?.getString(MEDIA_ID_ARG)).base64Decode()
            PlayerScreen(url = videoUrl)
        }
    }
}