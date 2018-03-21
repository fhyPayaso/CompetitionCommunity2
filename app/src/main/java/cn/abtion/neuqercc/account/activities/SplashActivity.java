package cn.abtion.neuqercc.account.activities;


import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import cn.abtion.neuqercc.NEUQerCCApplication;
import cn.abtion.neuqercc.R;
import cn.abtion.neuqercc.account.models.CheckTokenResponse;
import cn.abtion.neuqercc.base.activities.NoBarActivity;
import cn.abtion.neuqercc.common.constants.CacheKey;
import cn.abtion.neuqercc.main.MainActivity;
import cn.abtion.neuqercc.network.APIResponse;
import cn.abtion.neuqercc.network.DataCallback;
import cn.abtion.neuqercc.network.RestClient;
import cn.abtion.neuqercc.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import static cn.abtion.neuqercc.account.activities.LoginActivity.password;

/**
 * @author FanHongyu.
 * @since 18/3/19 20:43.
 * email fanhongyu@hrsoft.net.
 */

public class SplashActivity extends NoBarActivity {


    private static final String TAG = "SplashActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initVariable() {

    }

    @Override
    protected void initView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkToken();
            }
        }, 1000);
    }

    @Override
    protected void loadData() {

    }


    private void checkToken() {


        String token = NEUQerCCApplication.getInstance().getCacheUtil().getString(CacheKey.TOKEN);

        if (token == null) {

            Log.i(TAG, "checkToken: 首次登录");
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        } else {


            RestClient.getService().check(token).enqueue(new DataCallback<APIResponse<CheckTokenResponse>>() {
                @Override
                public void onDataResponse(Call<APIResponse<CheckTokenResponse>> call,
                                           Response<APIResponse<CheckTokenResponse>> response) {

                    Log.i(TAG, "onDataResponse: " + "token有效");
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    LoginActivity.phoneNumber = response.body().getData().getPhone();
                    LoginActivity.password = NEUQerCCApplication.getInstance().getCacheUtil().getString(CacheKey
                            .PASSWORD);
                    finish();

                }

                @Override
                public void onDataFailure(Call<APIResponse<CheckTokenResponse>> call, Throwable t) {

                    Log.i(TAG, "onDataFailure: " + "token失效，请重新登录");
                    ToastUtil.showToast("登录过期，请重新登录");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

                @Override
                public void dismissDialog() {

                }
            });
        }
    }


    /**
     * 登录环信接口
     */
    private void loginEM() {


        EMClient.getInstance().login(LoginActivity.phoneNumber, LoginActivity.password, new EMCallBack() {
            @Override
            public void onSuccess() {

                //保证进入主页面后本地会话和群组都 load 完毕
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //跳转至MainActivity
                Log.i("login", "onSuccess: EM登录成功");
            }

            @Override
            public void onError(int code, String error) {

                Log.i("login", "onError: EM登录失败，" + error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
