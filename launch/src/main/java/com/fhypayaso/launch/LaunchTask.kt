package com.fhypayaso.launch

import android.app.Application

/**
 * @author fhyPayaso
 * @since 2019/5/14 8:57 PM
 */
interface LaunchTask {

    fun runTask(app: Application)
}