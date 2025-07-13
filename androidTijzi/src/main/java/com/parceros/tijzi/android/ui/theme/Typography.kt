// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Typography.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object TijziTypography {

    // Logo y títulos principales
    val LogoTitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
        color = TijziColors.OnBackground
    )

    // Subtítulo motivacional
    val WelcomeSubtitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant
    )

    // Texto de botones principales
    val ButtonText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
        color = TijziColors.OnPrimary
    )

    // Labels de inputs
    val InputLabel = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color = TijziColors.OnSurface
    )

    // Texto dentro de inputs
    val InputText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = TijziColors.OnSurface
    )

    // Placeholder de inputs
    val InputPlaceholder = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = TijziColors.OnSurfaceVariant
    )

    // Código OTP (números grandes)
    val OtpCode = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurface
    )

    // Instrucciones y mensajes informativos
    val BodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant
    )

    // Contador de tiempo
    val CountdownTimer = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.sp,
        color = TijziColors.OnSurfaceVariant
    )

    // Mensajes de error
    val ErrorText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = TijziColors.Error
    )

    // Texto legal pequeño
    val Caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant
    )

    // Link dentro del texto legal
    val CaptionLink = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = TijziColors.Primary
    )
}

// Material 3 Typography para compatibilidad
val TijziMaterial3Typography = Typography(
    displayLarge = TijziTypography.LogoTitle,
    headlineMedium = TijziTypography.WelcomeSubtitle,
    titleLarge = TijziTypography.ButtonText,
    titleMedium = TijziTypography.InputLabel,
    bodyLarge = TijziTypography.InputText,
    bodyMedium = TijziTypography.BodyMedium,
    labelLarge = TijziTypography.ButtonText,
    labelMedium = TijziTypography.InputLabel,
    labelSmall = TijziTypography.Caption
)