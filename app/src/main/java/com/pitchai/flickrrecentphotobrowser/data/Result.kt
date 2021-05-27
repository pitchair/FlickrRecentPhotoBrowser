package com.pitchai.flickrrecentphotobrowser.data

sealed class Result<out T> {
    object Loading : Result<Nothing>()

    data class Success<out T>(
        val value: T
    ) : Result<T>()

    data class Error(
        val error: Throwable
    ) : Result<Nothing>()

    val isLoading get() = this is Loading
    val isFail get() = (this as? Error)?.error
    val valueOrNull get() = (this as? Success)?.value
}