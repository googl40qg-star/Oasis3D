package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.ChatMessageEntity
import com.example.data.database.InventoryItemEntity
import com.example.data.database.RoomEntity
import com.example.data.database.UserProfileEntity
import com.example.data.repository.NeonLoungeRepository
import com.example.model.AppLanguage
import com.example.model.Avatar3DState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class ScreenTab {
    WORLD_3D, AVATAR, SHOP, WORLDS_LIST, CHAT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NeonLoungeRepository

    private val _currentTab = MutableStateFlow(ScreenTab.WORLD_3D)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(AppLanguage.PT)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _activeRoomId = MutableStateFlow("room_neon_dance")
    val activeRoomId: StateFlow<String> = _activeRoomId.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfileEntity())
    val userProfile: StateFlow<UserProfileEntity> = _userProfile.asStateFlow()

    private val _inventory = MutableStateFlow<List<InventoryItemEntity>>(emptyList())
    val inventory: StateFlow<List<InventoryItemEntity>> = _inventory.asStateFlow()

    private val _roomsList = MutableStateFlow<List<RoomEntity>>(emptyList())
    val roomsList: StateFlow<List<RoomEntity>> = _roomsList.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessageEntity>> = _chatMessages.asStateFlow()

    // Voice chat simulation
    private val _isMicMuted = MutableStateFlow(true)
    val isMicMuted: StateFlow<Boolean> = _isMicMuted.asStateFlow()

    private val _voiceVolumeLevel = MutableStateFlow(0f)
    val voiceVolumeLevel: StateFlow<Float> = _voiceVolumeLevel.asStateFlow()

    // 3D Room State - Avatars present in the 3D space
    private val _roomAvatars = MutableStateFlow<List<Avatar3DState>>(emptyList())
    val roomAvatars: StateFlow<List<Avatar3DState>> = _roomAvatars.asStateFlow()

    // Local Avatar customization draft
    private val _draftSkinColor = MutableStateFlow("#FFE0BD")
    val draftSkinColor: StateFlow<String> = _draftSkinColor.asStateFlow()

    private val _draftEyeColor = MutableStateFlow("#4CD7F6")
    val draftEyeColor: StateFlow<String> = _draftEyeColor.asStateFlow()

    private val _draftBodyType = MutableStateFlow("Masculine")
    val draftBodyType: StateFlow<String> = _draftBodyType.asStateFlow()

    // Purchase toast/dialog feedback
    private val _purchaseStatus = MutableStateFlow<String?>(null)
    val purchaseStatus: StateFlow<String?> = _purchaseStatus.asStateFlow()

    private var voiceAnimationJob: Job? = null

    init {
        val db = AppDatabase.getInstance(application)
        repository = NeonLoungeRepository(db)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()

            // Observe User Profile
            launch {
                repository.userProfile.collectLatest { profile ->
                    if (profile != null) {
                        _userProfile.value = profile
                        _selectedLanguage.value = if (profile.language == "EN") AppLanguage.EN else AppLanguage.PT
                        _draftSkinColor.value = profile.skinToneHex
                        _draftEyeColor.value = profile.eyeColorHex
                        _draftBodyType.value = profile.bodyType
                        updateLocalAvatarIn3DRoom(profile)
                    }
                }
            }

            // Observe Inventory
            launch {
                repository.inventoryItems.collectLatest { items ->
                    _inventory.value = items
                    updateLocalAvatarIn3DRoom(_userProfile.value)
                }
            }

            // Observe Rooms
            launch {
                repository.rooms.collectLatest { roomList ->
                    _roomsList.value = roomList
                }
            }

            // Observe Chat for active room
            launch {
                _activeRoomId.collectLatest { roomId ->
                    repository.getChatMessages(roomId).collectLatest { msgs ->
                        _chatMessages.value = msgs
                    }
                }
            }
        }

        setupInitial3DAvatars()
    }

    private fun setupInitial3DAvatars() {
        val botAvatars = listOf(
            Avatar3DState(
                id = "bot_1",
                username = "NeonStar_99",
                isLocalUser = false,
                skinColorHex = "#FFCD94",
                eyeColorHex = "#D2BBFF",
                bodyType = "Feminine",
                shirtId = "shirt_urban_glitch",
                shoesId = "shoes_neon_sneakers",
                posX = 35f,
                posY = -20f,
                rotationDeg = 210f,
                isTalking = false
            ),
            Avatar3DState(
                id = "bot_2",
                username = "CyberUser_08",
                isLocalUser = false,
                skinColorHex = "#965D30",
                eyeColorHex = "#FFB0CD",
                bodyType = "Masculine",
                shirtId = "shirt_neon_tee",
                shoesId = "shoes_cyber_boots",
                posX = -40f,
                posY = 30f,
                rotationDeg = 45f,
                isTalking = false
            ),
            Avatar3DState(
                id = "bot_3",
                username = "DJ_CyberVibe",
                isLocalUser = false,
                skinColorHex = "#EAC08D",
                eyeColorHex = "#00E5FF",
                bodyType = "Masculine",
                shirtId = "shirt_cyber_jacket",
                posX = 0f,
                posY = -80f,
                rotationDeg = 180f,
                isTalking = true,
                currentChatMessage = "🎵 Drop the synthwave beat!"
            )
        )
        val localAvatar = createLocalAvatarState(_userProfile.value)
        _roomAvatars.value = listOf(localAvatar) + botAvatars
    }

    private fun createLocalAvatarState(profile: UserProfileEntity): Avatar3DState {
        val equippedShirt = _inventory.value.firstOrNull { it.isEquipped && it.category == "SHIRTS" }?.itemId ?: profile.equippedShirtId
        val equippedPants = _inventory.value.firstOrNull { it.isEquipped && it.category == "PANTS" }?.itemId ?: profile.equippedPantsId
        val equippedShoes = _inventory.value.firstOrNull { it.isEquipped && it.category == "SHOES" }?.itemId ?: profile.equippedShoesId
        val equippedAcc = _inventory.value.firstOrNull { it.isEquipped && it.category == "ACCESSORIES" }?.itemId ?: profile.equippedAccessoryId

        val existingLocal = _roomAvatars.value.firstOrNull { it.isLocalUser }
        return Avatar3DState(
            id = "local_user",
            username = profile.username,
            isLocalUser = true,
            skinColorHex = profile.skinToneHex,
            eyeColorHex = profile.eyeColorHex,
            bodyType = profile.bodyType,
            shirtId = equippedShirt,
            pantsId = equippedPants,
            shoesId = equippedShoes,
            accessoryId = equippedAcc,
            posX = existingLocal?.posX ?: 0f,
            posY = existingLocal?.posY ?: 10f,
            rotationDeg = existingLocal?.rotationDeg ?: 0f,
            isTalking = existingLocal?.isTalking ?: false,
            currentChatMessage = existingLocal?.currentChatMessage
        )
    }

    private fun updateLocalAvatarIn3DRoom(profile: UserProfileEntity) {
        val updatedLocal = createLocalAvatarState(profile)
        val currentBots = _roomAvatars.value.filter { !it.isLocalUser }
        _roomAvatars.value = listOf(updatedLocal) + currentBots
    }

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun toggleLanguage() {
        val nextLang = if (_selectedLanguage.value == AppLanguage.EN) AppLanguage.PT else AppLanguage.EN
        _selectedLanguage.value = nextLang
        viewModelScope.launch {
            repository.updateLanguage(nextLang.name)
        }
    }

    fun joinRoom(roomId: String) {
        _activeRoomId.value = roomId
        _currentTab.value = ScreenTab.WORLD_3D
    }

    // Avatar 3D Movement logic
    fun moveLocalAvatar(deltaX: Float, deltaY: Float) {
        val currentList = _roomAvatars.value.toMutableList()
        val index = currentList.indexOfFirst { it.isLocalUser }
        if (index != -1) {
            val old = currentList[index]
            val speed = 3.5f
            val newX = (old.posX + deltaX * speed).coerceIn(-90f, 90f)
            val newY = (old.posY + deltaY * speed).coerceIn(-90f, 90f)

            var angle = old.rotationDeg
            if (deltaX != 0f || deltaY != 0f) {
                angle = Math.toDegrees(Math.atan2(deltaX.toDouble(), -deltaY.toDouble())).toFloat()
            }

            currentList[index] = old.copy(posX = newX, posY = newY, rotationDeg = angle)
            _roomAvatars.value = currentList
        }
    }

    // Avatar Customization Draft setters
    fun setDraftSkinColor(colorHex: String) {
        _draftSkinColor.value = colorHex
    }

    fun setDraftEyeColor(colorHex: String) {
        _draftEyeColor.value = colorHex
    }

    fun setDraftBodyType(bodyType: String) {
        _draftBodyType.value = bodyType
    }

    fun saveAvatarCustomization() {
        viewModelScope.launch {
            repository.updateAppearance(
                skinHex = _draftSkinColor.value,
                eyeHex = _draftEyeColor.value,
                bodyType = _draftBodyType.value
            )
            _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Aparência salva!" else "Customization saved!"
            delay(2000)
            _purchaseStatus.value = null
        }
    }

    fun toggleMic() {
        _isMicMuted.value = !_isMicMuted.value
        val isMuted = _isMicMuted.value

        // Update local avatar isTalking state
        val currentList = _roomAvatars.value.toMutableList()
        val index = currentList.indexOfFirst { it.isLocalUser }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isTalking = !isMuted)
            _roomAvatars.value = currentList
        }

        voiceAnimationJob?.cancel()
        if (!isMuted) {
            voiceAnimationJob = viewModelScope.launch {
                while (!_isMicMuted.value) {
                    _voiceVolumeLevel.value = (0.3f + Math.random().toFloat() * 0.7f)
                    delay(200)
                }
                _voiceVolumeLevel.value = 0f
            }
        } else {
            _voiceVolumeLevel.value = 0f
        }
    }

    fun sendChatMessage(text: String, isQuick: Boolean = false) {
        if (text.isBlank()) return
        val profile = _userProfile.value
        viewModelScope.launch {
            repository.sendMessage(_activeRoomId.value, profile.username, text, isQuick)

            // Attach floating chat bubble above local avatar in 3D world
            val currentList = _roomAvatars.value.toMutableList()
            val index = currentList.indexOfFirst { it.isLocalUser }
            if (index != -1) {
                currentList[index] = currentList[index].copy(
                    currentChatMessage = text,
                    chatTime = System.currentTimeMillis()
                )
                _roomAvatars.value = currentList

                // Clear floating bubble after 4 seconds
                delay(4000)
                val currentList2 = _roomAvatars.value.toMutableList()
                val index2 = currentList2.indexOfFirst { it.isLocalUser }
                if (index2 != -1 && currentList2[index2].currentChatMessage == text) {
                    currentList2[index2] = currentList2[index2].copy(currentChatMessage = null)
                    _roomAvatars.value = currentList2
                }
            }
        }
    }

    fun buyOrEquipItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            val profile = _userProfile.value
            if (item.isUnlocked) {
                // Equip
                repository.equipItem(item.itemId, item.category)
                _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Item equipado!" else "Item equipped!"
            } else {
                // Check if user has enough currency
                if (item.priceCoins > 0 && profile.coins >= item.priceCoins) {
                    val newCoins = profile.coins - item.priceCoins
                    val db = AppDatabase.getInstance(getApplication())
                    db.userProfileDao().updateCurrency(newCoins, profile.diamonds)
                    repository.buyItem(item.itemId, item.category, item.priceCoins, item.priceDiamonds)
                    _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Item comprado e equipado!" else "Item purchased & equipped!"
                } else if (item.priceDiamonds > 0 && profile.diamonds >= item.priceDiamonds) {
                    val newDiamonds = profile.diamonds - item.priceDiamonds
                    val db = AppDatabase.getInstance(getApplication())
                    db.userProfileDao().updateCurrency(profile.coins, newDiamonds)
                    repository.buyItem(item.itemId, item.category, item.priceCoins, item.priceDiamonds)
                    _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Item comprado e equipado!" else "Item purchased & equipped!"
                } else {
                    _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Saldo insuficiente!" else "Insufficient balance!"
                }
            }
            delay(2500)
            _purchaseStatus.value = null
        }
    }

    fun createNewRoom(name: String, desc: String, category: String, isVip: Boolean) {
        viewModelScope.launch {
            repository.createRoom(name, desc, category, isVip)
            _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "Sala criada!" else "Room created!"
            delay(2000)
            _purchaseStatus.value = null
        }
    }

    fun claimDailyReward() {
        viewModelScope.launch {
            val profile = _userProfile.value
            val newCoins = profile.coins + 500
            val db = AppDatabase.getInstance(getApplication())
            db.userProfileDao().updateCurrency(newCoins, profile.diamonds)
            _purchaseStatus.value = if (_selectedLanguage.value == AppLanguage.PT) "+500 Moedas recebidas!" else "+500 Coins claimed!"
            delay(2000)
            _purchaseStatus.value = null
        }
    }
}
