// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Theme.kt
package com.parceros.tijzi.android.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun TijziTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Siempre usamos tema oscuro por el diseño
    val colorScheme = TijziColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            // Configurar iconos de la barra de estado para tema oscuro
            WindowCompat.getInsetsController(window, view).let { controller ->
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TijziMaterial3Typography,
        content = content
    )
}

// Composable para acceder fácilmente a nuestros valores custom
object TijziTheme {
    val colors: TijziColors
        @Composable
        get() = TijziColors

    val typography: TijziTypography
        @Composable
        get() = TijziTypography

    val dimensions: TijziDimensions
        @Composable
        get() = TijziDimensions
}