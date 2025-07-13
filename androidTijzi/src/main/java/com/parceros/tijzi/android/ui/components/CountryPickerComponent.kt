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
    Column(modifier = modifier) {
        // Selected country display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() },
            colors = CardDefaults.cardColors(
                containerColor = TijziTheme.colors.Surface
            ),
            shape = RoundedCornerShape(TijziTheme.dimensions.InputRadius)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TijziTheme.dimensions.InputPaddingHorizontal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCountry?.nameEs ?: "Selecciona tu país",
                    style = TijziTheme.typography.InputText,
                    color = if (selectedCountry != null)
                        TijziTheme.colors.OnSurface
                    else
                        TijziTheme.colors.OnSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isOpen) "Cerrar lista" else "Abrir lista",
                    tint = TijziTheme.colors.OnSurfaceVariant
                )
            }
        }

        // Dropdown list
        if (isOpen && countries.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = TijziTheme.dimensions.CountryPickerMaxHeight),
                colors = CardDefaults.cardColors(
                    containerColor = TijziTheme.colors.Surface
                ),
                shape = RoundedCornerShape(TijziTheme.dimensions.InputRadius)
            ) {
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
                if (isSelected) TijziTheme.colors.Primary.copy(alpha = 0.1f)
                else TijziTheme.colors.Surface
            )
            .padding(TijziTheme.dimensions.InputPaddingHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Country flag placeholder
        Box(
            modifier = Modifier
                .size(TijziTheme.dimensions.CountryFlagSize)
                .background(
                    TijziTheme.colors.Primary.copy(alpha = 0.2f),
                    RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = country.isoCode.take(2),
                style = TijziTheme.typography.Caption.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TijziTheme.colors.Primary
            )
        }

        Spacer(modifier = Modifier.width(TijziTheme.dimensions.SpaceM))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = country.nameEs,
                style = TijziTheme.typography.InputText,
                color = TijziTheme.colors.OnSurface
            )
        }

        Text(
            text = country.phoneCode,
            style = TijziTheme.typography.InputText.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TijziTheme.colors.Primary
        )
    }
}