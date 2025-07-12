package com.parceros.tijzi.data.repository.impl // O el paquete donde realmente está

import com.parceros.tijzi.data.repository.CountryRepository
import com.parceros.tijzi.domain.model.Country
import com.parceros.tijzi.util.Result // IMPORTA TU CLASE RESULT
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepositoryImpl(private val httpClient: HttpClient) : CountryRepository {

    private val countriesJsonUrl = "https://firebasestorage.googleapis.com/v0/b/tijzi-e129d.firebasestorage.app/o/contryList.json?alt=media&token=5a202381-07d3-48b4-b087-ae18369a3a55"

    override fun getCountries(): Flow<Result<List<Country>>> = flow { // <-- Verifica esta línea
        try {
            val countries = httpClient.get(countriesJsonUrl).body<List<Country>>()
            emit(Result.Success(countries))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    // ... tu función generateSimpleRegexFromLengthRule
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