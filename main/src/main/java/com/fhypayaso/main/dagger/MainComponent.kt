package com.fhypayaso.main.dagger

import com.fhypayaso.main.MainActivity

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:44 PM
 */
interface MainComponent {

    fun inject(activity: MainActivity)
}