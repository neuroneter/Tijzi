import org.gradle.kotlin.dsl.implementation // Esta importación puede no ser necesaria si usas la función implementation directamente
import org.jetbrains.kotlin.gradle.dsl.JvmTarget // Asegúrate que JvmTarget se resuelve, sino import org.jetbrains.kotlin.gradle.dsl.KotlinJvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8) // O KotlinJvmTarget.JVM_1_8 si JvmTarget no se resuelve
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting { // Usar 'val commonMain by getting' es más idiomático aquí
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)

                // AÑADE ESTA LÍNEA PARA EL MOTOR KTOR CIO
                implementation(libs.ktor.client.cio) // Asumiendo que tienes 'ktor-client-cio' definido en tu libs.versions.toml

                //put your multiplatform dependencies here
            }
        }
        val androidMain by getting { // Usar 'val androidMain by getting'
            dependencies {
                implementation(libs.androidx.security.crypto)
                implementation(libs.ktor.client.android) // Para Android puedes seguir usando el motor Android si prefieres, o CIO también.
                // Si quieres usar CIO para Android también, puedes mover la dependencia de CIO a commonMain.
            }
        }
        val commonTest by getting { // Usar 'val commonTest by getting'
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.parceros.tijzi"
    compileSdk = 35 // Nota: SDK 35 es una versión futura (developer preview). Asegúrate de tenerlo configurado.
    defaultConfig {
        minSdk = 28
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}