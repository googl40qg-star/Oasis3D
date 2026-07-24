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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.RoomEntity
import com.example.model.AppLanguage
import com.example.model.Translations
import com.example.ui.theme.NeonBackground
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonOnSurface
import com.example.ui.theme.NeonOutline
import com.example.ui.theme.NeonPrimary
import com.example.ui.theme.NeonPrimaryVariant
import com.example.ui.theme.NeonSecondary
import com.example.ui.theme.NeonSurface
import com.example.ui.theme.NeonSurfaceHigh
import com.example.ui.theme.NeonTertiary

@Composable
fun WorldsListScreen(
    rooms: List<RoomEntity>,
    activeRoomId: String,
    language: AppLanguage,
    onJoinRoom: (String) -> Unit,
    onCreateRoom: (String, String, String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newRoomName by remember { mutableStateOf("") }
    var newRoomDesc by remember { mutableStateOf("") }
    var newRoomCategory by remember { mutableStateOf("SOCIAL / CHILL") }
    var isVipRoom by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeonBackground)
            .padding(top = 12.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header & Host Room CTA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Translations.get("live_worlds", language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonSecondary
                )
                Text(
                    text = Translations.get("live_worlds_sub", language),
                    fontSize = 11.sp,
                    color = NeonOutline
                )
            }

            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPrimary),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Host Room", tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Translations.get("host_room", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Live Rooms List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(rooms) { room ->
                RoomCard(
                    room = room,
                    isActive = room.roomId == activeRoomId,
                    language = language,
                    onJoin = { onJoinRoom(room.roomId) }
                )
            }
        }
    }

    // Host Custom Room Modal Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = NeonSurface,
            shape = RoundedCornerShape(24.dp),
            title = {
                Text(
                    text = Translations.get("create_room", language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonSecondary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newRoomName,
                        onValueChange = { newRoomName = it },
                        label = { Text("Nome da Sala / Room Name", fontSize = 12.sp, color = NeonOutline) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = NeonOnSurface,
                            unfocusedTextColor = NeonOnSurface
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newRoomDesc,
                        onValueChange = { newRoomDesc = it },
                        label = { Text("Descrição / Description", fontSize = 12.sp, color = NeonOutline) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedTextColor = NeonOnSurface,
                            unfocusedTextColor = NeonOnSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("VIP Only", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonGold)
                        Switch(
                            checked = isVipRoom,
                            onCheckedChange = { isVipRoom = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonGold, checkedTrackColor = NeonPrimary)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newRoomName.isNotBlank()) {
                            onCreateRoom(newRoomName, newRoomDesc, newRoomCategory, isVipRoom)
                            showCreateDialog = false
                            newRoomName = ""
                            newRoomDesc = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPrimary)
                ) {
                    Text("Criar / Create", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCreateDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonSurfaceHigh)
                ) {
                    Text("Cancelar", color = NeonOutline)
                }
            }
        )
    }
}

@Composable
private fun RoomCard(
    room: RoomEntity,
    isActive: Boolean,
    language: AppLanguage,
    onJoin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(NeonSurfaceHigh, NeonSurface)
                )
            )
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = if (isActive) NeonSecondary else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Badges & Users counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (room.isVip) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonGold.copy(alpha = 0.2f))
                                .border(1.dp, NeonGold, RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = "VIP", tint = NeonGold, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(Translations.get("vip_only", language), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = NeonGold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = room.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPrimaryVariant
                    )
                }

                // Active Users count
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NeonSecondary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${room.currentUsers}/${room.maxCapacity}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonOnSurface
                    )
                }
            }

            // Room Title & Description
            Column {
                Text(
                    text = room.roomName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonOnSurface
                )
                Text(
                    text = room.description,
                    fontSize = 12.sp,
                    color = NeonOutline,
                    maxLines = 2
                )
            }

            // Action Button
            Button(
                onClick = { onJoin() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) NeonSecondary else NeonPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isActive) "SALA ATIVA / ACTIVE ROOM" else Translations.get("join_room", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
