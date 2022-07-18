package com.example.weather.util

import com.example.weather.data.Result
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): Result.ResponseError? {
        val converter =
            retrofit.responseBodyConverter<Result.ResponseError>(
                Result.ResponseError::class.java,
                arrayOfNulls(0)
            )

        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Result.ResponseError()
        }
    }
}
