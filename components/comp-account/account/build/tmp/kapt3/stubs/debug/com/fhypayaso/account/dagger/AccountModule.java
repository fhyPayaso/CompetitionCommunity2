package com.fhypayaso.account.dagger;

import java.lang.System;

/**
 * *
 * * @author fanhongyu
 * * @since 2019/5/10 5:42 PM
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007\u00a8\u0006\u0005"}, d2 = {"Lcom/fhypayaso/account/dagger/AccountModule;", "", "()V", "provideAccountService", "Lcom/fhypayaso/accountapi/AccountService;", "account_debug"})
@dagger.Module()
public final class AccountModule {
    
    @org.jetbrains.annotations.NotNull()
    @dagger.Provides()
    public final com.fhypayaso.accountapi.AccountService provideAccountService() {
        return null;
    }
    
    public AccountModule() {
        super();
    }
}