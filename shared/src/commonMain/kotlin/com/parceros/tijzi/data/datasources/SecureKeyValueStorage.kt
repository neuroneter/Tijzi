package com.parceros.tijzi.data.datasources

// commonMain

expect class SecureKeyValueStorage {

    fun saveString(key: String, value: String) // ¿Se llama así o de otra forma?
    fun getString(key: String): String?       // ¿Se llama así o de otra forma?
    fun removeString(key: String)


}