// androidTijzi/src/main/java/com/parceros/tijzi/android/di/UiModule.kt
package com.parceros.tijzi.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parceros.tijzi.data.datasources.SecureKeyValueStorage
import com.parceros.tijzi.presentation.viewmodel.AuthViewModel
import com.parceros.tijzi.presentation.viewmodel.AuthViewModelFactory

/**
 * Wrapper para integrar el AuthViewModel compartido con Android ViewModels
 */
class AndroidAuthViewModel(
    private val sharedAuthViewModel: AuthViewModel
) : ViewModel() {

    // Delegar todo al ViewModel compartido
    val uiState = sharedAuthViewModel.uiState
    val navigationEvents = sharedAuthViewModel.navigationEvents

    fun onEvent(event: com.parceros.tijzi.presentation.model.AuthEvent) {
        sharedAuthViewModel.onEvent(event)
    }

    override fun onCleared() {
        super.onCleared()
        sharedAuthViewModel.onCleared()
    }
}

// Factory para crear ViewModels Android
class AndroidAuthViewModelFactory(
    private val secureKeyValueStorage: SecureKeyValueStorage
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AndroidAuthViewModel::class.java -> {
                // Crear el ViewModel compartido con un scope temporal
                // En una implementación real, usarías el viewModelScope apropiado
                val sharedViewModel = AuthViewModelFactory.create(
                    secureKeyValueStorage = secureKeyValueStorage,
                    scope = kotlinx.coroutines.MainScope()
                )
                AndroidAuthViewModel(sharedViewModel) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

// Objeto para crear fácilmente el factory desde Activity
object UiModule {

    fun createAndroidAuthViewModelFactory(
        secureKeyValueStorage: SecureKeyValueStorage
    ): AndroidAuthViewModelFactory {
        return AndroidAuthViewModelFactory(secureKeyValueStorage)
    }
}