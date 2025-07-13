// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/LoadingComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.parceros.tijzi.android.ui.theme.TijziTheme

@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(TijziTheme.colors.Overlay),  // 🔥 OVERLAY OSCURO
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = TijziTheme.colors.Surface  // 🔥 NUEVA SUPERFICIE
            ),
            shape = RoundedCornerShape(16.dp)  // TijziTheme.dimensions.CardRadius
        ) {
            Column(
                modifier = Modifier.padding(32.dp),  // TijziTheme.dimensions.SpaceXL
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = TijziTheme.colors.Primary  // 🔥 ROJO TIJZI para el loading
                )
                Spacer(modifier = Modifier.height(16.dp))  // TijziTheme.dimensions.SpaceM
                Text(
                    text = "Enviando código...",
                    style = TijziTheme.typography.BodyMedium.copy(
                        color = Color.White  // 🔥 TEXTO BLANCO
                    )
                )
            }
        }
    }
}