// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/PhoneInputComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.parceros.tijzi.android.ui.theme.TijziTheme

@Composable
fun PhoneInputComponent(
    phoneNumber: String,
    countryCode: String,
    onPhoneNumberChange: (String) -> Unit,
    isValid: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Country code display
            Card(
                modifier = Modifier.width(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = TijziTheme.colors.SurfaceVariant
                ),
                shape = RoundedCornerShape(TijziTheme.dimensions.InputRadius)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TijziTheme.dimensions.InputHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = countryCode.ifBlank { "+57" },
                        style = TijziTheme.typography.InputText.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TijziTheme.colors.Primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(TijziTheme.dimensions.SpaceM))

            // Phone number input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "3001234567",
                        style = TijziTheme.typography.InputPlaceholder
                    )
                },
                textStyle = TijziTheme.typography.InputText,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isValid) TijziTheme.colors.Primary else TijziTheme.colors.BorderFocused,
                    unfocusedBorderColor = TijziTheme.colors.Border,
                    errorBorderColor = TijziTheme.colors.Error,
                    focusedTextColor = TijziTheme.colors.OnSurface,
                    unfocusedTextColor = TijziTheme.colors.OnSurface,
                    cursorColor = TijziTheme.colors.Primary,
                    focusedContainerColor = TijziTheme.colors.Surface,
                    unfocusedContainerColor = TijziTheme.colors.Surface
                ),
                shape = RoundedCornerShape(TijziTheme.dimensions.InputRadius),
                singleLine = true,
                isError = errorMessage != null
            )
        }

        // Error message
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(TijziTheme.dimensions.SpaceXS))
            Text(
                text = errorMessage,
                style = TijziTheme.typography.ErrorText,
                color = TijziTheme.colors.Error
            )
        }
    }
}