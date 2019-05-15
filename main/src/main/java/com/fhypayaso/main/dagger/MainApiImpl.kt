package com.fhypayaso.main.dagger

import android.content.Context
import com.fhypayaso.main.MainActivity
import com.fhypayaso.mainapi.MainService

/**
 * @author fhyPayaso
 * @since 2019/5/15 12:13 PM
 */
class MainApiImpl : MainService {

    override fun startMainActivity(context: Context) {
        MainActivity.start(context)
    }
}