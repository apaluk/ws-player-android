package com.apaluk.wsplayer.core.navigation

import com.apaluk.wsplayer.core.navigation.WsPlayerNavArgs.MEDIA_ID_ARG
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.DASHBOARD_SCREEN
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.LOGIN_SCREEN
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.MEDIA_ITEM_SCREEN
import com.apaluk.wsplayer.core.navigation.WsPlayerScreens.VIDEO_PLAYER_SCREEN

object WsPlayerScreens {
    const val LOGIN_SCREEN = "login"
    const val DASHBOARD_SCREEN = "dashboard"
    const val MEDIA_ITEM_SCREEN = "mediaItem"
    const val VIDEO_PLAYER_SCREEN = "videoPlayer"
}

object WsPlayerNavArgs {
    const val MEDIA_ID_ARG = "mediaId"
}

object WsPlayerDestinations {
    const val LOGIN_ROUTE = LOGIN_SCREEN
    const val DASHBOARD_ROUTE = DASHBOARD_SCREEN
    const val VIDEO_PLAYER_ROUTE = "$VIDEO_PLAYER_SCREEN/{$MEDIA_ID_ARG}"
    const val MEDIA_ITEM_ROUTE = "$MEDIA_ITEM_SCREEN/{$MEDIA_ID_ARG}"
}