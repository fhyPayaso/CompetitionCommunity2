package com.fhypayaso.account.dagger

import android.content.Context
import android.content.Intent
import com.fhypayaso.account.LoginActivity
import com.fhypayaso.accountapi.AccountService

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:32 PM
 */
class AccountServiceImpl : AccountService {

    override fun startLoginActivity(context: Context) {
        LoginActivity.start(context)
    }
}