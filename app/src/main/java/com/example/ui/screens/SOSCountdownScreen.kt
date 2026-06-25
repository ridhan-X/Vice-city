package com.example.ui.screens

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.ui.components.GhostButton
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@Composable
fun SOSCountdownScreen(
    countdownSec: Int,
    isTest: Boolean,
    onCancel: () -> Unit,
    onAlarm: () -> Unit,
    timeLeft: Int
) {
    val t = LocalAppColors.current
    val accentColor = if (isTest) t.blue else t.red
    
    val context = LocalContext.current

    DisposableEffect(Unit) {
        var mediaPlayer: MediaPlayer? = null
        try {
            val resId = context.resources.getIdentifier("sos_sound", "raw", context.packageName)
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(context, resId)
                if (mediaPlayer != null) {
                    mediaPlayer.isLooping = true // The user wants it to play 2 times total (13s + 13s = 26s, the screen lasts 27s). Looping it will ensure it plays continuously until the countdown is stopped or finished.
                    mediaPlayer.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    val mins = (timeLeft / 60).toString().padStart(2, '0')
    val secs = (timeLeft % 60).toString().padStart(2, '0')

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF080000)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isTest) "🧪 TEST SOS — NO EMERGENCY" else "🚨 SOS ACTIVATED",
            fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp, color = accentColor,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Box(modifier = Modifier.size(230.dp).padding(bottom = 32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$mins:$secs", fontSize = 58.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Until Dispatch", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor, letterSpacing = 1.5.sp)
            }
        }

        Text(
            text = if (isTest) "Test message will be sent to your number" else "Emergency message will be sent",
            fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = if (isTest) Color(0xFF90CAF9) else Color(0xFFFF8A80),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Fetching your location now...",
            fontSize = 14.sp, color = Color(0xFF4A1010),
            modifier = Modifier.padding(bottom = 44.dp)
        )

        PrimaryButton(
            text = if (isTest) "Send Test Now — Skip Countdown" else "Send Now — Skip Countdown",
            onClick = onAlarm,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        GhostButton(
            text = "✕ Cancel — ${if (isTest) "Stop Test" else "I'm Safe"}",
            onClick = onCancel
        )
    }
}
