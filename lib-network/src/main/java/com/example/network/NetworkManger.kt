package com.example.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.HashMap

/**
 * @author fhyPayaso
 * @since 2019/5/14 9:42 PM
 */
class NetworkManger private constructor() {

    private lateinit var mRetrofit: Retrofit

    private val mServiceCache: HashMap<String?, Any> = HashMap()

    companion object {
        const val BASE_URL = "localtest:8080"
        val inst = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = NetworkManger()
    }


    fun init() {
        mRetrofit = getRetrofitClient()
    }


    private fun getRetrofitClient() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build()

    private fun getOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().also {
            it.level = if (BuildConfig.DEBUG) BODY else NONE
        })
        .build()

    fun <T : Any> createService(service: Class<T>): T {
        if (mServiceCache.containsKey(service.canonicalName)) {
            return mServiceCache[service.canonicalName] as T
        }
        val retrofitService = mRetrofit.create(service)
        mServiceCache[service.canonicalName] = retrofitService
        return service as T
    }


}