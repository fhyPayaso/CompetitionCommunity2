package com.fhypayaso.launch.task

import android.app.Application
import com.fhypayaso.core.Graph
import com.fhypayaso.launch.DaggerAppComponent
import com.fhypayaso.launch.LaunchTask

/**
 * @author fhyPayaso
 * @since 2019/5/14 8:59 PM
 */
class DaggerTask : LaunchTask {
    override fun runTask(app: Application) {
        Graph.getInstance().init(DaggerAppComponent.create())
    }
}