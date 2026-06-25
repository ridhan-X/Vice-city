package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppColors

@Composable
fun PrivacyScreen() {
    val t = LocalAppColors.current
    val sections = listOf(
        "No Data Collection" to "AyuGuard does not collect, transmit, or store any personal data on external servers. Everything stays on your device.",
        "Location Data" to "GPS only activates when SOS is triggered or Journey Mode is running. Coordinates are stored locally only and never uploaded.",
        "Contacts" to "Your trusted contacts are stored in device-level Preferences storage. Never accessed or transmitted externally.",
        "SMS Dispatch" to "Emergency SMS is sent directly from your device via the native Android SMS system. AyuGuard does not intercept or log messages."
    )

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Last updated: January 2025", fontSize = 13.sp, color = t.text3, modifier = Modifier.padding(bottom = 4.dp))
        
        sections.forEach { (title, body) ->
            Column(modifier = Modifier.fillMaxWidth().background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(18.dp)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = t.text1, modifier = Modifier.padding(bottom = 8.dp))
                Text(body, fontSize = 14.sp, color = t.text2, lineHeight = 22.sp)
            }
        }
    }
}
