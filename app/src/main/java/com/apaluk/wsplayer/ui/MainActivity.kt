package com.apaluk.wsplayer.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.apaluk.wsplayer.core.navigation.WsPlayerNavGraph
import com.apaluk.wsplayer.core.util.AndroidVersion
import com.apaluk.wsplayer.ui.common.composable.LockScreenOrientation
import com.apaluk.wsplayer.ui.theme.WsPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            WsPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WsPlayerNavGraph()
                }
            }
        }
    }

    fun hideSystemUI() {
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (AndroidVersion.isR()) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
