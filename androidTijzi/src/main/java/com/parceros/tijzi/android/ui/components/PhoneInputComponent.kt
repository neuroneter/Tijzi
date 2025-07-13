// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/PhoneInputComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    containerColor = TijziTheme.colors.SurfaceVariant  // 🔥 NUEVA SUPERFICIE VARIANTE
                ),
                shape = RoundedCornerShape(12.dp)  // TijziTheme.dimensions.InputRadius
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),  // TijziTheme.dimensions.InputHeight
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = countryCode.ifBlank { "+57" },
                        style = TijziTheme.typography.InputText.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TijziTheme.colors.OnPrimary  // 🔥 ROJO TIJZI para código de país
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))  // TijziTheme.dimensions.SpaceM

            // Phone number input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "3001234567",
                        style = TijziTheme.typography.InputPlaceholder.copy(
                            color = TijziTheme.colors.OnSurfaceVariant  // 🔥 GRIS para placeholder
                        )
                    )
                },
                textStyle = TijziTheme.typography.InputText.copy(
                    color = Color.White  // 🔥 BLANCO para texto ingresado
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    // Colores del borde
                    focusedBorderColor = if (isValid)
                        TijziTheme.colors.Primary  // 🔥 ROJO TIJZI cuando válido y enfocado
                    else
                        TijziTheme.colors.BorderFocused,  // 🔥 BORDE ENFOCADO
                    unfocusedBorderColor = TijziTheme.colors.Border,  // 🔥 BORDE NORMAL
                    errorBorderColor = TijziTheme.colors.Error,  // 🔥 ROJO ERROR

                    // Colores del texto
                    focusedTextColor = Color.White,  // 🔥 BLANCO
                    unfocusedTextColor = Color.White,  // 🔥 BLANCO
                    errorTextColor = Color.White,  // 🔥 BLANCO incluso en error

                    // Color del cursor
                    cursorColor = TijziTheme.colors.Primary,  // 🔥 ROJO TIJZI

                    // Colores del contenedor
                    focusedContainerColor = TijziTheme.colors.Surface,  // 🔥 NUEVA SUPERFICIE
                    unfocusedContainerColor = TijziTheme.colors.Surface,  // 🔥 NUEVA SUPERFICIE
                    errorContainerColor = TijziTheme.colors.Surface  // 🔥 SUPERFICIE incluso en error
                ),
                shape = RoundedCornerShape(12.dp),  // TijziTheme.dimensions.InputRadius
                singleLine = true,
                isError = errorMessage != null
            )
        }

        // Error message
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))  // TijziTheme.dimensions.SpaceXS
            Text(
                text = errorMessage,
                style = TijziTheme.typography.ErrorText  // 🔥 YA TIENE COLOR ROJO PARA ERRORES
            )
        }
    }
}