// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Dimensions.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.ui.unit.dp

object TijziDimensions {

    // Espaciados generales
    val SpaceXXS = 2.dp
    val SpaceXS = 4.dp
    val SpaceS = 8.dp
    val SpaceM = 16.dp
    val SpaceL = 24.dp
    val SpaceXL = 32.dp
    val SpaceXXL = 48.dp
    val SpaceXXXL = 64.dp

    // Padding de pantallas
    val ScreenPaddingHorizontal = 24.dp
    val ScreenPaddingVertical = 32.dp
    val ScreenPaddingTop = 48.dp
    val ScreenPaddingBottom = 24.dp

    // Componentes - Botones
    val ButtonHeight = 56.dp
    val ButtonHeightSmall = 44.dp
    val ButtonRadius = 12.dp
    val ButtonPaddingHorizontal = 24.dp
    val ButtonPaddingVertical = 16.dp
    val ButtonElevation = 2.dp

    // Componentes - Inputs
    val InputHeight = 56.dp
    val InputRadius = 12.dp
    val InputPaddingHorizontal = 16.dp
    val InputPaddingVertical = 16.dp
    val InputBorderWidth = 1.dp
    val InputBorderWidthFocused = 2.dp

    // Componentes - OTP Input
    val OtpInputSize = 48.dp
    val OtpInputRadius = 8.dp
    val OtpSpacing = 8.dp
    val OtpGroupSpacing = 16.dp // Entre grupos XXX XXX

    // Componentes - Country Picker
    val CountryPickerHeight = 56.dp
    val CountryPickerMaxHeight = 200.dp
    val CountryFlagSize = 24.dp
    val CountryItemHeight = 56.dp

    // Logo y branding
    val LogoHeight = 48.dp
    val LogoSpacing = 24.dp

    // Cards y superficies
    val CardRadius = 16.dp
    val CardElevation = 4.dp
    val CardPadding = 16.dp

    // Modal y overlay
    val ModalRadius = 16.dp
    val ModalPadding = 24.dp
    val ModalMaxWidth = 400.dp

    // Iconos
    val IconSizeSmall = 16.dp
    val IconSizeMedium = 24.dp
    val IconSizeLarge = 32.dp
    val IconSizeXLarge = 48.dp

    // Touch targets (Accesibilidad)
    val MinTouchTarget = 48.dp
    val RecommendedTouchTarget = 56.dp

    // Separadores
    val DividerThickness = 1.dp
    val SectionSpacing = 32.dp

    // Específicos del diseño Tijzi
    object Auth {
        // Espaciado entre logo y subtitle
        val LogoToSubtitle = 16.dp

        // Espaciado entre subtitle y form
        val SubtitleToForm = 48.dp

        // Espaciado entre elementos del form
        val FormElementSpacing = 16.dp

        // Espaciado entre botones de canal
        val ChannelButtonSpacing = 12.dp

        // Espaciado antes del texto legal
        val FormToLegal = 48.dp

        // Espaciado específico para OTP
        val OtpInstructionSpacing = 24.dp
        val OtpToTimer = 32.dp

        // Altura del área de scroll principal
        val ContentMaxHeight = 600.dp
    }

    object Animation {
        // Duraciones en milisegundos
        const val Fast = 150
        const val Medium = 300
        const val Slow = 500

        // Delays
        const val DelayShort = 50
        const val DelayMedium = 100
        const val DelayLong = 200
    }
}

// Extensiones útiles para spacing
val Int.spacingDp get() = (this * 8).dp // Múltiplos de 8dp
val Double.spacingDp get() = (this * 8).dp