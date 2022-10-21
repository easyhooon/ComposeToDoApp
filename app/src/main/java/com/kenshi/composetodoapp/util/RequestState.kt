package com.kenshi.composetodoapp.util

//기본값을 0으로 두지 않고 상태로 두어서 EmptyScreen 이 잠깐 보이는 이슈 해결
sealed class RequestState<out T> {
    object Idle : RequestState<Nothing>()
    object Loading : RequestState<Nothing>()
    data class Success<T>(val data: T) : RequestState<T>()
    data class Error(val error: Throwable) : RequestState<Nothing>()
}
