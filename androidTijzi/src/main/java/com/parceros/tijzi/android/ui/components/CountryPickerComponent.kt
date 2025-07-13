// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/components/CountryPickerComponent.kt
package com.parceros.tijzi.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.parceros.tijzi.android.ui.theme.TijziTheme
import com.parceros.tijzi.domain.model.Country

@Composable
fun CountryPickerComponent(
    selectedCountry: Country?,
    countries: List<Country>,
    isOpen: Boolean,
    onCountrySelected: (Country) -> Unit,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerRadius = 12.dp

    Column(modifier = modifier) {
        // Selected country display with dynamic corners
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
            colors = CardDefaults.cardColors(
                containerColor = TijziTheme.colors.Surface
            ),
            // 🔥 ESQUINAS DINÁMICAS: Si está abierto, solo esquinas superiores redondeadas
            shape = if (isOpen) {
                RoundedCornerShape(
                    topStart = cornerRadius,
                    topEnd = cornerRadius,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            } else {
                // Si está cerrado, todas las esquinas redondeadas
                RoundedCornerShape(cornerRadius)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCountry?.nameEs ?: "Selecciona tu país",
                    style = TijziTheme.typography.InputText.copy(
                        color = if (selectedCountry != null)
                            Color.White
                        else
                            TijziTheme.colors.OnSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isOpen) "Cerrar lista" else "Abrir lista",
                    tint = TijziTheme.colors.OnSurfaceVariant
                )
            }
        }

        // Dropdown list with dynamic corners
        if (isOpen && countries.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = TijziTheme.colors.Surface
                ),
                // 🔥 ESQUINAS DINÁMICAS: Solo esquinas inferiores redondeadas
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = cornerRadius,
                    bottomEnd = cornerRadius
                )
            ) {
                // 🔥 LÍNEA DIVISORIA SUTIL entre el selector y la lista
                Divider(
                    color = TijziTheme.colors.Border,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn {
                    items(countries) { country ->
                        CountryItem(
                            country = country,
                            isSelected = selectedCountry?.phoneCode == country.phoneCode,
                            onSelected = {
                                onCountrySelected(country)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryItem(
    country: Country,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .background(
                if (isSelected)
                    TijziTheme.colors.Primary.copy(alpha = 0.1f)
                else
                    TijziTheme.colors.Surface
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Country flag placeholder
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    TijziTheme.colors.Primary,
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = country.isoCode.take(2),
                style = TijziTheme.typography.Caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = TijziTheme.colors.OnPrimary
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = country.nameEs,
                style = TijziTheme.typography.InputText.copy(
                    color = Color.White
                )
            )
        }

        Text(
            text = country.phoneCode,
            style = TijziTheme.typography.InputText.copy(
                fontWeight = FontWeight.SemiBold,
                color = TijziTheme.colors.OnPrimary
            )
        )
    }
}