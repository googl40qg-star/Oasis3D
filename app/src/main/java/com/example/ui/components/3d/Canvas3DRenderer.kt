package com.example.ui.components.`3d`

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Avatar3DState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Canvas3DRenderer(
    avatars: List<Avatar3DState>,
    roomName: String = "Neon Dance Club",
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Infinite animation for neon lights, floor grid pulses, and DJ beats
    val infiniteTransition = rememberInfiniteTransition(label = "3d_glow")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 2000, easing = LinearEasing)
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val originX = width / 2f
        val originY = height / 2f + 40.dp.toPx()
        val tileScale = width / 320f

        // 1. Draw 3D Background & Atmospheric Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF060E20),
                    Color(0xFF0B1326),
                    Color(0xFF131B2E)
                )
            )
        )

        // 2. Draw 3D Isometric Floor Grid
        draw3DFloorGrid(originX, originY, tileScale, pulseAnim)

        // 3. Draw Room Environment Props (DJ Stage, Speakers, Lounge Sofas, Neon Pillars)
        draw3DRoomProps(originX, originY, tileScale, pulseAnim)

        // 4. Sort Avatars by Isometric Depth (Depth sorting: Back to Front)
        val sortedAvatars = avatars.sortedBy { it.posX + it.posY }

        // 5. Render 3D Avatars
        sortedAvatars.forEach { avatar ->
            val iso = toIsoCoordinates(avatar.posX, avatar.posY, 0f, originX, originY, tileScale)
            draw3DAvatar(
                avatar = avatar,
                isoPos = iso,
                scale = tileScale,
                pulseAnim = pulseAnim,
                textMeasurer = textMeasurer
            )
        }
    }
}

// Convert 3D world coordinates (x, y, z) to Screen Isometric Offset
private fun toIsoCoordinates(
    wx: Float,
    wy: Float,
    wz: Float,
    originX: Float,
    originY: Float,
    scale: Float
): Offset {
    val isoX = originX + (wx - wy) * scale * 1.8f
    val isoY = originY + (wx + wy) * scale * 0.9f - wz * scale * 2.0f
    return Offset(isoX, isoY)
}

private fun DrawScope.draw3DFloorGrid(
    originX: Float,
    originY: Float,
    scale: Float,
    pulseAnim: Float
) {
    val gridSize = 10
    val gridStep = 18f

    // Draw isometric grid lines
    for (i in -gridSize..gridSize) {
        val start = toIsoCoordinates(i * gridStep, -gridSize * gridStep, 0f, originX, originY, scale)
        val end = toIsoCoordinates(i * gridStep, gridSize * gridStep, 0f, originX, originY, scale)
        drawCircle(
            color = Color(0xFF7C3AED).copy(alpha = 0.15f),
            radius = 1.5f,
            center = start
        )
        drawLine(
            color = Color(0xFF7C3AED).copy(alpha = 0.25f),
            start = start,
            end = end,
            strokeWidth = 1.2f
        )
    }

    for (j in -gridSize..gridSize) {
        val start = toIsoCoordinates(-gridSize * gridStep, j * gridStep, 0f, originX, originY, scale)
        val end = toIsoCoordinates(gridSize * gridStep, j * gridStep, 0f, originX, originY, scale)
        drawLine(
            color = Color(0xFF4CD7F6).copy(alpha = 0.25f),
            start = start,
            end = end,
            strokeWidth = 1.2f
        )
    }

    // Dance floor centerpiece grid tile with glowing colors
    val danceSize = 3 * gridStep
    val p1 = toIsoCoordinates(-danceSize, -danceSize, 0f, originX, originY, scale)
    val p2 = toIsoCoordinates(danceSize, -danceSize, 0f, originX, originY, scale)
    val p3 = toIsoCoordinates(danceSize, danceSize, 0f, originX, originY, scale)
    val p4 = toIsoCoordinates(-danceSize, danceSize, 0f, originX, originY, scale)

    val danceFloorPath = Path().apply {
        moveTo(p1.x, p1.y)
        lineTo(p2.x, p2.y)
        lineTo(p3.x, p3.y)
        lineTo(p4.x, p4.y)
        close()
    }

    val glowAlpha = 0.2f + 0.15f * sin(pulseAnim * Math.PI * 2).toFloat()
    drawPath(
        path = danceFloorPath,
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF7C3AED).copy(alpha = glowAlpha),
                Color(0xFF03B5D3).copy(alpha = 0.05f)
            ),
            center = Offset(originX, originY)
        )
    )
    drawPath(
        path = danceFloorPath,
        color = Color(0xFF4CD7F6).copy(alpha = 0.6f),
        style = Stroke(width = 2.5f)
    )
}

private fun DrawScope.draw3DRoomProps(
    originX: Float,
    originY: Float,
    scale: Float,
    pulseAnim: Float
) {
    // 1. DJ Stage at the back top
    val stagePos = toIsoCoordinates(0f, -100f, 0f, originX, originY, scale)
    val stageWidth = 120.dp.toPx()
    val stageHeight = 40.dp.toPx()

    // Stage platform
    drawRoundRect(
        color = Color(0xFF222A3D),
        topLeft = Offset(stagePos.x - stageWidth / 2, stagePos.y - stageHeight / 2),
        size = Size(stageWidth, stageHeight),
        cornerRadius = CornerRadius(16f, 16f)
    )
    drawRoundRect(
        color = Color(0xFF7C3AED),
        topLeft = Offset(stagePos.x - stageWidth / 2, stagePos.y - stageHeight / 2),
        size = Size(stageWidth, stageHeight),
        cornerRadius = CornerRadius(16f, 16f),
        style = Stroke(width = 2f)
    )

    // DJ Equalizer bars animation
    val bars = 8
    val barWidth = stageWidth / (bars * 2)
    for (b in 0 until bars) {
        val barH = (10 + (b % 4 + 1) * 6 * (0.5f + 0.5f * sin(pulseAnim * 6 + b))).dp.toPx()
        val bx = stagePos.x - stageWidth / 3 + b * (barWidth * 1.8f)
        val by = stagePos.y - barH / 2
        drawRect(
            color = if (b % 2 == 0) Color(0xFF4CD7F6) else Color(0xFFFFB0CD),
            topLeft = Offset(bx, by),
            size = Size(barWidth, barH)
        )
    }

    // 2. Left & Right Lounge Sofas
    val sofaLeftPos = toIsoCoordinates(-80f, 0f, 0f, originX, originY, scale)
    drawRoundRect(
        color = Color(0xFF2D3449),
        topLeft = Offset(sofaLeftPos.x - 30.dp.toPx(), sofaLeftPos.y - 15.dp.toPx()),
        size = Size(60.dp.toPx(), 30.dp.toPx()),
        cornerRadius = CornerRadius(12f, 12f)
    )
    drawRoundRect(
        color = Color(0xFF03B5D3),
        topLeft = Offset(sofaLeftPos.x - 30.dp.toPx(), sofaLeftPos.y - 15.dp.toPx()),
        size = Size(60.dp.toPx(), 30.dp.toPx()),
        cornerRadius = CornerRadius(12f, 12f),
        style = Stroke(width = 1.5f)
    )

    val sofaRightPos = toIsoCoordinates(80f, 0f, 0f, originX, originY, scale)
    drawRoundRect(
        color = Color(0xFF2D3449),
        topLeft = Offset(sofaRightPos.x - 30.dp.toPx(), sofaRightPos.y - 15.dp.toPx()),
        size = Size(60.dp.toPx(), 30.dp.toPx()),
        cornerRadius = CornerRadius(12f, 12f)
    )
    drawRoundRect(
        color = Color(0xFFFFB0CD),
        topLeft = Offset(sofaRightPos.x - 30.dp.toPx(), sofaRightPos.y - 15.dp.toPx()),
        size = Size(60.dp.toPx(), 30.dp.toPx()),
        cornerRadius = CornerRadius(12f, 12f),
        style = Stroke(width = 1.5f)
    )
}

private fun DrawScope.draw3DAvatar(
    avatar: Avatar3DState,
    isoPos: Offset,
    scale: Float,
    pulseAnim: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val shadowRadiusX = 22.dp.toPx()
    val shadowRadiusY = 10.dp.toPx()

    // 1. Draw 3D Floor Shadow
    drawOval(
        color = Color.Black.copy(alpha = 0.45f),
        topLeft = Offset(isoPos.x - shadowRadiusX, isoPos.y - shadowRadiusY),
        size = Size(shadowRadiusX * 2, shadowRadiusY * 2)
    )

    // 2. Voice-active pulsating ring
    if (avatar.isTalking) {
        val pulseSize = shadowRadiusX * (1.2f + 0.3f * sin(pulseAnim * Math.PI * 4).toFloat())
        drawOval(
            color = Color(0xFF4CD7F6).copy(alpha = 0.7f),
            topLeft = Offset(isoPos.x - pulseSize, isoPos.y - pulseSize * 0.5f),
            size = Size(pulseSize * 2, pulseSize),
            style = Stroke(width = 3f)
        )
    }

    // 3. Local User Direction Indicator
    if (avatar.isLocalUser) {
        val rad = Math.toRadians(avatar.rotationDeg.toDouble())
        val dirX = isoPos.x + (25 * sin(rad)).toFloat()
        val dirY = isoPos.y - (15 * cos(rad)).toFloat()
        drawLine(
            color = Color(0xFF4CD7F6),
            start = isoPos,
            end = Offset(dirX, dirY),
            strokeWidth = 3f
        )
        drawCircle(
            color = Color(0xFF4CD7F6),
            radius = 4.dp.toPx(),
            center = Offset(dirX, dirY)
        )
    }

    // Parse Colors
    val skinColor = parseHexColor(avatar.skinColorHex, Color(0xFFFFE0BD))
    val eyeColor = parseHexColor(avatar.eyeColorHex, Color(0xFF4CD7F6))

    // 4. Draw Avatar Body Mesh (Shoes, Legs, Torso, Head)
    val headY = isoPos.y - 36.dp.toPx()
    val torsoY = isoPos.y - 20.dp.toPx()

    // Pants / Shoes
    val pantsColor = when (avatar.pantsId) {
        "pants_neon_jeans" -> Color(0xFF00E5FF)
        else -> Color(0xFF131B2E)
    }
    val shoesColor = when (avatar.shoesId) {
        "shoes_cyber_boots" -> Color(0xFFFFB0CD)
        else -> Color(0xFF4CD7F6)
    }

    // Shoes
    drawOval(
        color = shoesColor,
        topLeft = Offset(isoPos.x - 12.dp.toPx(), isoPos.y - 6.dp.toPx()),
        size = Size(24.dp.toPx(), 10.dp.toPx())
    )

    // Pants / Legs
    drawRect(
        color = pantsColor,
        topLeft = Offset(isoPos.x - 8.dp.toPx(), torsoY + 4.dp.toPx()),
        size = Size(16.dp.toPx(), 14.dp.toPx())
    )

    // Torso / Shirt
    val shirtColor = when (avatar.shirtId) {
        "shirt_cyber_jacket" -> Color(0xFF7C3AED)
        "shirt_urban_glitch" -> Color(0xFFFFB0CD)
        else -> Color(0xFF222A3D)
    }

    drawRoundRect(
        color = shirtColor,
        topLeft = Offset(isoPos.x - 12.dp.toPx(), torsoY - 10.dp.toPx()),
        size = Size(24.dp.toPx(), 18.dp.toPx()),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Jacket neon stripe accent
    drawRect(
        color = Color(0xFF4CD7F6),
        topLeft = Offset(isoPos.x - 2.dp.toPx(), torsoY - 10.dp.toPx()),
        size = Size(4.dp.toPx(), 18.dp.toPx())
    )

    // Head
    drawCircle(
        color = skinColor,
        radius = 12.dp.toPx(),
        center = Offset(isoPos.x, headY)
    )

    // Eyes
    drawCircle(
        color = eyeColor,
        radius = 2.5f.dp.toPx(),
        center = Offset(isoPos.x - 4.dp.toPx(), headY - 2.dp.toPx())
    )
    drawCircle(
        color = eyeColor,
        radius = 2.5f.dp.toPx(),
        center = Offset(isoPos.x + 4.dp.toPx(), headY - 2.dp.toPx())
    )

    // Accessory Overlay (e.g. Neural Headset / Glow)
    if (avatar.accessoryId == "acc_neural_headset") {
        drawArc(
            color = Color(0xFF7C3AED),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(isoPos.x - 13.dp.toPx(), headY - 12.dp.toPx()),
            size = Size(26.dp.toPx(), 14.dp.toPx()),
            style = Stroke(width = 3.5f)
        )
    }

    // 5. User Tag Badge (Username + Level)
    val tagText = avatar.username
    val textResult = textMeasurer.measure(
        text = tagText,
        style = TextStyle(
            fontSize = 10.sp,
            color = if (avatar.isLocalUser) Color(0xFF4CD7F6) else Color.White
        )
    )
    val bgWidth = textResult.size.width + 16
    val bgHeight = textResult.size.height + 8
    val tagY = headY - 28.dp.toPx()

    drawRoundRect(
        color = Color.Black.copy(alpha = 0.65f),
        topLeft = Offset(isoPos.x - bgWidth / 2f, tagY - bgHeight / 2f),
        size = Size(bgWidth.toFloat(), bgHeight.toFloat()),
        cornerRadius = CornerRadius(8f, 8f)
    )
    drawText(
        textLayoutResult = textResult,
        topLeft = Offset(isoPos.x - textResult.size.width / 2f, tagY - textResult.size.height / 2f)
    )

    // 6. Floating Speech Bubble (if any)
    avatar.currentChatMessage?.let { message ->
        val bubbleMeasurer = textMeasurer.measure(
            text = message,
            style = TextStyle(fontSize = 11.sp, color = Color(0xFF003640))
        )
        val bWidth = (bubbleMeasurer.size.width + 24).toFloat()
        val bHeight = (bubbleMeasurer.size.height + 14).toFloat()
        val bubbleY = tagY - 28.dp.toPx()

        drawRoundRect(
            color = Color(0xFF4CD7F6),
            topLeft = Offset(isoPos.x - bWidth / 2f, bubbleY - bHeight / 2f),
            size = Size(bWidth, bHeight),
            cornerRadius = CornerRadius(16f, 16f)
        )
        drawText(
            textLayoutResult = bubbleMeasurer,
            topLeft = Offset(isoPos.x - bubbleMeasurer.size.width / 2f, bubbleY - bubbleMeasurer.size.height / 2f)
        )
    }
}

private fun parseHexColor(hex: String, defaultColor: Color): Color {
    return try {
        val cleaned = hex.replace("#", "")
        val colorInt = cleaned.toLong(16)
        if (cleaned.length == 6) {
            Color(colorInt or 0xFF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        defaultColor
    }
}
