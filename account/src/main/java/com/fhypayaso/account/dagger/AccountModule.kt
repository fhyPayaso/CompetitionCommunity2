package com.fhypayaso.account.dagger

import com.fhypayaso.accountapi.AccountService
import dagger.Module
import dagger.Provides

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:42 PM
 */
@Module
class AccountModule {

    @Provides
    fun provideAccountService(): AccountService {
        return AccountServiceImpl()
    }

}