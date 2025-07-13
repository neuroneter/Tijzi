// androidTijzi/src/main/java/com/parceros/tijzi/android/ui/navigation/AuthNavigation.kt
package com.parceros.tijzi.android.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.parceros.tijzi.android.di.AndroidAuthViewModel
import com.parceros.tijzi.android.ui.screens.auth.LoginScreen  // 🔥 IMPORT AGREGADO
import com.parceros.tijzi.presentation.model.AuthNavigationEvent

// Rutas de navegación
object AuthRoutes {
    const val LOGIN = "login"
    const val OTP_VERIFICATION = "otp_verification"
    const val RESEND_CODE = "resend_code"
    const val WELCOME = "welcome"
}

@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AndroidAuthViewModel
) {
    val uiState by authViewModel.uiState.collectAsState()

    // Escuchar eventos de navegación del ViewModel
    LaunchedEffect(authViewModel) {
        authViewModel.navigationEvents.collect { event ->
            when (event) {
                AuthNavigationEvent.NavigateToLogin -> {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                    }
                }
                AuthNavigationEvent.NavigateToOtpVerification -> {
                    navController.navigate(AuthRoutes.OTP_VERIFICATION)
                }
                AuthNavigationEvent.NavigateToResendCode -> {
                    navController.navigate(AuthRoutes.RESEND_CODE)
                }
                AuthNavigationEvent.NavigateToWelcome -> {
                    navController.navigate(AuthRoutes.WELCOME) {
                        popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                    }
                }
                AuthNavigationEvent.NavigateBack -> {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
                AuthNavigationEvent.ShowError -> {
                    // TODO: Mostrar error dialog o snackbar
                }
                is AuthNavigationEvent.ShowToast -> {
                    // TODO: Mostrar toast
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.LOGIN
    ) {
        composable(AuthRoutes.LOGIN) {
            // 🔥 AQUÍ ESTABA EL PROBLEMA - Ahora usa LoginScreen real
            LoginScreen(
                uiState = uiState,
                onEvent = authViewModel::onEvent
            )
        }

        composable(AuthRoutes.OTP_VERIFICATION) {
            // TODO: Implementar OtpVerificationScreen
            TemporaryScreen(
                title = "OTP Verification",
                content = """
                    Estado OTP:
                    • Code: ${uiState.otpCode}
                    • Valid: ${uiState.isOtpValid}
                    • Countdown: ${uiState.countdown}s
                    • Can request new: ${uiState.canRequestNewOtp}
                """.trimIndent()
            )
        }

        composable(AuthRoutes.RESEND_CODE) {
            // TODO: Implementar ResendCodeScreen
            TemporaryScreen(
                title = "Resend Code",
                content = """
                    Opciones de reenvío:
                    • Selected channel: ${uiState.selectedChannel?.displayName ?: "None"}
                    • Countries: ${uiState.countries.size} loaded
                """.trimIndent()
            )
        }

        composable(AuthRoutes.WELCOME) {
            // TODO: Implementar WelcomeScreen
            TemporaryScreen(
                title = "¡Bienvenido a Tijzi!",
                content = """
                    ✅ Login exitoso!
                    
                    Usuario autenticado:
                    • Phone: ${uiState.phoneNumber}
                    • Country: ${uiState.selectedCountry?.nameEs ?: "Unknown"}
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun TemporaryScreen(
    title: String,
    content: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}