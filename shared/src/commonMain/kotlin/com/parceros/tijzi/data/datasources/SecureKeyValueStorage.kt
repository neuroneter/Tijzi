package com.parceros.tijzi.data.datasources

expect class SecureKeyValueStorage {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
    fun removeString(key: String)
    fun clearAll()
}