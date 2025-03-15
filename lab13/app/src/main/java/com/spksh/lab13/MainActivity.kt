package com.spksh.lab13

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VideoPlayer()
                }
            }
        }
    }
}

@Composable
fun VideoPlayer() {
    val context = LocalContext.current
    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.video}")

    val mediaController = MediaController(context).apply {
        setAnchorView(this)
    }

    DisposableEffect(Unit) {
        val videoView = VideoView(context).apply {
            setMediaController(mediaController)
            setVideoURI(videoUri)
            start()
        }

        onDispose {
            videoView.stopPlayback()
        }
    }

    Box {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setMediaController(mediaController)
                    setVideoURI(videoUri)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { videoView ->
                videoView.start()
            }
        )
        Text("Грачёв Григорий Алексеевич БПИ236", modifier = Modifier.align(Alignment.BottomCenter))
    }

}