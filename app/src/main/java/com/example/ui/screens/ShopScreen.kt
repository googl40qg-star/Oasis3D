package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import androidx.compose.ui.unit.sp
import com.example.data.database.InventoryItemEntity
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

@Composable
fun ShopScreen(
    items: List<InventoryItemEntity>,
    language: AppLanguage,
    onBuyItem: (InventoryItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("FEATURED") }

    val filteredItems = items.filter { item ->
        val matchesCategory = when (selectedCategory) {
            "FEATURED" -> true
            "SHIRTS" -> item.category == "SHIRTS"
            "SHOES" -> item.category == "SHOES"
            "ACCESSORIES" -> item.category == "ACCESSORIES"
            else -> true
        }
        val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val featuredItem = items.firstOrNull { it.itemId == "shirt_cyber_jacket" } ?: items.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NeonBackground)
            .padding(top = 12.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Search
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Translations.get("virtual_boutique", language),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPrimaryVariant
                )
                Text(
                    text = Translations.get("boutique_sub", language),
                    fontSize = 11.sp,
                    color = NeonOutline
                )
            }
        }

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(Translations.get("search_items", language), fontSize = 12.sp, color = NeonOutline) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonOutline) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonPrimary,
                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                focusedContainerColor = NeonSurface,
                unfocusedContainerColor = NeonSurface,
                focusedTextColor = NeonOnSurface,
                unfocusedTextColor = NeonOnSurface
            ),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Category Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryPill(
                icon = Icons.Default.AutoAwesome,
                label = Translations.get("featured", language),
                isSelected = selectedCategory == "FEATURED",
                onClick = { selectedCategory = "FEATURED" }
            )
            CategoryPill(
                icon = Icons.Default.Checkroom,
                label = Translations.get("shirts", language),
                isSelected = selectedCategory == "SHIRTS",
                onClick = { selectedCategory = "SHIRTS" }
            )
            CategoryPill(
                icon = Icons.Default.Watch,
                label = Translations.get("accessories", language),
                isSelected = selectedCategory == "ACCESSORIES",
                onClick = { selectedCategory = "ACCESSORIES" }
            )
        }

        // Featured Hero Card
        featuredItem?.let { featured ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(NeonSurfaceHigh, NeonSurface)
                        )
                    )
                    .border(2.dp, Brush.horizontalGradient(listOf(NeonPrimary, NeonSecondary)), RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonSecondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEW DROP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    }

                    // Hero Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.cyber_jacket_1784929152292),
                            contentDescription = featured.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(featured.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NeonOnSurface)
                            Text(featured.description, fontSize = 11.sp, color = NeonOutline)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Diamond, contentDescription = "Diamonds", tint = NeonSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${featured.priceDiamonds}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonSecondary)
                        }
                    }

                    Button(
                        onClick = { onBuyItem(featured) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Buy", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (featured.isEquipped) Translations.get("equipped", language) else if (featured.isUnlocked) Translations.get("equip", language) else Translations.get("buy_now", language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Boutique Bento Grid
        Text(
            text = "CATÁLOGO / CATALOG",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = NeonSecondary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            items(filteredItems) { item ->
                ShopItemCard(
                    item = item,
                    language = language,
                    onBuy = { onBuyItem(item) }
                )
            }
        }
    }
}

@Composable
private fun CategoryPill(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) NeonPrimary else NeonSurfaceHigh)
            .border(1.dp, if (isSelected) NeonSecondary else Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = if (isSelected) Color.White else NeonOutline, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else NeonOutline)
        }
    }
}

@Composable
private fun ShopItemCard(
    item: InventoryItemEntity,
    language: AppLanguage,
    onBuy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeonSurface)
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Item Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeonSurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                if (item.itemId == "shoes_neon_sneakers") {
                    Image(
                        painter = painterResource(id = R.drawable.neon_sneakers_1784929164161),
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (item.itemId == "shirt_cyber_jacket") {
                    Image(
                        painter = painterResource(id = R.drawable.cyber_jacket_1784929152292),
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = item.name.split(" ").firstOrNull() ?: item.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonSecondary
                    )
                }
            }

            Text(item.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonOnSurface, maxLines = 1)

            // Price Tag
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.priceDiamonds > 0) {
                    Icon(Icons.Default.Diamond, contentDescription = "Diamonds", tint = NeonSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${item.priceDiamonds}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonSecondary)
                } else {
                    Icon(Icons.Default.MonetizationOn, contentDescription = "Coins", tint = NeonGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${item.priceCoins}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonGold)
                }
            }

            Button(
                onClick = { onBuy() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.isEquipped) NeonSecondary else if (item.isUnlocked) NeonPrimary else NeonSurfaceHigh
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (item.isEquipped) Translations.get("equipped", language) else if (item.isUnlocked) Translations.get("equip", language) else Translations.get("buy", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
