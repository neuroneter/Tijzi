// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/LoadingComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.parceros.tijzi.android.ui.theme.TijziTheme

@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(TijziTheme.colors.Overlay),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = TijziTheme.colors.Surface
            ),
            shape = RoundedCornerShape(TijziTheme.dimensions.CardRadius)
        ) {
            Column(
                modifier = Modifier.padding(TijziTheme.dimensions.SpaceXL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = TijziTheme.colors.Primary
                )
                Spacer(modifier = Modifier.height(TijziTheme.dimensions.SpaceM))
                Text(
                    text = "Enviando código...",
                    style = TijziTheme.typography.BodyMedium,
                    color = TijziTheme.colors.OnSurface
                )
            }
        }
    }
}