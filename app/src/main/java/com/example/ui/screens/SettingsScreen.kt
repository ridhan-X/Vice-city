package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.data.AppSettings
import com.example.ui.theme.LocalAppColors

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onThemeChange: (String) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onToggleAppLock: (Boolean) -> Unit,
    onToggleFingerprint: (Boolean) -> Unit,
    onTestSOS: () -> Unit
) {
    val t = LocalAppColors.current

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        
        // Appearance
        Column {
            Text("APPEARANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(16.dp)) {
                Text("Theme", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = t.text1)
                Text("Choose how AyuGuard looks on your device", fontSize = 13.sp, color = t.text2, modifier = Modifier.padding(bottom = 14.dp))
                Row(modifier = Modifier.fillMaxWidth().background(t.inputBg, RoundedCornerShape(12.dp)).padding(3.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    listOf("dark" to "🌑 AMOLED Dark", "day" to "☀️ Day").forEach { (id, label) ->
                        val isSelected = settings.themeId == id
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) t.card else Color.Transparent)
                                .clickable { onThemeChange(id) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = if (isSelected) t.text1 else t.text2, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }
        }

        // App Lock
        Column {
            Text("APP LOCK", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp))) {
                SettingRow(
                    icon = Icons.Default.Lock,
                    label = "PIN Lock",
                    sub = if (settings.appLockPin.isNotEmpty()) (if (settings.appLockEnabled) "Enabled — tap to change PIN" else "PIN set but disabled — tap to enable") else "Set a PIN to lock the app",
                    right = {
                        if (settings.appLockPin.isNotEmpty()) {
                            Switch(checked = settings.appLockEnabled, onCheckedChange = onToggleAppLock)
                        } else {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = t.text3)
                        }
                    }
                )
                HorizontalDivider(color = t.border)
                SettingRow(
                    icon = Icons.Default.Fingerprint,
                    label = "Fingerprint Unlock",
                    sub = if (settings.fingerprintEnabled) "Enabled — fingerprint unlocks app" else "Fast unlock using biometric — requires PIN as backup",
                    right = {
                        Switch(checked = settings.fingerprintEnabled, onCheckedChange = onToggleFingerprint)
                    }
                )
            }
            Text("⚠️ SOS button always works even when app is locked — hold 4 seconds from lock screen.", fontSize = 12.sp, color = t.text3, modifier = Modifier.padding(top = 8.dp))
        }

        // Emergency Settings
        Column {
            Text("EMERGENCY SETTINGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp))) {
                SettingRow(
                    icon = Icons.Default.MusicNote,
                    label = "Alarm Sound",
                    sub = settings.customAlarmName ?: "AyuGuard Default"
                )
                HorizontalDivider(color = t.border)
                SettingRow(
                    icon = Icons.Default.Vibration,
                    label = "Vibration",
                    sub = "Vibrate during alarm and SOS",
                    right = {
                        Switch(checked = settings.vibrationEnabled, onCheckedChange = onToggleVibration)
                    }
                )
                HorizontalDivider(color = t.border)
                SettingRow(
                    icon = Icons.Default.Timer,
                    label = "Countdown Time",
                    sub = "Time before emergency message is sent",
                    right = { Text("${settings.countdownSec}s ›", color = t.red, fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
                )
                HorizontalDivider(color = t.border)
                SettingRow(
                    icon = Icons.Default.NotificationsActive,
                    label = "Alarm Duration",
                    sub = "How long the alarm plays before dispatch",
                    right = { Text("${settings.alarmDurationSec}s ›", color = t.red, fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
                )
            }
        }

        // Testing
        Column {
            Text("TESTING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp))) {
                SettingRow(
                    icon = Icons.Default.Science,
                    label = "Test SOS",
                    sub = "Run full SOS flow with a test message to your number",
                    onClick = onTestSOS,
                    right = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = t.text3) }
                )
            }
        }

    }
}

@Composable
fun SettingRow(icon: ImageVector, label: String, sub: String? = null, right: @Composable (() -> Unit)? = null, onClick: (() -> Unit)? = null) {
    val t = LocalAppColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 18.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(36.dp).background(t.inputBg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = t.text2, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = t.text1)
            if (sub != null) {
                Text(sub, fontSize = 12.sp, color = t.text3)
            }
        }
        if (right != null) {
            Spacer(Modifier.width(14.dp))
            right()
        }
    }
}
