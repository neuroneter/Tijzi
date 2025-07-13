// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/ChannelButtonComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parceros.tijzi.android.ui.theme.TijziTheme
import com.parceros.tijzi.presentation.model.OtpChannel  // 🔥 IMPORT CORRECTO

@Composable
fun ChannelButtonComponent(
    channel: OtpChannel,
    isSelected: Boolean,
    onChannelSelected: (OtpChannel) -> Unit,
    onSendOtp: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val channelColor = when (channel) {
        OtpChannel.WHATSAPP -> TijziTheme.colors.WhatsApp
        OtpChannel.SMS -> TijziTheme.colors.SMS
        OtpChannel.TELEGRAM -> TijziTheme.colors.Telegram
    }

    Button(
        onClick = {
            onChannelSelected(channel)
            if (isSelected) {
                onSendOtp()
            }
        },
        modifier = modifier.height(TijziTheme.dimensions.ButtonHeight),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = channelColor,
            contentColor = TijziTheme.colors.OnPrimary,
            disabledContainerColor = TijziTheme.colors.Disabled,
            disabledContentColor = TijziTheme.colors.OnSurfaceVariant
        ),
        shape = RoundedCornerShape(TijziTheme.dimensions.ButtonRadius)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Channel icon placeholder (you can add actual icons later)
            Box(
                modifier = Modifier
                    .size(TijziTheme.dimensions.IconSizeMedium)
                    .background(
                        TijziTheme.colors.OnPrimary.copy(alpha = 0.2f),
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (channel) {
                        OtpChannel.WHATSAPP -> "W"
                        OtpChannel.SMS -> "S"
                        OtpChannel.TELEGRAM -> "T"
                    },
                    style = TijziTheme.typography.ButtonText.copy(fontSize = 12.sp),
                    color = channelColor
                )
            }

            Spacer(modifier = Modifier.width(TijziTheme.dimensions.SpaceM))

            Text(
                text = "Enviar a ${channel.displayName}",  // 🔥 ESTO AHORA FUNCIONARÁ
                style = TijziTheme.typography.ButtonText
            )
        }
    }
}