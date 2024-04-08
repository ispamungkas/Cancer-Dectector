package com.dicoding.asclepius.utils

sealed class Resources <T> (
    val data: T? = null,
    val message: String? = null
){

    class Success<T>(datas: T) : Resources<T>(datas)
    class Failed<T>(errorMessage: String) : Resources<T>(null, errorMessage)
    class Loading<T> : Resources<T>()

}