package com.parceros.tijzi.util // Or your preferred utility package

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success<*>
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = if (this is Success<T>) data else null
    fun exceptionOrNull(): Throwable? = if (this is Failure) exception else null
}