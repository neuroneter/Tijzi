// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Fonts.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import com.parceros.tijzi.android.R

object TijziFonts {
    val TijziLogo = FontFamily(
        // Fuentes regulares
        Font(
            resId = R.font.exo_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.exo_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        // 🔥 NUEVAS FUENTES AGREGADAS
        Font(
            resId = R.font.exo_mediumitalic,
            weight = FontWeight.Medium,      // Peso Medium
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.exo_bolditalic,
            weight = FontWeight.Bold,        // Peso Bold
            style = FontStyle.Italic
        )
    )
}