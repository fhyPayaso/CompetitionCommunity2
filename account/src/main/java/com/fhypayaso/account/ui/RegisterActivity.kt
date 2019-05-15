package com.fhypayaso.account.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.fhypayaso.account.R

/**
 *
 * @author fanhongyu
 * @since 2019/5/8 1:55 AM
 */
@Route(path = "account/register")
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}