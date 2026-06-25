package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    startSOS: () -> Unit,
    onJourney: () -> Unit,
    onContacts: () -> Unit
) {
    val t = LocalAppColors.current
    var isHolding by remember { mutableStateOf(false) }
    var holdProgress by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(targetValue = 1f + (holdProgress / 4000f) * 0.12f, label = "scale")
    val pct = holdProgress / 4000f
    val secsLeft = Math.ceil((4000 - holdProgress) / 1000.0).toInt()

    LaunchedEffect(isHolding) {
        if (isHolding) {
            var tick = 0
            while (tick < 4000 && isHolding) {
                delay(50)
                tick += 50
                holdProgress = tick
            }
            if (tick >= 4000) {
                startSOS()
            }
            holdProgress = 0
            isHolding = false
        } else {
            holdProgress = 0
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 36.dp, bottom = 12.dp)
                .size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circle progress logic (for simplicity, just a normal button logic)
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(t.red) // Use gradient in future
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isHolding = true
                                tryAwaitRelease()
                                isHolding = false
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SOS", fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color.White)
                    if (holdProgress > 0) {
                        Text("${secsLeft}s", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
        }

        Text(
            text = if (holdProgress > 0) "HOLD TO CONFIRM..." else "HOLD 4 SECONDS TO TRIGGER",
            color = if (holdProgress > 0) t.red else t.text3,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 32.dp)) {
            Row(
                modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = t.green, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("OFFLINE READY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.green)
            }
            Row(
                modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.SignalWifiOff, contentDescription = null, tint = t.text3, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("NO CLOUD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                modifier = Modifier.weight(1f).background(t.card, RoundedCornerShape(18.dp)).border(1.dp, t.border, RoundedCornerShape(18.dp)).clickable { onJourney() }.padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.CarRental, contentDescription = null, tint = t.text2, modifier = Modifier.size(24.dp))
                Spacer(Modifier.height(8.dp))
                Text("Journey Mode", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = t.text2)
            }
            Column(
                modifier = Modifier.weight(1f).background(t.card, RoundedCornerShape(18.dp)).border(1.dp, t.border, RoundedCornerShape(18.dp)).clickable { onContacts() }.padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Contacts, contentDescription = null, tint = t.text2, modifier = Modifier.size(24.dp))
                Spacer(Modifier.height(8.dp))
                Text("Contacts", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = t.text2)
            }
        }
    }
}
