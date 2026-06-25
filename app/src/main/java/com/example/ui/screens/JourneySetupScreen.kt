package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneySetupScreen(onStart: (Int, String, String) -> Unit) {
    val t = LocalAppColors.current
    var durationType by remember { mutableStateOf("30") }
    var vehicle by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }

    val vehicles = listOf("Auto Rickshaw", "Uber", "Cab", "Eco", "Bus", "Walk")

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        
        Column {
            Text("DURATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp))) {
                listOf("30" to "30 Minutes", "60" to "1 Hour").forEachIndexed { index, (id, label) ->
                    if (index > 0) Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(t.border))
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { durationType = id }.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(22.dp).clip(CircleShape).background(if (durationType == id) t.red else Color.Transparent).border(2.dp, if (durationType == id) t.red else t.borderStrong, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (durationType == id) Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                        }
                        Spacer(Modifier.width(14.dp))
                        Text(label, fontSize = 15.sp, fontWeight = if (durationType == id) FontWeight.SemiBold else FontWeight.Normal, color = t.text1)
                    }
                }
            }
        }

        Column {
            Text("VEHICLE (OPTIONAL)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            Column(modifier = Modifier.background(t.card, RoundedCornerShape(20.dp)).border(1.dp, t.border, RoundedCornerShape(20.dp)).padding(16.dp)) {
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 14.dp)) {
                    vehicles.forEach { v ->
                        val isSelected = vehicle == v
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) t.red else t.inputBg)
                                .border(1.dp, if (isSelected) t.red else t.border, RoundedCornerShape(20.dp))
                                .clickable { vehicle = if (isSelected) "" else v }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(v, color = if (isSelected) Color.White else t.text2, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    placeholder = { Text("Number plate (Will be included in SOS SMS)", color = t.text3) },
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
            }
        }

        Column(modifier = Modifier.background(t.red.copy(alpha = 0.08f), RoundedCornerShape(20.dp)).border(1.dp, t.red.copy(alpha = 0.2f), RoundedCornerShape(20.dp)).padding(16.dp)) {
            Text("HOW JOURNEY MODE WORKS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.red, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 10.dp))
            val lines = listOf("GPS cached every 15 min — minimal battery use", "Basement/indoors: last known location is sent", "Auto-SOS triggers if timer expires without check-in", "Live countdown shown in notification bar")
            lines.forEachIndexed { i, s ->
                Row(modifier = Modifier.padding(bottom = if (i < 3) 8.dp else 0.dp), verticalAlignment = Alignment.Top) {
                    Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(t.red), contentAlignment = Alignment.Center) {
                        Text("${i + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(s, fontSize = 13.sp, color = t.text2, lineHeight = 20.sp)
                }
            }
        }

        PrimaryButton(text = "➤ Start Journey", onClick = { onStart(durationType.toIntOrNull() ?: 30, vehicle, plate) })
    }
}
