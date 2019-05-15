package com.fhypayaso.account.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fhypayaso.account.R
import com.fhypayaso.account.dagger.AccountComponent
import com.fhypayaso.account.data.AccountViewModel
import com.fhypayaso.core.Graph
import com.fhypayaso.mainapi.MainService
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 *
 * @author fanhongyu
 * @since 2019/5/8 1:50 AM
 */
@Route(path = "/account/login")
class LoginActivity : AppCompatActivity() {

    private lateinit var mViewModel: AccountViewModel

    @Inject
    lateinit var mainService: MainService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Graph.getInstance().getGraph(AccountComponent::class.java).inject(this)
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        mViewModel.getLoginLiveData().observe(this, Observer {
            if (it != null) {
                ARouter.getInstance().build("/main/home").navigation()
                finish()
            }
        })
        btn_login.setOnClickListener {
            mViewModel.login(
                edit_phone.text.toString().trim(),
                edit_password.text.toString().trim()
            )
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}