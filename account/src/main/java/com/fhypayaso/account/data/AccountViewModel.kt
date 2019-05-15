package com.fhypayaso.account.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.network.NetworkManger
import com.fhypayaso.core.ApiResponse
import com.fhypayaso.core.RxViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author fhyPayaso
 * @since 2019/5/15 10:12 AM
 */
class AccountViewModel : RxViewModel() {

    private var mApi: AccountApi =
        NetworkManger.inst.createService(AccountApi::class.java)

    private val loginLiveData: MutableLiveData<ApiResponse<Unit>> = MutableLiveData()


    fun login(phone: String, password: String) {
        register(mApi.login(phone, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { loginLiveData.postValue(it) },
                { loginLiveData.postValue(null) }
            ))
    }

    fun getLoginLiveData(): LiveData<ApiResponse<Unit>> {
        return loginLiveData
    }


}