package com.example.data.repository

import com.example.data.database.AppDatabase
import com.example.data.database.ChatMessageEntity
import com.example.data.database.InventoryItemEntity
import com.example.data.database.RoomEntity
import com.example.data.database.UserProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NeonLoungeRepository(private val db: AppDatabase) {

    val userProfile: Flow<UserProfileEntity?> = db.userProfileDao().getUserProfile()
    val inventoryItems: Flow<List<InventoryItemEntity>> = db.inventoryDao().getAllItems()
    val rooms: Flow<List<RoomEntity>> = db.roomDao().getAllRooms()

    fun getChatMessages(roomId: String): Flow<List<ChatMessageEntity>> {
        return db.chatDao().getMessagesForRoom(roomId)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        // Seed default profile
        val profileDao = db.userProfileDao()
        val invDao = db.inventoryDao()
        val roomDao = db.roomDao()
        val chatDao = db.chatDao()

        val defaultProfile = UserProfileEntity()
        profileDao.insertOrUpdateProfile(defaultProfile)

        // Seed default shop catalog
        val initialItems = listOf(
            InventoryItemEntity(
                itemId = "shirt_cyber_jacket",
                name = "Cyber Jacket V.2",
                category = "SHIRTS",
                priceCoins = 0,
                priceDiamonds = 450,
                isUnlocked = true,
                isEquipped = true,
                imageUrl = "file:///android_asset/cyber_jacket.jpg",
                description = "Jaqueta neon reativa com fibra ótica fluorescente"
            ),
            InventoryItemEntity(
                itemId = "shirt_urban_glitch",
                name = "Urban Glitch Hoodie",
                category = "SHIRTS",
                priceCoins = 1200,
                priceDiamonds = 0,
                isUnlocked = true,
                isEquipped = false,
                imageUrl = "",
                description = "Moletom casual com estampas retrô neon"
            ),
            InventoryItemEntity(
                itemId = "shirt_neon_tee",
                name = "Neon Lounge Tee",
                category = "SHIRTS",
                priceCoins = 600,
                priceDiamonds = 0,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Camiseta clássica preta com logotipo da casa"
            ),
            InventoryItemEntity(
                itemId = "pants_techwear",
                name = "Techwear Joggers",
                category = "PANTS",
                priceCoins = 0,
                priceDiamonds = 0,
                isUnlocked = true,
                isEquipped = true,
                imageUrl = "",
                description = "Calça estilo cyber com tiras fiveladas"
            ),
            InventoryItemEntity(
                itemId = "pants_neon_jeans",
                name = "Holo-Stripe Jeans",
                category = "PANTS",
                priceCoins = 1800,
                priceDiamonds = 0,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Jeans ajustado com faixas brilhantes laterais"
            ),
            InventoryItemEntity(
                itemId = "shoes_neon_sneakers",
                name = "Neon Sneakers",
                category = "SHOES",
                priceCoins = 2800,
                priceDiamonds = 0,
                isUnlocked = true,
                isEquipped = true,
                imageUrl = "file:///android_asset/neon_sneakers.jpg",
                description = "Tênis futuristas com solado emissivo de LED cyan"
            ),
            InventoryItemEntity(
                itemId = "shoes_cyber_boots",
                name = "Cyber Boots",
                category = "SHOES",
                priceCoins = 0,
                priceDiamonds = 350,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Botas táticas pesadas com amortecimento de plasma"
            ),
            InventoryItemEntity(
                itemId = "acc_glow_eyes",
                name = "Glow Eyes: Phoenix",
                category = "ACCESSORIES",
                priceCoins = 0,
                priceDiamonds = 120,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Aura de fogo magenta fluorescente nos olhos"
            ),
            InventoryItemEntity(
                itemId = "acc_holo_ring",
                name = "Holo-Ring Wristband",
                category = "ACCESSORIES",
                priceCoins = 1500,
                priceDiamonds = 0,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Anéis holográficos orbitando o pulso"
            ),
            InventoryItemEntity(
                itemId = "acc_neural_headset",
                name = "Neural Headset",
                category = "ACCESSORIES",
                priceCoins = 0,
                priceDiamonds = 310,
                isUnlocked = false,
                isEquipped = false,
                imageUrl = "",
                description = "Headset cyberpunk minimalista com sensores"
            )
        )
        invDao.insertItems(initialItems)

        // Seed default 3D Rooms
        val initialRooms = listOf(
            RoomEntity(
                roomId = "room_neon_dance",
                roomName = "Neon Dance Club",
                description = "Pista de dança cyberpunk em alta energia com DJ de EDM",
                category = "EDM / DANCE",
                maxCapacity = 50,
                currentUsers = 18,
                isVip = false,
                primaryColorHex = "#7C3AED",
                secondaryColorHex = "#4CD7F6",
                imageUrl = ""
            ),
            RoomEntity(
                roomId = "room_vip_lounge",
                roomName = "VIP Lounge",
                description = "Espaço exclusivo para cidadãos de nível 20+. Camarotes privados e trilhas raras",
                category = "EXCLUSIVO",
                maxCapacity = 60,
                currentUsers = 42,
                isVip = true,
                primaryColorHex = "#FFD700",
                secondaryColorHex = "#FFB0CD",
                imageUrl = ""
            ),
            RoomEntity(
                roomId = "room_coffee_chill",
                roomName = "Coffee Chill",
                description = "Lobby aconchegante ao som de Lo-Fi Beats e conversas casuais",
                category = "LO-FI / SOCIAL",
                maxCapacity = 25,
                currentUsers = 9,
                isVip = false,
                primaryColorHex = "#03B5D3",
                secondaryColorHex = "#7C3AED",
                imageUrl = ""
            ),
            RoomEntity(
                roomId = "room_moonlight",
                roomName = "Moonlight Terrace",
                description = "Terraço aberto sob a lua cheia para bate-papo descontraído",
                category = "CHILL / TALK",
                maxCapacity = 40,
                currentUsers = 15,
                isVip = false,
                primaryColorHex = "#00E5FF",
                secondaryColorHex = "#FFB0CD",
                imageUrl = ""
            )
        )
        roomDao.insertRooms(initialRooms)

        // Seed welcome chat messages
        val initialMessages = listOf(
            ChatMessageEntity(
                roomId = "room_neon_dance",
                senderName = "DJ_CyberVibe",
                messageText = "Bem-vindos ao Neon Dance Club! / Welcome to Neon Dance Club! 🎧🔥",
                isQuickReaction = false
            ),
            ChatMessageEntity(
                roomId = "room_neon_dance",
                senderName = "NeonStar_99",
                messageText = "👋 Hello / Olá a todos!",
                isQuickReaction = true
            ),
            ChatMessageEntity(
                roomId = "room_neon_dance",
                senderName = "CyberUser_08",
                messageText = "Bora dançar! 🔥 LFG!",
                isQuickReaction = true
            )
        )
        for (msg in initialMessages) {
            chatDao.insertMessage(msg)
        }
    }

    suspend fun buyItem(itemId: String, category: String, priceCoins: Int, priceDiamonds: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val profile = db.userProfileDao().getUserProfile()
            // We get profile sync or update
            val currentCoins = 2800 // default or read
            // Perform Room operation
            db.inventoryDao().unlockItem(itemId)
            db.inventoryDao().equipItemInCategory(itemId, category)
            true
        }
    }

    suspend fun updateAppearance(skinHex: String, eyeHex: String, bodyType: String) {
        withContext(Dispatchers.IO) {
            db.userProfileDao().updateAppearance(skinHex, eyeHex, bodyType)
        }
    }

    suspend fun equipItem(itemId: String, category: String) {
        withContext(Dispatchers.IO) {
            db.inventoryDao().equipItemInCategory(itemId, category)
        }
    }

    suspend fun updateLanguage(lang: String) {
        withContext(Dispatchers.IO) {
            db.userProfileDao().updateLanguage(lang)
        }
    }

    suspend fun sendMessage(roomId: String, senderName: String, text: String, isQuick: Boolean = false) {
        withContext(Dispatchers.IO) {
            db.chatDao().insertMessage(
                ChatMessageEntity(
                    roomId = roomId,
                    senderName = senderName,
                    messageText = text,
                    isQuickReaction = isQuick
                )
            )
        }
    }

    suspend fun createRoom(name: String, desc: String, category: String, isVip: Boolean) {
        withContext(Dispatchers.IO) {
            val newRoom = RoomEntity(
                roomId = "room_" + System.currentTimeMillis(),
                roomName = name,
                description = desc,
                category = category,
                maxCapacity = 30,
                currentUsers = 1,
                isVip = isVip,
                primaryColorHex = "#7C3AED",
                secondaryColorHex = "#4CD7F6",
                imageUrl = ""
            )
            db.roomDao().insertRoom(newRoom)
        }
    }
}
