package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppColors
import androidx.compose.foundation.clickable

@Composable
fun AboutScreen(onPrivacy: () -> Unit) {
    val t = LocalAppColors.current
    val checks = listOf("Did SOS work?", "Was message delivered?", "Was location accurate?", "Did Journey Mode work?", "Any bugs?")
    val checkedState = remember { mutableStateMapOf<Int, Boolean>() }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(22.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = t.red, modifier = Modifier.size(30.dp))
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("AyuGuard", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp, color = t.text1)
                    Text("V1 BETA", fontSize = 12.sp, color = t.text2, fontWeight = FontWeight.SemiBold)
                }
            }
            Text("A fast, lightweight, offline-first personal safety shield. Works without internet. No login. No cloud.", color = t.text2, fontSize = 14.sp, lineHeight = 22.sp, modifier = Modifier.padding(bottom = 14.dp))
            Text("100% Privacy Focused — no data collected, tracked, or stored externally.", color = t.text1, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 14.dp))
            TextButton(onClick = onPrivacy, contentPadding = PaddingValues(0.dp)) {
                Text("[ Privacy Policy ]", color = t.blue, fontSize = 14.sp)
            }
        }

        Column(modifier = Modifier.fillMaxWidth().background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("MADE WITH ❤️ BY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 14.dp))
            Text("Ridhan", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = t.text1, modifier = Modifier.padding(bottom = 6.dp))
            Text("Tap to open AyuGuard on GitHub", fontSize = 13.sp, color = t.text2)
        }

        Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(20.dp)) {
            Text("BETA TESTING CHECKLIST", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 16.dp))
            checks.forEachIndexed { i, c ->
                val isChecked = checkedState[i] == true
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { checkedState[i] = !isChecked }.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(22.dp).clip(RoundedCornerShape(6.dp)).background(if (isChecked) t.green else Color.Transparent).border(2.dp, if (isChecked) t.green else t.border, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isChecked) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(c, fontSize = 14.sp, color = if (isChecked) t.text3 else t.text1)
                }
            }
        }
    }
}
