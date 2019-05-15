package com.fhypayaso.core

/**
 * @author fhyPayaso
 * @since 2019/5/15 11:00 AM
 */
data class ApiResponse<T>(

    val code: Int,
    val data: T,
    val msg: String
)