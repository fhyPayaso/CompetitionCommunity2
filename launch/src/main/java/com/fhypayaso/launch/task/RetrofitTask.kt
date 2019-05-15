package com.fhypayaso.launch.task

import android.app.Application
import com.example.network.NetworkManger
import com.fhypayaso.launch.LaunchTask

/**
 * @author fhyPayaso
 * @since 2019/5/14 9:03 PM
 */
class RetrofitTask : LaunchTask {

    override fun runTask(app: Application) {
        NetworkManger.inst.init()
    }
}