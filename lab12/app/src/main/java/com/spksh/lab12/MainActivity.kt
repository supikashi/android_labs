package com.spksh.lab12

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SoundPlayerScreen()
                }
            }
        }
    }
}

@Composable
fun SoundPlayerScreen() {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            playSound(context, mediaPlayer)
        }) {
            Text(text = "Воспроизвести звук")
        }
        Text("Грачёв Григорий Алексеевич БПИ236", modifier = Modifier.align(Alignment.BottomCenter))
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}

private fun playSound(context: Context, mediaPlayer: MediaPlayer) {
    try {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
        mediaPlayer.apply {
            setDataSource(
                context.resources.openRawResourceFd(R.raw.sound)
            )
            prepare()
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}