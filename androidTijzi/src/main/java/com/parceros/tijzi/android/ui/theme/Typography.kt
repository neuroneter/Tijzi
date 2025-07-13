// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/theme/Typography.kt
package com.parceros.tijzi.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

object TijziTypography {

    // Logo y títulos principales - SIEMPRE BLANCO
    val LogoTitle = TextStyle(
        fontFamily = TijziFonts.TijziLogo,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 45.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp,
        color = Color.White  // 🔥 BLANCO para logo
    )

    // Subtítulo motivacional - GRIS CLARO
    val WelcomeSubtitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant  // 🔥 GRIS CLARO
    )

    // Texto de botones principales - SIEMPRE BLANCO
    val ButtonText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
        color = Color.White  // 🔥 BLANCO sobre botones rojos
    )

    // Labels de inputs - BLANCO
    val InputLabel = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color = Color.White  // 🔥 BLANCO para labels
    )

    // Texto dentro de inputs - BLANCO
    val InputText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = Color.White  // 🔥 BLANCO para texto de input
    )

    // Placeholder de inputs - GRIS CLARO
    val InputPlaceholder = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = TijziColors.OnSurfaceVariant  // 🔥 GRIS CLARO para placeholders
    )

    // Código OTP (números grandes) - BLANCO
    val OtpCode = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center,
        color = Color.White  // 🔥 BLANCO para códigos OTP
    )

    // Instrucciones y mensajes informativos - GRIS CLARO
    val BodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant  // 🔥 GRIS CLARO
    )

    // Contador de tiempo - GRIS CLARO
    val CountdownTimer = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.sp,
        color = TijziColors.OnSurfaceVariant  // 🔥 GRIS CLARO
    )

    // Mensajes de error - ROJO SOLO PARA ERRORES (NO para texto normal)
    val ErrorText = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = TijziColors.Error  // 🔥 ROJO SOLO para errores
    )

    // Texto legal pequeño - GRIS CLARO
    val Caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = TijziColors.OnSurfaceVariant  // 🔥 GRIS CLARO
    )

    // Link dentro del texto legal - ROJO TIJZI SOLO PARA LINKS
    val CaptionLink = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        color = TijziColors.Primary  // 🔥 ROJO TIJZI solo para enlaces
    )
}

// 🔥 MATERIAL 3 TYPOGRAPHY ACTUALIZADA - SIN COLORES ROJOS EN TEXTO
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