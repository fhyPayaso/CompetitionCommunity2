package cn.abtion.neuqercc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import cn.abtion.neuqercc.account.activities.LoginActivity;
import cn.abtion.neuqercc.message.data.ChatHelper;
import cn.abtion.neuqercc.utils.CacheUtil;

import static android.content.ContentValues.TAG;

/**
 * @author abtion.
 * @since 17/9/22 17:56.
 * email caiheng@hrsoft.net
 */

public class NEUQerCCApplication extends Application {
    private static NEUQerCCApplication instance;//实例对象
    private static List<Activity> activityList = new ArrayList<>();
    private static CacheUtil cacheUtil;
    public static Context appContext;


    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        NEUQerCCApplication application = (NEUQerCCApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appContext = this;
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        LeakCanary.install(this);
        ChatHelper.initEM(appContext);


        //解决安卓7.0相机调用崩溃问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    /**
     * 缓存初始化
     */
    public CacheUtil getCacheUtil() {
        if (cacheUtil == null) {
            cacheUtil = CacheUtil.get(instance.getFilesDir());
        }
        return cacheUtil;
    }

    /**
     * 外部获取实例对象
     *
     * @return net.hrsoft.transparent_factory_manager.NEUQerCCApplication
     */
    public static NEUQerCCApplication getInstance() {
        return instance;
    }

    private static android.app.Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new android.app.Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            activityList.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityList.remove(activity);
        }
    };

    /**
     * 移除Activity
     *
     * @param activity activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 清除所有Activity
     */
    public void removeAllActivity() {
        for (Activity activity : activityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        removeAllActivity();
        System.exit(0);
    }

    /**
     * 退出账户
     */
    public void exitAccount() {
        getCacheUtil().clear();
        removeAllActivity();

        Log.i(TAG, "exitAccount: 退出账户成功，缓存已经清除");
        Intent intent = new Intent(this,LoginActivity.class);
        Log.i(TAG, "exitAccount: 重新进入登录界面");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.i(TAG, "exitAccount: 重新进入登录界面");
    }
}
