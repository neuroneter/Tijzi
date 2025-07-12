package com.parceros.tijzi.data.repository

import com.parceros.tijzi.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import com.parceros.tijzi.util.Result

interface AuthRepository {

    /**
     * Solicita al backend que envíe un código OTP al número de teléfono proporcionado.
     * @param countryCode El código del país (ej. "+57").
     * @param phoneNumber El número de teléfono (sin el código de país).
     * @return Un Result que indica éxito (Unit) o fallo (Exception).
     */
    fun requestOtp(countryCode: String, phoneNumber: String): Flow<Result<Unit>>

    /**
     * Verifica el código OTP ingresado por el usuario con el backend.
     * @param countryCode El código del país.
     * @param phoneNumber El número de teléfono.
     * @param otp El código OTP de 6 dígitos ingresado por el usuario.
     * @return Un Result que contiene la UserSession (con el token) si es exitoso, o un fallo (Exception).
     */
    fun verifyOtp(countryCode: String, phoneNumber: String, otp: String): Flow<Result<UserSession>>

}