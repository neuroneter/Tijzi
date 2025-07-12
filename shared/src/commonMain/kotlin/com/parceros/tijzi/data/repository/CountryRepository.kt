package com.parceros.tijzi.data.repository // O el paquete donde realmente está

import com.parceros.tijzi.domain.model.Country
import com.parceros.tijzi.util.Result // IMPORTA TU CLASE RESULT
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    // V V V V V ESTA ES LA LÍNEA QUE DEBES CAMBIAR/ASEGURAR V V V V V
    fun getCountries(): Flow<Result<List<Country>>>
}