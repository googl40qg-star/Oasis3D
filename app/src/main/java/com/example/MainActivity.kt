package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppLanguage
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopAppBar
import com.example.ui.screens.AvatarCreatorScreen
import com.example.ui.screens.ChatHistoryScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.World3DScreen
import com.example.ui.screens.WorldsListScreen
import com.example.ui.theme.NeonBackground
import com.example.ui.theme.NeonLoungeTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NeonLoungeTheme {
                NeonLoungeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NeonLoungeApp(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val language by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val inventory by viewModel.inventory.collectAsStateWithLifecycle()
    val roomsList by viewModel.roomsList.collectAsStateWithLifecycle()
    val activeRoomId by viewModel.activeRoomId.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isMicMuted by viewModel.isMicMuted.collectAsStateWithLifecycle()
    val voiceVolumeLevel by viewModel.voiceVolumeLevel.collectAsStateWithLifecycle()
    val roomAvatars by viewModel.roomAvatars.collectAsStateWithLifecycle()

    val draftSkinHex by viewModel.draftSkinColor.collectAsStateWithLifecycle()
    val draftEyeHex by viewModel.draftEyeColor.collectAsStateWithLifecycle()
    val draftBodyType by viewModel.draftBodyType.collectAsStateWithLifecycle()

    val purchaseStatus by viewModel.purchaseStatus.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(purchaseStatus) {
        purchaseStatus?.let { status ->
            snackbarHostState.showSnackbar(status)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                userProfile = userProfile,
                language = language,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onClaimDailyReward = { viewModel.claimDailyReward() }
            )
        },
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                language = language,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NeonBackground)
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "tab_switch") { tab ->
                when (tab) {
                    ScreenTab.WORLD_3D -> {
                        World3DScreen(
                            avatars = roomAvatars,
                            language = language,
                            isMicMuted = isMicMuted,
                            voiceVolumeLevel = voiceVolumeLevel,
                            onMoveAvatar = { dx, dy -> viewModel.moveLocalAvatar(dx, dy) },
                            onToggleMic = { viewModel.toggleMic() },
                            onSendMessage = { text, isQuick -> viewModel.sendChatMessage(text, isQuick) }
                        )
                    }
                    ScreenTab.AVATAR -> {
                        AvatarCreatorScreen(
                            draftSkinHex = draftSkinHex,
                            draftEyeHex = draftEyeHex,
                            draftBodyType = draftBodyType,
                            inventory = inventory,
                            language = language,
                            onSetSkinHex = { viewModel.setDraftSkinColor(it) },
                            onSetEyeHex = { viewModel.setDraftEyeColor(it) },
                            onSetBodyType = { viewModel.setDraftBodyType(it) },
                            onEquipItem = { viewModel.buyOrEquipItem(it) },
                            onSaveCustomization = { viewModel.saveAvatarCustomization() }
                        )
                    }
                    ScreenTab.SHOP -> {
                        ShopScreen(
                            items = inventory,
                            language = language,
                            onBuyItem = { viewModel.buyOrEquipItem(it) }
                        )
                    }
                    ScreenTab.WORLDS_LIST -> {
                        WorldsListScreen(
                            rooms = roomsList,
                            activeRoomId = activeRoomId,
                            language = language,
                            onJoinRoom = { viewModel.joinRoom(it) },
                            onCreateRoom = { name, desc, cat, vip -> viewModel.createNewRoom(name, desc, cat, vip) }
                        )
                    }
                    ScreenTab.CHAT -> {
                        ChatHistoryScreen(
                            chatMessages = chatMessages,
                            language = language,
                            isMicMuted = isMicMuted,
                            voiceVolumeLevel = voiceVolumeLevel,
                            onToggleMic = { viewModel.toggleMic() },
                            onSendMessage = { text, isQuick -> viewModel.sendChatMessage(text, isQuick) }
                        )
                    }
                }
            }
        }
    }
}
