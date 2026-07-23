package com.zaziapps.mtg_scanner.ui.themes

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Enum class representing thematic MTG color profiles for background gradients and text contrast layouts.
 */
enum class CardDetailBackground(
    val textColor: Color,
    val gradient: Brush
) {
    UNCOLORED(
        textColor = Color.White,
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF4E545C), Color(0xFF2C3E50), Color(0xFF1A252F))
        )
    ),
    WHITE(
        textColor = Color(0xFF1C1C1C),
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFDF0), Color(0xFFF0E6D2), Color(0xFFD9C39E))
        )
    ),
    BLUE(
        textColor = Color.White,
        gradient = Brush.radialGradient(
            colors = listOf(Color(0xFF00B4DB), Color(0xFF0083B0), Color(0xFF0A2540))
        )
    ),
    BLACK(
        textColor = Color(0xFFE0E0E0),
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF3A3A3A), Color(0xFF1A1A1A), Color(0xFF0A0A0A))
        )
    ),
    RED(
        textColor = Color.White,
        gradient = Brush.linearGradient(
            colors = listOf(Color(0xFFED213A), Color(0xFF93291E), Color(0xFF4A0E17))
        )
    ),
    GREEN(
        textColor = Color.White,
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF11998e), Color(0xFF38ef7d), Color(0xFF0F4C2A))
        )
    )
}
