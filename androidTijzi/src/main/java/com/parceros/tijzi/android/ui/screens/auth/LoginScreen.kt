// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/screens/auth/LoginScreen.kt
package com.parceros.tijzi.android.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parceros.tijzi.android.ui.components.CountryPickerComponent
import com.parceros.tijzi.android.ui.components.PhoneInputComponent
import com.parceros.tijzi.android.ui.components.ChannelButtonComponent
import com.parceros.tijzi.android.ui.components.LoadingComponent
import com.parceros.tijzi.android.ui.theme.TijziTheme
import com.parceros.tijzi.presentation.model.AuthEvent
import com.parceros.tijzi.presentation.model.AuthUiState
import com.parceros.tijzi.presentation.model.OtpChannel

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TijziTheme.colors.Background)
    ) {
        // Loading overlay
        if (uiState.isLoading) {
            LoadingComponent(
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(TijziTheme.dimensions.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(TijziTheme.dimensions.ScreenPaddingTop))

            // Header - Logo y mensaje motivacional
            LoginHeader()

            Spacer(modifier = Modifier.height(48.dp)) // 🔥 FIJO: Usar valor directo

            // Form - Country picker + Phone input + Channels
            LoginForm(
                uiState = uiState,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(48.dp)) // 🔥 FIJO: Usar valor directo

            // Footer - Texto legal
            LoginFooter()

            Spacer(modifier = Modifier.height(TijziTheme.dimensions.ScreenPaddingBottom))
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Tijzi
        Text(
            text = "Tijzi",
            style = TijziTheme.typography.LogoTitle.copy(
                color = TijziTheme.colors.Primary,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp)) // 🔥 FIJO: Usar valor directo

        // Mensaje motivacional
        Text(
            text = "¡Entra al mundo de tu libertad,\nexplora, disfruta y desea!",
            style = TijziTheme.typography.WelcomeSubtitle,
            textAlign = TextAlign.Center,
            color = TijziTheme.colors.OnSurfaceVariant
        )
    }
}

@Composable
private fun LoginForm(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Country Picker
        CountryPickerComponent(
            selectedCountry = uiState.selectedCountry,
            countries = uiState.countries,
            isOpen = uiState.isCountryPickerOpen,
            onCountrySelected = { country ->
                onEvent(AuthEvent.CountrySelected(country))
            },
            onToggle = {
                onEvent(AuthEvent.ToggleCountryPicker)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp)) // 🔥 FIJO: Usar valor directo

        // Phone Input
        PhoneInputComponent(
            phoneNumber = uiState.phoneNumber,
            countryCode = uiState.selectedCountry?.phoneCode ?: "",
            onPhoneNumberChange = { phoneNumber ->
                onEvent(AuthEvent.PhoneNumberChanged(phoneNumber))
            },
            isValid = uiState.isPhoneValid,
            errorMessage = uiState.phoneError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp)) // 🔥 FIJO: Usar valor directo

        // Channel Buttons
        ChannelButtons(
            selectedChannel = uiState.selectedChannel,
            onChannelSelected = { channel ->
                onEvent(AuthEvent.ChannelSelected(channel))
            },
            onSendOtp = {
                onEvent(AuthEvent.RequestOtp)
            },
            isEnabled = uiState.isPhoneValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        // Error message
        val errorMessage = uiState.error // 🔥 FIJO: Extraer a variable
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(TijziTheme.dimensions.SpaceM))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = TijziTheme.colors.Error.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(TijziTheme.dimensions.CardRadius)
            ) {
                Text(
                    text = errorMessage, // 🔥 FIJO: Usar variable
                    style = TijziTheme.typography.ErrorText,
                    modifier = Modifier.padding(TijziTheme.dimensions.SpaceM),
                    color = TijziTheme.colors.Error
                )
            }
        }
    }
}

@Composable
private fun ChannelButtons(
    selectedChannel: OtpChannel?,
    onChannelSelected: (OtpChannel) -> Unit,
    onSendOtp: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // WhatsApp Button
        ChannelButtonComponent(
            channel = OtpChannel.WHATSAPP,
            isSelected = selectedChannel == OtpChannel.WHATSAPP,
            onChannelSelected = onChannelSelected,
            onSendOtp = onSendOtp,
            isEnabled = isEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp)) // 🔥 FIJO: Usar valor directo

        // SMS Button
        ChannelButtonComponent(
            channel = OtpChannel.SMS,
            isSelected = selectedChannel == OtpChannel.SMS,
            onChannelSelected = onChannelSelected,
            onSendOtp = onSendOtp,
            isEnabled = isEnabled,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp)) // 🔥 FIJO: Usar valor directo

        // Telegram Button
        ChannelButtonComponent(
            channel = OtpChannel.TELEGRAM,
            isSelected = selectedChannel == OtpChannel.TELEGRAM,
            onChannelSelected = onChannelSelected,
            onSendOtp = onSendOtp,
            isEnabled = isEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoginFooter() {
    Text(
        text = "Al ingresar confirmas que eres mayor de edad y te haces responsable por todas tus interacciones, visita AQUÍ",
        style = TijziTheme.typography.Caption,
        textAlign = TextAlign.Center,
        color = TijziTheme.colors.OnSurfaceVariant,
        modifier = Modifier.padding(horizontal = TijziTheme.dimensions.SpaceM)
    )
}