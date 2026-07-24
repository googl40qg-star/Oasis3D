package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.ChatMessageEntity
import com.example.model.AppLanguage
import com.example.model.Translations
import com.example.ui.theme.NeonBackground
import com.example.ui.theme.NeonOnSurface
import com.example.ui.theme.NeonOutline
import com.example.ui.theme.NeonPrimary
import com.example.ui.theme.NeonPrimaryVariant
import com.example.ui.theme.NeonSecondary
import com.example.ui.theme.NeonSurface
import com.example.ui.theme.NeonSurfaceHigh

@Composable
fun ChatHistoryScreen(
    chatMessages: List<ChatMessageEntity>,
    language: AppLanguage,
    isMicMuted: Boolean,
    voiceVolumeLevel: Float,
    onToggleMic: () -> Unit,
    onSendMessage: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeonBackground)
            .padding(top = 12.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Translations.get("chat", language) + " & " + Translations.get("voice_chat", language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonSecondary
                )
                Text(
                    text = "LOBBY RECEPTORA 3D",
                    fontSize = 11.sp,
                    color = NeonOutline
                )
            }

            // Voice Mic Toggle Widget
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (!isMicMuted) NeonSecondary.copy(alpha = 0.2f) else NeonSurfaceHigh)
                    .border(1.dp, if (!isMicMuted) NeonSecondary else Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                    .clickable { onToggleMic() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (!isMicMuted) Icons.Default.Mic else Icons.Default.MicOff,
                    contentDescription = "Mic",
                    tint = if (!isMicMuted) NeonSecondary else NeonOutline,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (!isMicMuted) Translations.get("mic_on", language) else Translations.get("mic_off", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isMicMuted) NeonSecondary else NeonOutline
                )
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeonSurface.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages) { msg ->
                ChatMessageItem(msg = msg)
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeonSurface)
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(Translations.get("type_message", language), fontSize = 12.sp, color = NeonOutline) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = NeonOnSurface,
                    unfocusedTextColor = NeonOnSurface
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText, false)
                            inputText = ""
                            focusManager.clearFocus()
                        }
                    }
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        onSendMessage(inputText, false)
                        inputText = ""
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(NeonPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatMessageItem(msg: ChatMessageEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NeonSurfaceHigh.copy(alpha = 0.7f))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = msg.senderName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPrimaryVariant
            )

            if (msg.isQuickReaction) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonSecondary.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("REACTION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonSecondary)
                }
            }
        }

        Text(
            text = msg.messageText,
            fontSize = 13.sp,
            color = NeonOnSurface
        )
    }
}
