package com.fhypayaso.account.data

import com.fhypayaso.core.ApiResponse
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author fhyPayaso
 * @since 2019/5/14 10:17 PM
 */
interface AccountApi {

    @POST("account/login")
    fun login(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Observable<ApiResponse<Unit>>


    @POST("account/register")
    fun register(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Observable<ApiResponse<Unit>>


}