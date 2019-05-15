package com.fhypayaso.account.dagger

import android.content.Context
import com.fhypayaso.account.ui.LoginActivity
import com.fhypayaso.accountapi.AccountService

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:32 PM
 */
class AccountApiImpl : AccountService {

    override fun startLoginActivity(context: Context) {
        LoginActivity.start(context)
    }
}