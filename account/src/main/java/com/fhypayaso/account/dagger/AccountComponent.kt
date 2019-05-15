package com.fhypayaso.account.dagger

import com.fhypayaso.account.ui.LoginActivity

/**
 * @author fhyPayaso
 * @since 2019/5/15 1:40 PM
 */

interface AccountComponent {

    fun inject(activity: LoginActivity)
}