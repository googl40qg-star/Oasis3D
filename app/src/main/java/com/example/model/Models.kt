package com.example.model

enum class AppLanguage {
    EN, PT
}

enum class ItemCategory {
    FEATURED, SHIRTS, PANTS, SHOES, ACCESSORIES
}

enum class CurrencyType {
    COINS, DIAMONDS
}

data class Vector3(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

data class Avatar3DState(
    val id: String,
    val username: String,
    val isLocalUser: Boolean = false,
    val skinColorHex: String = "#FFE0BD",
    val eyeColorHex: String = "#4CD7F6",
    val bodyType: String = "Masculine",
    val shirtId: String = "shirt_cyber_jacket",
    val pantsId: String = "pants_techwear",
    val shoesId: String = "shoes_neon_sneakers",
    val accessoryId: String = "acc_none",
    val posX: Float = 0f, // -100 to +100
    val posY: Float = 0f, // -100 to +100
    val rotationDeg: Float = 0f,
    val isTalking: Boolean = false,
    val currentChatMessage: String? = null,
    val chatTime: Long = 0L
)

object Translations {
    fun get(key: String, lang: AppLanguage): String {
        val pt = mapOf(
            "app_title" to "NEON LOUNGE",
            "world" to "Mundo 3D",
            "avatar" to "Aparência",
            "shop" to "Loja",
            "chat" to "Chat",
            "skin_tone" to "Tom de Pele",
            "eye_color" to "Cor dos Olhos",
            "body_type" to "Tipo de Corpo",
            "masculine" to "Masculino",
            "feminine" to "Feminino",
            "wardrobe" to "Guarda-roupa",
            "shirts" to "Roupas",
            "pants" to "Calças",
            "shoes" to "Tênis",
            "accessories" to "Acessórios",
            "save" to "Salvar Alterações",
            "buy_now" to "Comprar Agora",
            "buy" to "Comprar",
            "purchased" to "Adquirido!",
            "equip" to "Equipar",
            "equipped" to "Equipado",
            "search_items" to "Buscar itens da loja...",
            "featured" to "Destaques",
            "virtual_boutique" to "Boutique Virtual 3D",
            "boutique_sub" to "Eleve sua presença digital com itens exclusivos.",
            "live_worlds" to "Mundos ao Vivo",
            "live_worlds_sub" to "Escolha seu destino 3D e comece a socializar.",
            "join_room" to "Entrar no Mundo",
            "vip_only" to "APENAS VIP",
            "host_room" to "Criar Sala 3D",
            "create_room" to "Criar Nova Sala",
            "type_message" to "Digite uma mensagem...",
            "voice_chat" to "Chat por Voz",
            "mic_on" to "Microfone Ligado",
            "mic_off" to "Microfone Desligado",
            "hello" to "👋 Olá!",
            "lfg" to "🔥 Vamos!",
            "nice" to "💎 Legal!",
            "dance" to "🎵 Dançar!",
            "room_created" to "Sala criada com sucesso!",
            "coins" to "Moedas",
            "diamonds" to "Diamantes",
            "daily_reward" to "Bônus Diário de 500 Moedas!",
            "move_hint" to "Use o Joystick ou WASD / Setas para se mover no espaço 3D"
        )

        val en = mapOf(
            "app_title" to "NEON LOUNGE",
            "world" to "3D World",
            "avatar" to "Avatar",
            "shop" to "Shop",
            "chat" to "Chat",
            "skin_tone" to "Skin Tone",
            "eye_color" to "Eye Color",
            "body_type" to "Body Type",
            "masculine" to "Masculine",
            "feminine" to "Feminine",
            "wardrobe" to "Wardrobe",
            "shirts" to "Shirts",
            "pants" to "Pants",
            "shoes" to "Shoes",
            "accessories" to "Accessories",
            "save" to "Save Customization",
            "buy_now" to "Buy Now",
            "buy" to "Buy",
            "purchased" to "Purchased!",
            "equip" to "Equip",
            "equipped" to "Equipped",
            "search_items" to "Search boutique items...",
            "featured" to "Featured",
            "virtual_boutique" to "Virtual 3D Boutique",
            "boutique_sub" to "Elevate your digital presence with limited edition drops.",
            "live_worlds" to "Live 3D Worlds",
            "live_worlds_sub" to "Choose your destination and start socializing.",
            "join_room" to "Join Room",
            "vip_only" to "VIP ONLY",
            "host_room" to "Host 3D Room",
            "create_room" to "Create New Room",
            "type_message" to "Type a message...",
            "voice_chat" to "Voice Chat",
            "mic_on" to "Mic Active",
            "mic_off" to "Mic Muted",
            "hello" to "👋 Hello!",
            "lfg" to "🔥 LFG!",
            "nice" to "💎 Nice!",
            "dance" to "🎵 Dance!",
            "room_created" to "Room created successfully!",
            "coins" to "Coins",
            "diamonds" to "Diamonds",
            "daily_reward" to "Daily Bonus +500 Coins!",
            "move_hint" to "Use Joystick or WASD / Arrow Keys to move in 3D"
        )

        val dictionary = if (lang == AppLanguage.PT) pt else en
        return dictionary[key] ?: key
    }
}
