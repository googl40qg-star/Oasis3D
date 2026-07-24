package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.database.InventoryItemEntity
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
import com.example.ui.theme.NeonSurfaceHigh
import com.example.ui.theme.NeonSurfaceHighest

@Composable
fun AvatarCreatorScreen(
    draftSkinHex: String,
    draftEyeHex: String,
    draftBodyType: String,
    inventory: List<InventoryItemEntity>,
    language: AppLanguage,
    onSetSkinHex: (String) -> Unit,
    onSetEyeHex: (String) -> Unit,
    onSetBodyType: (String) -> Unit,
    onEquipItem: (InventoryItemEntity) -> Unit,
    onSaveCustomization: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var selectedCategory by remember { mutableStateOf("SHIRTS") }

    val skinSwatches = listOf("#FFE0BD", "#FFCD94", "#EAC08D", "#965D30", "#623A1B")
    val eyeSwatches = listOf("#4CD7F6", "#D2BBFF", "#FFB0CD", "#00E5FF")

    // Filter items by category
    val categoryItems = inventory.filter { it.category == selectedCategory }

    val currentShirt = inventory.firstOrNull { it.isEquipped && it.category == "SHIRTS" }?.itemId ?: "shirt_cyber_jacket"
    val currentPants = inventory.firstOrNull { it.isEquipped && it.category == "PANTS" }?.itemId ?: "pants_techwear"
    val currentShoes = inventory.firstOrNull { it.isEquipped && it.category == "SHOES" }?.itemId ?: "shoes_neon_sneakers"
    val currentAcc = inventory.firstOrNull { it.isEquipped && it.category == "ACCESSORIES" }?.itemId ?: "acc_none"

    val previewAvatar = Avatar3DState(
        id = "preview",
        username = "Preview",
        isLocalUser = true,
        skinColorHex = draftSkinHex,
        eyeColorHex = draftEyeHex,
        bodyType = draftBodyType,
        shirtId = currentShirt,
        pantsId = currentPants,
        shoesId = currentShoes,
        accessoryId = currentAcc,
        posX = 0f,
        posY = 0f,
        rotationDeg = rotationAngle
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeonBackground)
            .padding(top = 12.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Translations.get("avatar", language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonSecondary
                )
                Text(
                    text = "CHARACTER CREATOR",
                    fontSize = 11.sp,
                    color = NeonOutline,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { onSaveCustomization() },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPrimary),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Translations.get("save", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Center 3D Stage Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(NeonSurface)
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas3DRenderer(
                avatars = listOf(previewAvatar),
                modifier = Modifier.fillMaxSize()
            )

            // Rotation Controls
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { rotationAngle = (rotationAngle - 45f) % 360f },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NeonSurfaceHigh)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateLeft,
                        contentDescription = "Rotate Left",
                        tint = NeonSecondary
                    )
                }

                IconButton(
                    onClick = { rotationAngle = (rotationAngle + 45f) % 360f },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NeonSurfaceHigh)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateRight,
                        contentDescription = "Rotate Right",
                        tint = NeonSecondary
                    )
                }
            }
        }

        // Traits Section: Skin Tone, Eye Color, Body Type
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeonSurface.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = Translations.get("skin_tone", language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOnSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                skinSwatches.forEach { hex ->
                    ColorSwatchCircle(
                        hexColor = hex,
                        isSelected = draftSkinHex.equals(hex, ignoreCase = true),
                        onClick = { onSetSkinHex(hex) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = Translations.get("eye_color", language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOnSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                eyeSwatches.forEach { hex ->
                    ColorSwatchCircle(
                        hexColor = hex,
                        isSelected = draftEyeHex.equals(hex, ignoreCase = true),
                        onClick = { onSetEyeHex(hex) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = Translations.get("body_type", language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOnSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeonSurfaceHigh)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (draftBodyType == "Masculine") NeonPrimary else Color.Transparent)
                        .clickable { onSetBodyType("Masculine") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Translations.get("masculine", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (draftBodyType == "Feminine") NeonPrimary else Color.Transparent)
                        .clickable { onSetBodyType("Feminine") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Translations.get("feminine", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Wardrobe Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeonSurface.copy(alpha = 0.6f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = Translations.get("wardrobe", language),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NeonSecondary
            )

            // Category tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WardrobeTab(
                    label = Translations.get("shirts", language),
                    isSelected = selectedCategory == "SHIRTS",
                    onClick = { selectedCategory = "SHIRTS" },
                    modifier = Modifier.weight(1f)
                )
                WardrobeTab(
                    label = Translations.get("pants", language),
                    isSelected = selectedCategory == "PANTS",
                    onClick = { selectedCategory = "PANTS" },
                    modifier = Modifier.weight(1f)
                )
                WardrobeTab(
                    label = Translations.get("shoes", language),
                    isSelected = selectedCategory == "SHOES",
                    onClick = { selectedCategory = "SHOES" },
                    modifier = Modifier.weight(1f)
                )
                WardrobeTab(
                    label = Translations.get("accessories", language),
                    isSelected = selectedCategory == "ACCESSORIES",
                    onClick = { selectedCategory = "ACCESSORIES" },
                    modifier = Modifier.weight(1f)
                )
            }

            // Items Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                items(categoryItems) { item ->
                    WardrobeCard(
                        item = item,
                        onClick = { onEquipItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSwatchCircle(
    hexColor: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = parseHexColor(hexColor)
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) NeonSecondary else Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (hexColor.equals("#FFE0BD", true) || hexColor.equals("#FFCD94", true)) Color.Black else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun WardrobeTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) NeonPrimary.copy(alpha = 0.3f) else NeonSurfaceHigh)
            .border(
                1.dp,
                if (isSelected) NeonPrimary else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) NeonSecondary else NeonOutline
        )
    }
}

@Composable
private fun WardrobeCard(
    item: InventoryItemEntity,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(NeonSurfaceHighest)
            .border(
                width = if (item.isEquipped) 2.dp else 1.dp,
                color = if (item.isEquipped) NeonSecondary else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = NeonOnSurface,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (!item.isUnlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = NeonSecondary,
                    modifier = Modifier.size(16.dp)
                )
            } else if (item.isEquipped) {
                Text(
                    text = "✓",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NeonSecondary
                )
            }
        }
    }
}

private fun parseHexColor(hex: String): Color {
    return try {
        val cleaned = hex.replace("#", "")
        val colorInt = cleaned.toLong(16)
        if (cleaned.length == 6) {
            Color(colorInt or 0xFF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        Color.Gray
    }
}
