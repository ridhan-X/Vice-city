package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@Composable
fun TutorialScreen(onDone: () -> Unit) {
    val t = LocalAppColors.current
    var step by remember { mutableStateOf(0) }
    
    val steps = listOf(
        Triple("🛡️", "Your Personal Safety Shield", "AyuGuard works 100% offline. No cloud, no login, no tracking. Everything stays on your device."),
        Triple("🆘", "Hold 4 Seconds for SOS", "Press and hold the SOS button for 4 seconds to trigger an emergency alert to your trusted contacts."),
        Triple("📍", "Smart Location — Battery Friendly", "GPS only activates when SOS is triggered. During Journey Mode it caches every 15 min. In basements, your last known location is sent automatically."),
        Triple("🚗", "Journey Mode", "Set a timer before any trip. If you don't check in on time, your contacts get auto-alerted with your location.")
    )
    val s = steps[step]
    val isLast = step == steps.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(t.bg)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = s.first, fontSize = 72.sp, modifier = Modifier.padding(bottom = 28.dp))
            Text(
                text = s.second,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = t.text1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 14.dp)
            )
            Text(
                text = s.third,
                fontSize = 15.sp,
                color = t.text2,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            steps.indices.forEach { i ->
                Box(
                    modifier = Modifier
                        .size(width = if (i == step) 24.dp else 8.dp, height = 8.dp)
                        .background(if (i == step) t.red else t.border, RoundedCornerShape(4.dp))
                )
            }
        }

        PrimaryButton(
            text = if (isLast) "Get Started" else "Next →",
            onClick = { if (isLast) onDone() else step++ }
        )

        if (!isLast) {
            TextButton(
                onClick = onDone,
                modifier = Modifier.padding(top = 14.dp).fillMaxWidth()
            ) {
                Text("Skip", color = t.text3, fontSize = 14.sp)
            }
        }
    }
}
