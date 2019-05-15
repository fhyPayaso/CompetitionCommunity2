package com.fhypayaso.launch

import com.fhypayaso.account.dagger.AccountComponent
import com.fhypayaso.account.dagger.AccountModule
import com.fhypayaso.main.dagger.MainComponent
import com.fhypayaso.main.dagger.MainModule
import dagger.Component

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:54 PM
 */
@Component(modules = [AccountModule::class, MainModule::class])
interface AppComponent : MainComponent, AccountComponent {

}