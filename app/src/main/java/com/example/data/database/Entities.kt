package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "CyberUser_01",
    val level: Int = 24,
    val coins: Int = 2800,
    val diamonds: Int = 1250,
    val skinToneHex: String = "#FFE0BD",
    val eyeColorHex: String = "#4CD7F6",
    val bodyType: String = "Masculine",
    val equippedShirtId: String = "shirt_cyber_jacket",
    val equippedPantsId: String = "pants_techwear",
    val equippedShoesId: String = "shoes_neon_sneakers",
    val equippedAccessoryId: String = "acc_none",
    val language: String = "PT"
)

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey val itemId: String,
    val name: String,
    val category: String, // SHIRTS, PANTS, SHOES, ACCESSORIES, FEATURED
    val priceCoins: Int,
    val priceDiamonds: Int,
    val isUnlocked: Boolean,
    val isEquipped: Boolean,
    val imageUrl: String,
    val description: String = ""
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val roomId: String,
    val roomName: String,
    val description: String,
    val category: String,
    val maxCapacity: Int = 50,
    val currentUsers: Int = 12,
    val isVip: Boolean = false,
    val primaryColorHex: String = "#7C3AED",
    val secondaryColorHex: String = "#4CD7F6",
    val imageUrl: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: String,
    val senderName: String,
    val senderAvatarUrl: String = "",
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isQuickReaction: Boolean = false
)
