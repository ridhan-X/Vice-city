package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostButton
import com.example.ui.theme.LocalAppColors

@Composable
fun SOSAlarmScreen(
    isTest: Boolean,
    smsSent: Boolean,
    timeLeft: Int,
    onStop: () -> Unit
) {
    val t = LocalAppColors.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF080000)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🚨", fontSize = 80.sp, modifier = Modifier.padding(bottom = 32.dp))

        if (smsSent) {
            Text("Help is on the way", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(bottom = 12.dp))
            Text("Emergency SMS sent to all trusted contacts with your location.", fontSize = 15.sp, color = Color(0xFFFF8A80), textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 16.dp))
            Box(modifier = Modifier.background(Color(0xFF110000), RoundedCornerShape(14.dp)).padding(horizontal = 20.dp, vertical = 12.dp).padding(bottom = 44.dp)) {
                Text("Stay calm · Help is coming · Keep your phone on", fontSize = 12.sp, color = Color(0xFF4A1010), fontWeight = FontWeight.SemiBold)
            }
        } else if (timeLeft == 0) {
            Text("Sending message...", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(bottom = 10.dp))
            Text("Contacting your trusted contacts now.", fontSize = 15.sp, color = Color(0xFFFF8A80), modifier = Modifier.padding(bottom = 44.dp))
        } else {
            Text("Alarm Active", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(bottom = 10.dp))
            Text("Cancel if you're safe — message sending in", fontSize = 15.sp, color = Color(0xFFFF8A80), modifier = Modifier.padding(bottom = 6.dp))
            Text("${timeLeft}s", fontSize = 60.sp, fontWeight = FontWeight.Black, color = t.red, modifier = Modifier.padding(bottom = 44.dp))
        }

        GhostButton(
            text = if (smsSent) "I'm Safe Now" else "✕ Cancel — I'm Safe",
            onClick = onStop
        )
    }
}
