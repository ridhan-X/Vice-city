package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostButton
import com.example.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSOSSetupScreen(
    testPhone: String,
    onPhoneChange: (String) -> Unit,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    val t = LocalAppColors.current
    val canHold = testPhone.replace(" ", "").length >= 10

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Column(modifier = Modifier.background(t.blue.copy(alpha = 0.08f), RoundedCornerShape(20.dp)).border(1.dp, t.blue.copy(alpha = 0.2f), RoundedCornerShape(20.dp)).padding(18.dp)) {
            Text("🧪 Test Mode", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = t.blue, modifier = Modifier.padding(bottom = 6.dp))
            Text("Full SOS flow — countdown, alarm, sound — but sends TEST message only to your number. No real emergency triggered.", fontSize = 13.sp, color = t.text2, lineHeight = 20.sp)
        }

        Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(18.dp)) {
            Text("Your phone number", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = t.text1, modifier = Modifier.padding(bottom = 4.dp))
            Text("Enter your own number so the test SMS arrives on this device — you'll see exactly what your contacts would receive.", fontSize = 13.sp, color = t.text2, lineHeight = 20.sp, modifier = Modifier.padding(bottom = 14.dp))
            OutlinedTextField(
                value = testPhone,
                onValueChange = onPhoneChange,
                placeholder = { Text("+91 98765 43210 (your own number)", color = t.text3) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = t.red,
                    unfocusedBorderColor = t.border,
                    focusedContainerColor = t.inputBg,
                    unfocusedContainerColor = t.inputBg,
                    focusedTextColor = t.text1,
                    unfocusedTextColor = t.text1
                )
            )
            if (testPhone.isNotEmpty() && !canHold) {
                Text("Enter a valid phone number to enable test", color = t.red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            }
        }

        // Simulating the Hold Button
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            GhostButton(text = if (canHold) "Trigger Test SOS" else "Enter number first", onClick = { if (canHold) onStart() })
        }

        GhostButton(text = "← Back to Settings", onClick = onBack)
    }
}
