package com.fhypayaso.launch.task

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.fhypayaso.launch.LaunchTask

/**
 * @author fhyPayaso
 * @since 2019/5/14 9:00 PM
 */
class ARouterTask : LaunchTask {

    override fun runTask(app: Application) {
        ARouter.init(app)
    }
}