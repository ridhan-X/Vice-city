package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostButton
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@Composable
fun JourneyActiveScreen(
    durationMin: Int,
    vehicle: String,
    plate: String,
    timeLeft: Int,
    onStop: () -> Unit,
    onSOS: () -> Unit
) {
    val t = LocalAppColors.current
    val urgent = timeLeft < 300 // last 5 min
    val accentColor = if (urgent) t.red else t.text1
    val circleColor = if (urgent) t.red else t.green
    
    val h = timeLeft / 3600
    val m = (timeLeft % 3600) / 60
    val s = timeLeft % 60
    val timeStr = if (h > 0) "$h:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}" else "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        
        Box(modifier = Modifier.size(210.dp).padding(bottom = 24.dp), contentAlignment = Alignment.Center) {
            // Circle placeholder for simplicity
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(timeStr, fontSize = 38.sp, fontWeight = FontWeight.Black, color = accentColor, letterSpacing = (-1).sp)
                Text("REMAINING", fontSize = 11.sp, color = t.text3, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, modifier = Modifier.padding(top = 6.dp))
            }
        }

        if (vehicle.isNotEmpty()) {
            Row(
                modifier = Modifier.background(t.card, RoundedCornerShape(12.dp)).border(1.dp, t.border, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 8.dp).padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CarRental, contentDescription = null, tint = t.text2, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (plate.isNotEmpty()) "$vehicle · $plate" else vehicle, fontSize = 14.sp, color = t.text2)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().background(t.green.copy(alpha = 0.05f), RoundedCornerShape(20.dp)).border(1.dp, t.green.copy(alpha = 0.2f), RoundedCornerShape(20.dp)).padding(horizontal = 16.dp, vertical = 12.dp).padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(t.green))
            Spacer(Modifier.width(10.dp))
            Text("GPS caching active · Countdown in notification bar", fontSize = 12.sp, color = t.text2)
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(text = "🚨 Trigger SOS Now", onClick = onSOS)
            GhostButton(text = "✓ I've Arrived Safely", onClick = onStop)
        }
    }
}
