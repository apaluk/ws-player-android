package com.apaluk.wsplayer.ui.player

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.apaluk.wsplayer.core.navigation.WsPlayerNavActions
import com.apaluk.wsplayer.ui.common.composable.*
import okhttp3.OkHttpClient

@Composable
fun PlayerScreen(
    navActions: WsPlayerNavActions,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    KeepScreenOn()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        uiState.navigateUpEvent.flow.collect {
            navActions.navigateUp()
        }
    }
    UiStateAnimator(uiState = uiState.uiState) {
        uiState.videoUrl?.let {
            VideoPlayer(
                uri = Uri.parse(it),
                onPlayerScreenAction = viewModel::onPlayerScreenAction
            )
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayer(
    uri: Uri,
    onPlayerScreenAction: (PlayerScreenAction) -> Unit
) {
    val context = LocalContext.current
    var wasPlaying = remember { true }

    FullScreen()

    val exoPlayer = remember {
        val defaultDataSourceFactory = OkHttpDataSource.Factory(
            OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .build()
        ).setUserAgent(Util.getUserAgent(context, "scc"))
        val mediaSourceFactory = ProgressiveMediaSource.Factory(defaultDataSourceFactory)

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(uri))
                playWhenReady = true
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                repeatMode = Player.REPEAT_MODE_OFF
                addListener(object: Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if(playbackState == Player.STATE_ENDED) {
                            onPlayerScreenAction(PlayerScreenAction.VideoEnded)
                        }
                    }
                })
                prepare()
            }
    }
    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
    OnLifecycleEvent { _, event ->
        when(event) {
            Lifecycle.Event.ON_PAUSE -> {
                wasPlaying = exoPlayer.isPlaying
                exoPlayer.pause()
            }
            Lifecycle.Event.ON_RESUME -> {
                if(wasPlaying)
                    exoPlayer.play()
            }
            else -> {}
        }
    }

}