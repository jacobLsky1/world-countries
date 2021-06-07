package com.jacoblip.andriod.worldcountriestest.utilities

//resource class helps you navagate error and success in network requests
//it a generic class
//and it is sealed which means that you can pick what classes inherit from the class
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}