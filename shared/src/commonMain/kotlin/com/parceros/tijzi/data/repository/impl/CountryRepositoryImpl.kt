// shared/src/commonMain/kotlin/com/parceros/tijzi/data/repository/impl/CountryRepositoryImpl.kt
package com.parceros.tijzi.data.repository.impl

import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.data.remote.dto.CountryDto
import com.parceros.tijzi.domain.model.Country
import com.parceros.tijzi.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepositoryImpl(private val httpClient: HttpClient) : CountryRepository {

    private val countriesJsonUrl = "https://firebasestorage.googleapis.com/v0/b/tijzi-e129d.firebasestorage.app/o/contryList.json?alt=media&token=5a202381-07d3-48b4-b087-ae18369a3a55"

    override fun getCountries(): Flow<Result<List<Country>>> = flow {
        try {
            // Obtener los DTOs de la API
            val countryDtos = httpClient.get(countriesJsonUrl).body<List<CountryDto>>()

            // Mapear de DTO a modelo de dominio - usando los datos que vienen de la API
            val countries = countryDtos.map { dto ->
                Country(
                    nameEs = dto.nameEs,
                    nameEn = dto.nameEn,
                    isoCode = dto.isoCode, // ✅ Usar el que viene de la API
                    phoneCode = dto.phoneCode,
                    numberLength = dto.numberLength,
                    customRegexPattern = dto.customRegexPattern ?: generateSimpleRegexFromLengthRule(dto.numberLength)
                )
            }

            emit(Result.Success(countries))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    private fun generateSimpleRegexFromLengthRule(rule: String?): String? {
        if (rule == null) return null
        return when {
            rule.matches(Regex("^\\d+$")) -> "^\\d{$rule}$"
            rule.matches(Regex("^\\d+,\\d+$")) -> {
                val parts = rule.split(',')
                if (parts.size == 2) "^\\d{${parts[0]},${parts[1]}}$" else null
            }
            else -> null
        }
    }
}