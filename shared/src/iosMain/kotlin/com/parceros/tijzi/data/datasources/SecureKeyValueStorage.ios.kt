package com.parceros.tijzi.data.datasources

import platform.Foundation.*
import platform.CoreFoundation.*
import platform.Security.*
import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
actual class SecureKeyValueStorage() {

    private fun createQuery(key: String): CFMutableDictionaryRef? {
        val query = CFDictionaryCreateMutable(null, 0, null, null)
        CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, key, kCFStringEncodingUTF8))
        return query
    }

    actual fun saveString(key: String, value: String) {
        val query = createQuery(key) ?: return

        // Eliminar entrada existente
        SecItemDelete(query)

        // Añadir nueva entrada
        val valueData = value.encodeToByteArray()
        val cfData = CFDataCreate(null, valueData.asUByteArray().toCValues(), valueData.size.toLong())
        CFDictionarySetValue(query, kSecValueData, cfData)

        SecItemAdd(query, null)
        CFRelease(query)
        CFRelease(cfData)
    }

    actual fun getString(key: String): String? {
        val query = createQuery(key) ?: return null
        CFDictionarySetValue(query, kSecReturnData, kCFBooleanTrue)

        return memScoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)
            CFRelease(query)

            if (status == errSecSuccess) {
                val data = result.value as CFDataRef?
                if (data != null) {
                    val length = CFDataGetLength(data).toInt()
                    val bytes = CFDataGetBytePtr(data)
                    val byteArray = bytes?.readBytes(length)
                    CFRelease(data)
                    byteArray?.decodeToString()
                } else null
            } else null
        }
    }

    actual fun removeString(key: String) {
        val query = createQuery(key) ?: return
        SecItemDelete(query)
        CFRelease(query)
    }

    actual fun clearAll() {
        val query = CFDictionaryCreateMutable(null, 0, null, null)
        if (query != null) {
            CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
            SecItemDelete(query)
            CFRelease(query)
        }
    }
}