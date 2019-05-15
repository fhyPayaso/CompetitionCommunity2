package com.fhypayaso.main.dagger

import com.fhypayaso.mainapi.MainService
import dagger.Module
import dagger.Provides

/**
 * @author fhyPayaso
 * @since 2019/5/15 1:37 PM
 */
@Module
class MainModule {

    @Provides
    fun provideMainApi(): MainService {
        return MainApiImpl()
    }

}