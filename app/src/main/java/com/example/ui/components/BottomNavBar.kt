package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.Translations
import com.example.ui.theme.NeonOutline
import com.example.ui.theme.NeonPrimary
import com.example.ui.theme.NeonPrimaryVariant
import com.example.ui.theme.NeonSecondary
import com.example.ui.theme.NeonSurface
import com.example.ui.viewmodel.ScreenTab

@Composable
fun BottomNavBar(
    currentTab: ScreenTab,
    language: AppLanguage,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(32.dp))
                .background(NeonSurface.copy(alpha = 0.85f))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.25f), Color.White.copy(alpha = 0.05f))
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    icon = Icons.Default.Public,
                    label = Translations.get("world", language),
                    isSelected = currentTab == ScreenTab.WORLD_3D,
                    onClick = { onTabSelected(ScreenTab.WORLD_3D) }
                )

                NavItem(
                    icon = Icons.Default.Language,
                    label = Translations.get("live_worlds", language).split(" ").firstOrNull() ?: "Mundos",
                    isSelected = currentTab == ScreenTab.WORLDS_LIST,
                    onClick = { onTabSelected(ScreenTab.WORLDS_LIST) }
                )

                // Highlighted Shop Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentTab == ScreenTab.SHOP) {
                                Brush.linearGradient(listOf(NeonPrimary, NeonSecondary))
                            } else {
                                Brush.linearGradient(listOf(NeonPrimary.copy(alpha = 0.6f), NeonSurface))
                            }
                        )
                        .clickable { onTabSelected(ScreenTab.SHOP) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Shop",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                NavItem(
                    icon = Icons.Default.PersonSearch,
                    label = Translations.get("avatar", language),
                    isSelected = currentTab == ScreenTab.AVATAR,
                    onClick = { onTabSelected(ScreenTab.AVATAR) }
                )

                NavItem(
                    icon = Icons.Default.Forum,
                    label = Translations.get("chat", language),
                    isSelected = currentTab == ScreenTab.CHAT,
                    onClick = { onTabSelected(ScreenTab.CHAT) }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) NeonSecondary else NeonOutline,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NeonSecondary else NeonOutline
        )
    }
}
