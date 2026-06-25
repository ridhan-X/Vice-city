package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Contact
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    contacts: List<Contact>,
    onAddContact: (String, String) -> Unit,
    onDeleteContact: (Contact) -> Unit
) {
    val t = LocalAppColors.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp)) {
        Text(
            text = "Emergency SMS goes to these contacts. Stored only on your device — survives app updates.",
            color = t.text2,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Only \"Clear Storage\" removes them — not Clear Cache.",
            color = t.text3,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 16.dp)) {
            contacts.forEach { c ->
                Row(
                    modifier = Modifier.fillMaxWidth().background(t.card, RoundedCornerShape(20.dp)).padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(42.dp).clip(RoundedCornerShape(21.dp)).background(t.red.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(c.name.take(1).uppercase(), color = t.red, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(c.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = t.text1)
                        Text(c.phone, fontSize = 13.sp, color = t.text2)
                    }
                    IconButton(onClick = { onDeleteContact(c) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = t.text3)
                    }
                }
            }
        }

        if (contacts.size < 5) {
            Column(modifier = Modifier.fillMaxWidth().background(t.card, RoundedCornerShape(20.dp)).padding(16.dp)) {
                Text("ADD CONTACT (${contacts.size}/5)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = t.text3, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 14.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Name", color = t.text3) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
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
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("Phone Number", color = t.text3) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
                PrimaryButton(text = "+ Add Contact", onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onAddContact(name.trim(), phone.trim())
                        name = ""
                        phone = ""
                    }
                })
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth().background(t.card, RoundedCornerShape(20.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Maximum 5 contacts reached", color = t.text3, fontSize = 14.sp)
            }
        }
    }
}
