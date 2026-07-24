package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserProfileEntity
import com.example.model.AppLanguage
import com.example.ui.theme.NeonBackground
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonOnSurface
import com.example.ui.theme.NeonPrimary
import com.example.ui.theme.NeonPrimaryVariant
import com.example.ui.theme.NeonSecondary
import com.example.ui.theme.NeonSurfaceHigh

@Composable
fun TopAppBar(
    userProfile: UserProfileEntity,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onClaimDailyReward: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(NeonBackground.copy(alpha = 0.85f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.15f), Color.Transparent)
                ),
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & User level
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(NeonPrimary, NeonSecondary, NeonPrimary)
                            )
                        )
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(NeonSurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NL",
                        color = NeonSecondary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "NEON LOUNGE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NeonPrimaryVariant,
                    letterSpacing = (-0.5).sp
                )
            }

            // Right Actions: Coins, Diamonds, Claim, Language
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Coins
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonSurfaceHigh)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Coins",
                        tint = NeonGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.coins}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonOnSurface
                    )
                }

                // Diamonds
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonSurfaceHigh)
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = "Diamonds",
                        tint = NeonSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.diamonds}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonOnSurface
                    )
                }

                // Daily Gift
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(NeonPrimary)
                        .clickable { onClaimDailyReward() }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Daily Bonus",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Language toggle switch
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonPrimary.copy(alpha = 0.2f))
                        .border(1.dp, NeonPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onToggleLanguage() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        tint = NeonSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = language.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonSecondary
                    )
                }
            }
        }
    }
}
