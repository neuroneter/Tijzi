// androidTijzi/src/main/java/com/parceros/tijzi/android/MainActivity.kt
package com.parceros.tijzi.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.parceros.tijzi.android.di.UiModule
import com.parceros.tijzi.android.di.AndroidAuthViewModel
import com.parceros.tijzi.android.ui.navigation.AuthNavigation
import com.parceros.tijzi.android.ui.theme.TijziTheme
import com.parceros.tijzi.data.datasources.SecureKeyValueStorage

class MainActivity : ComponentActivity() {

    // Crear SecureKeyValueStorage para Android
    private val secureKeyValueStorage by lazy {
        SecureKeyValueStorage(this)
    }

    // Crear AndroidAuthViewModel usando nuestro factory
    private val authViewModel: AndroidAuthViewModel by viewModels {
        UiModule.createAndroidAuthViewModelFactory(secureKeyValueStorage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TijziTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation(authViewModel = authViewModel)
                }
            }
        }
    }
}