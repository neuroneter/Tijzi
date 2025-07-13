// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Color.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.ui.graphics.Color

object TijziColors {
    // 🔥 NUEVOS COLORES OFICIALES TIJZI
    val Primary = Color(0xFFBE1E2D)        // Rojo oficial Tijzi
    val PrimaryVariant = Color(0xFF9A1725)  // Rojo más oscuro para pressed states
    val Secondary = Color(0xFF4A5568)       // Gris para elementos secundarios

    // 🔥 NUEVO FONDO OFICIAL
    val Background = Color(0xFF2B2831)      // Fondo oficial Tijzi
    val Surface = Color(0xFF3A3741)         // Superficie de componentes (un poco más claro que background)
    val SurfaceVariant = Color(0xFF45424A)  // Variante para inputs (más claro)

    // 🔥 TEXTO SIEMPRE BLANCO - NUNCA ROJO
    val OnPrimary = Color.White            // Texto sobre botones rojos (BLANCO)
    val OnBackground = Color.White         // Texto principal (BLANCO)
    val OnSurface = Color.White           // Texto sobre superficies (BLANCO)
    val OnSurfaceVariant = Color(0xFFB8B5BD) // Texto secundario/placeholder (gris claro)

    // Estados - Manteniendo funcionalidad pero con mejor contraste
    val Success = Color(0xFF25D366)        // Verde WhatsApp
    val Error = Color(0xFFFF4444)          // Rojo más brillante para errores (NO para texto)
    val Warning = Color(0xFFFFA726)        // Amarillo para advertencias
    val Info = Color(0xFF42A5F5)           // Azul para información

    // Canales específicos - Colores oficiales
    val WhatsApp = Color(0xFF25D366)       // Verde WhatsApp oficial
    val SMS = Color(0xFFBE1E2D)            // Rojo Tijzi para SMS
    val Telegram = Color(0xFF0088CC)       // Azul Telegram oficial

    // Borders y divisores - Ajustados al nuevo fondo
    val Border = Color(0xFF55525A)         // Bordes de inputs
    val BorderFocused = Color(0xFFBE1E2D)  // Borde rojo Tijzi cuando está enfocado
    val Divider = Color(0xFF3A3741)        // Líneas divisorias

    // Overlays y estados especiales
    val Overlay = Color(0x80000000)        // Para modals/loading
    val Disabled = Color(0xFF6B6870)       // Elementos deshabilitados
}

// 🔥 MATERIAL 3 COLOR SCHEME ACTUALIZADO
val TijziColorScheme = androidx.compose.material3.darkColorScheme(
    // Colores principales
    primary = TijziColors.Primary,
    onPrimary = TijziColors.OnPrimary,        // BLANCO sobre rojo
    primaryContainer = TijziColors.PrimaryVariant,
    onPrimaryContainer = Color.White,         // BLANCO sobre contenedor primario

    // Colores secundarios
    secondary = TijziColors.Secondary,
    onSecondary = Color.White,                // BLANCO sobre secundario

    // Fondos
    background = TijziColors.Background,
    onBackground = TijziColors.OnBackground,  // BLANCO sobre fondo
    surface = TijziColors.Surface,
    onSurface = TijziColors.OnSurface,       // BLANCO sobre superficie
    surfaceVariant = TijziColors.SurfaceVariant,
    onSurfaceVariant = TijziColors.OnSurfaceVariant, // Gris claro para placeholders

    // Estados
    error = TijziColors.Error,
    onError = Color.White,                    // BLANCO sobre error

    // Borders
    outline = TijziColors.Border,
    outlineVariant = TijziColors.BorderFocused
)