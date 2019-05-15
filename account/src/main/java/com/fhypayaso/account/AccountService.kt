package com.fhypayaso.account

import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author fhyPayaso
 * @since 2019/5/14 10:17 PM
 */
interface AccountService {

    @POST
    fun login(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Observable<String>


    @POST
    fun register(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Observable<String>


}