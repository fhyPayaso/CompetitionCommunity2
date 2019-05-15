package com.fhypayaso.launch

import android.app.Application
import com.fhypayaso.launch.task.ARouterTask
import com.fhypayaso.launch.task.DaggerTask
import com.fhypayaso.launch.task.RetrofitTask

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:53 PM
 */
class App : Application() {

    private val mTaskList: List<LaunchTask> = listOf(
        ARouterTask(),
        DaggerTask(),
        RetrofitTask()
    )

    override fun onCreate() {
        super.onCreate()
        mTaskList.forEach { it.runTask(this) }
    }
}