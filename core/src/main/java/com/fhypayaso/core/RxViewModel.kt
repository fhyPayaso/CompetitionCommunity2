package com.fhypayaso.core

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author fhyPayaso
 * @since 2019/5/15 10:34 AM
 */
abstract class RxViewModel : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun register(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}