// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Color.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.ui.graphics.Color

object TijziColors {
    // Colores principales del diseño
    val Primary = Color(0xFFE53E3E)        // Rojo principal de botones
    val PrimaryVariant = Color(0xFFC53030)  // Rojo más oscuro para pressed
    val Secondary = Color(0xFF4A5568)       // Gris para elementos secundarios

    // Backgrounds
    val Background = Color(0xFF2D3748)      // Fondo principal oscuro
    val Surface = Color(0xFF4A5568)         // Superficie de componentes
    val SurfaceVariant = Color(0xFF5A6578)  // Variante para inputs

    // Text colors
    val OnPrimary = Color.White            // Texto sobre botones rojos
    val OnBackground = Color.White         // Texto principal
    val OnSurface = Color.White           // Texto sobre superficies
    val OnSurfaceVariant = Color(0xFFA0ADB8) // Texto secundario/placeholder

    // Estados
    val Success = Color(0xFF25D366)        // Verde WhatsApp
    val Error = Color(0xFFE53E3E)          // Rojo para errores
    val Warning = Color(0xFFF59E0B)        // Amarillo para advertencias
    val Info = Color(0xFF3182CE)           // Azul para información

    // Canales específicos
    val WhatsApp = Color(0xFF25D366)       // Verde WhatsApp oficial
    val SMS = Color(0xFFE53E3E)            // Rojo primary para SMS
    val Telegram = Color(0xFF0088CC)       // Azul Telegram oficial

    // Borders y divisores
    val Border = Color(0xFF5A6578)         // Bordes de inputs
    val BorderFocused = Color(0xFFE53E3E)  // Borde cuando está enfocado
    val Divider = Color(0xFF4A5568)        // Líneas divisorias

    // Overlays
    val Overlay = Color(0x80000000)        // Para modals/loading
    val Disabled = Color(0xFF718096)       // Elementos deshabilitados
}

// Extensión para facilitar el uso
val TijziColorScheme = androidx.compose.material3.darkColorScheme(
    primary = TijziColors.Primary,
    onPrimary = TijziColors.OnPrimary,
    primaryContainer = TijziColors.PrimaryVariant,
    secondary = TijziColors.Secondary,
    background = TijziColors.Background,
    onBackground = TijziColors.OnBackground,
    surface = TijziColors.Surface,
    onSurface = TijziColors.OnSurface,
    surfaceVariant = TijziColors.SurfaceVariant,
    onSurfaceVariant = TijziColors.OnSurfaceVariant,
    error = TijziColors.Error,
    outline = TijziColors.Border
)