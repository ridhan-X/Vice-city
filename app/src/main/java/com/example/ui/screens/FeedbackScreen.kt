package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onClose: () -> Unit) {
    val t = LocalAppColors.current
    var rating by remember { mutableStateOf(0) }
    var note by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).padding(top = 60.dp, bottom = 40.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("🙏", fontSize = 52.sp, modifier = Modifier.padding(bottom = 18.dp))
            Text("Glad you're safe", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-0.5).sp, color = t.text1, modifier = Modifier.padding(bottom = 10.dp))
            Text("How did AyuGuard perform? Share your feedback — it helps make it better for everyone.", fontSize = 15.sp, color = t.text2, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 28.dp))
            
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 22.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (1..5).forEach { s ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(if (s <= rating) t.red.copy(alpha = 0.12f) else t.card, RoundedCornerShape(14.dp))
                            .border(1.5.dp, if (s <= rating) t.red else t.border, RoundedCornerShape(14.dp))
                            .clickable { rating = s }
                    ) {
                        Text(if (s <= rating) "★" else "☆", fontSize = 22.sp, color = if (s <= rating) t.red else t.text3, modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                    }
                }
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("Any bugs or suggestions? (optional)", color = t.text3) },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = t.red,
                    unfocusedBorderColor = t.border,
                    focusedContainerColor = t.inputBg,
                    unfocusedContainerColor = t.inputBg,
                    focusedTextColor = t.text1,
                    unfocusedTextColor = t.text1
                )
            )
            Text("Tapping \"Share Feedback\" will open GitHub Discussions where you can post your experience.", fontSize = 12.sp, color = t.text3, lineHeight = 18.sp, modifier = Modifier.padding(top = 10.dp))
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryButton(text = "Share Feedback on GitHub", onClick = onClose)
            TextButton(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
                Text("Skip", color = t.text3, fontSize = 14.sp)
            }
        }
    }
}
