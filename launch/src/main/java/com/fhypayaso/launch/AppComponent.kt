package com.fhypayaso.launch

import com.fhypayaso.account.dagger.AccountModule
import com.fhypayaso.main.dagger.MainComponent
import dagger.Component

/**
 *
 * @author fanhongyu
 * @since 2019/5/10 5:54 PM
 */
@Component(modules = [AccountModule::class])
interface AppComponent : MainComponent{

}