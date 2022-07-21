package com.example.weather.util

import com.example.weather.model.Result
import retrofit2.Response
import retrofit2.Retrofit

fun Response<*>.asResponseError(retrofit: Retrofit): Result.ResponseError? {
    val converter = retrofit.responseBodyConverter<Result.ResponseError>(
        Result.ResponseError::class.java,
        arrayOfNulls(0)
    )

    return try {
        converter.convert(this.errorBody()!!)
    } catch (e: Exception) {
        Result.ResponseError()
    }
}
