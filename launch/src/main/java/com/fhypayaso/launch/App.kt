package com.fhypayaso.launch

import android.app.Application
import com.fhypayaso.core.Graph

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:53 PM
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.getInstance().init(DaggerAppComponent.create())
    }
}