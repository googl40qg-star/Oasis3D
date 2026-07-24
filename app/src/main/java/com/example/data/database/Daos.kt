package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET coins = :coins, diamonds = :diamonds WHERE id = 1")
    suspend fun updateCurrency(coins: Int, diamonds: Int)

    @Query("UPDATE user_profile SET skinToneHex = :skinHex, eyeColorHex = :eyeHex, bodyType = :bodyType WHERE id = 1")
    suspend fun updateAppearance(skinHex: String, eyeHex: String, bodyType: String)

    @Query("UPDATE user_profile SET equippedShirtId = :shirtId, equippedPantsId = :pantsId, equippedShoesId = :shoesId, equippedAccessoryId = :accessoryId WHERE id = 1")
    suspend fun updateEquippedItems(shirtId: String, pantsId: String, shoesId: String, accessoryId: String)

    @Query("UPDATE user_profile SET language = :lang WHERE id = 1")
    suspend fun updateLanguage(lang: String)
}

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items")
    fun getAllItems(): Flow<List<InventoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<InventoryItemEntity>)

    @Query("UPDATE inventory_items SET isUnlocked = 1 WHERE itemId = :itemId")
    suspend fun unlockItem(itemId: String)

    @Query("UPDATE inventory_items SET isEquipped = CASE WHEN itemId = :itemId THEN 1 ELSE 0 END WHERE category = :category")
    suspend fun equipItemInCategory(itemId: String, category: String)
}

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms")
    fun getAllRooms(): Flow<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE roomId = :roomId ORDER BY timestamp ASC")
    fun getMessagesForRoom(roomId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)
}
