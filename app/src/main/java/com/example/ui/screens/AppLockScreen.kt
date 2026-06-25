package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppLockScreen(
    appLockPin: String,
    fingerprintEnabled: Boolean,
    onUnlock: () -> Unit,
    onUseFingerprint: () -> Unit
) {
    val t = LocalAppColors.current
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    val handleDigit = { d: String ->
        if (pin.length < 6) {
            val next = pin + d
            pin = next
            error = false
            if (next.length == appLockPin.length) {
                if (next == appLockPin) {
                    onUnlock()
                } else {
                    error = true
                    pin = ""
                }
            }
        }
    }

    val dots = List(appLockPin.length) { it < pin.length }

    Column(
        modifier = Modifier.fillMaxSize().background(t.bg).padding(40.dp, 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = t.red, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(10.dp))
            Text("AyuGuard", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = t.text1, letterSpacing = (-0.5).sp)
        }

        Text("Enter PIN to unlock", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = t.text1, modifier = Modifier.padding(bottom = 8.dp))
        if (error) {
            Text("Incorrect PIN — try again", fontSize = 13.sp, color = t.red, modifier = Modifier.padding(bottom = 4.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(bottom = 40.dp, top = if (error) 0.dp else 20.dp)) {
            dots.forEach { filled ->
                Box(
                    modifier = Modifier.size(14.dp).clip(CircleShape)
                        .background(if (filled) t.red else Color.Transparent)
                        .border(2.dp, if (filled) t.red else t.border, CircleShape)
                )
            }
        }

        val pad = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.width(280.dp)) {
            pad.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { k ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(68.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (k.isEmpty()) Color.Transparent else t.card)
                                .border(1.dp, if (k.isEmpty()) Color.Transparent else t.border, RoundedCornerShape(16.dp))
                                .clickable(enabled = k.isNotEmpty()) {
                                    if (k == "⌫") {
                                        if (pin.isNotEmpty()) pin = pin.dropLast(1)
                                    } else {
                                        handleDigit(k)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (k.isNotEmpty()) {
                                Text(k, fontSize = if (k == "⌫") 20.sp else 24.sp, fontWeight = FontWeight.SemiBold, color = if (k == "⌫") t.text2 else t.text1)
                            }
                        }
                    }
                }
            }
        }

        if (fingerprintEnabled) {
            Column(
                modifier = Modifier.padding(top = 28.dp).clickable { onUseFingerprint() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = t.text2, modifier = Modifier.size(28.dp))
                Spacer(Modifier.height(6.dp))
                Text("Use Fingerprint", fontSize = 12.sp, color = t.text3, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
