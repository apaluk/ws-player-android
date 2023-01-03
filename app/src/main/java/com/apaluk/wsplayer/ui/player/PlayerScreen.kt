package com.apaluk.wsplayer.ui.player

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import okhttp3.OkHttpClient
import timber.log.Timber

@Composable
fun PlayerScreen(
    url: String,
    viewModel: PlayerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Timber.d("xxx player url=$url")
    VideoPlayer(uri = Uri.parse(url))
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val exoPlayer = remember {

        val defaultDataSourceFactory = OkHttpDataSource.Factory(
            OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build()
        )
            .setUserAgent(Util.getUserAgent(context, "scc"))

        val mediaSourceFactory = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
            .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(10))

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
            }
    }

    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}