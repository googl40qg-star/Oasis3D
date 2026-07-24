package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Avatar3DState
import com.example.model.Translations
import com.example.ui.components.`3d`.Canvas3DRenderer
import com.example.ui.theme.NeonBackground
import com.example.ui.theme.NeonOnSurface
import com.example.ui.theme.NeonOutline
import com.example.ui.theme.NeonPrimary
import com.example.ui.theme.NeonSecondary
import com.example.ui.theme.NeonSurface
import com.example.ui.theme.NeonTertiary
import kotlin.math.roundToInt

@Composable
fun World3DScreen(
    avatars: List<Avatar3DState>,
    language: AppLanguage,
    isMicMuted: Boolean,
    voiceVolumeLevel: Float,
    onMoveAvatar: (deltaX: Float, deltaY: Float) -> Unit,
    onToggleMic: () -> Unit,
    onSendMessage: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var chatText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Touch Joystick State
    var joystickOffsetX by remember { mutableStateOf(0f) }
    var joystickOffsetY by remember { mutableStateOf(0f) }

    // Pulsating animation for voice mic
    val infiniteTransition = rememberInfiniteTransition(label = "mic")
    val micPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeonBackground)
            .onKeyEvent { keyEvent ->
                // Computer Keyboard WASD & Arrow Keys Support
                when (keyEvent.key) {
                    Key.DirectionLeft, Key.A -> {
                        onMoveAvatar(-1f, 0f)
                        true
                    }
                    Key.DirectionRight, Key.D -> {
                        onMoveAvatar(1f, 0f)
                        true
                    }
                    Key.DirectionUp, Key.W -> {
                        onMoveAvatar(0f, -1f)
                        true
                    }
                    Key.DirectionDown, Key.S -> {
                        onMoveAvatar(0f, 1f)
                        true
                    }
                    else -> false
                }
            }
    ) {
        // 1. Interactive 3D World Canvas
        Canvas3DRenderer(
            avatars = avatars,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Room Overlay Info & Control Hints
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(NeonSurface.copy(alpha = 0.65f))
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "📍 " + Translations.get("move_hint", language),
                fontSize = 11.sp,
                color = NeonSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }

        // 3. Bottom HUD Overlay: Quick Emote Reactions, Joystick, Mic Toggle, Chat Box
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 76.dp), // Safe space above bottom bar
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row A: Quick Emote Reactions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickEmoteChip(
                    text = Translations.get("hello", language),
                    color = NeonPrimary,
                    onClick = { onSendMessage(Translations.get("hello", language), true) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickEmoteChip(
                    text = Translations.get("lfg", language),
                    color = NeonSecondary,
                    onClick = { onSendMessage(Translations.get("lfg", language), true) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                QuickEmoteChip(
                    text = Translations.get("nice", language),
                    color = NeonTertiary,
                    onClick = { onSendMessage(Translations.get("nice", language), true) }
                )
            }

            // Row B: Joystick (Left) and Mic/Chat controls (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // On-screen Joystick
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(2.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    joystickOffsetX = 0f
                                    joystickOffsetY = 0f
                                },
                                onDragCancel = {
                                    joystickOffsetX = 0f
                                    joystickOffsetY = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val maxDist = 35f
                                    val newX = (joystickOffsetX + dragAmount.x).coerceIn(-maxDist, maxDist)
                                    val newY = (joystickOffsetY + dragAmount.y).coerceIn(-maxDist, maxDist)
                                    joystickOffsetX = newX
                                    joystickOffsetY = newY

                                    // Trigger movement in 3D world
                                    onMoveAvatar(newX / maxDist, newY / maxDist)
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(joystickOffsetX.roundToInt(), joystickOffsetY.roundToInt()) }
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(NeonPrimary, NeonSecondary))
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    )
                }

                // Voice Mic & Forum Chat toggle actions
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    // Voice Mic Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                if (!isMicMuted) NeonSecondary.copy(alpha = 0.25f) else NeonSurface.copy(alpha = 0.7f)
                            )
                            .border(
                                width = 2.dp,
                                color = if (!isMicMuted) NeonSecondary else Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .clickable { onToggleMic() }
                    ) {
                        if (!isMicMuted) {
                            Box(
                                modifier = Modifier
                                    .size((52 + (voiceVolumeLevel * 20)).dp)
                                    .clip(CircleShape)
                                    .background(NeonSecondary.copy(alpha = micPulseAlpha))
                            )
                        }
                        Icon(
                            imageVector = if (!isMicMuted) Icons.Default.Mic else Icons.Default.MicOff,
                            contentDescription = "Voice Chat",
                            tint = if (!isMicMuted) NeonSecondary else NeonOutline,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(NeonSurface.copy(alpha = 0.7f))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = "Chat",
                            tint = NeonPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            // Row C: Text Chat Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(NeonSurface.copy(alpha = 0.85f))
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = chatText,
                    onValueChange = { chatText = it },
                    placeholder = {
                        Text(
                            text = Translations.get("type_message", language),
                            fontSize = 13.sp,
                            color = NeonOutline
                        )
                    },
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
                            if (chatText.isNotBlank()) {
                                onSendMessage(chatText, false)
                                chatText = ""
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        if (chatText.isNotBlank()) {
                            onSendMessage(chatText, false)
                            chatText = ""
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
}

@Composable
private fun QuickEmoteChip(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(NeonSurface.copy(alpha = 0.75f))
            .border(1.dp, color.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
