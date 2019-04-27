package cn.abtion.neuqercc.message.data;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

import cn.abtion.neuqercc.NEUQerCCApplication;
import cn.abtion.neuqercc.common.constants.CacheKey;
import cn.abtion.neuqercc.main.MainActivity;
import cn.abtion.neuqercc.utils.ToastUtil;

import static android.content.ContentValues.TAG;
import static android.content.Context.ACTIVITY_SERVICE;
import static cn.abtion.neuqercc.BuildConfig.DEBUG;

/**
 * 聊天相关
 *
 * @author FanHongyu.
 * @since 18/3/17 15:46.
 * email fanhongyu@hrsoft.net.
 */

public class ChatHelper {


    /**
     * 在Application的onCreate方法中初始化环信sdk
     */
    public static void initEM(Context appContext) {
        int pid = android.os.Process.myPid();
        String processName = getProcessAppName(pid, appContext);
        if (processName == null || !processName.equalsIgnoreCase(appContext.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        options.setAutoLogin(false);

        EMClient.getInstance().init(appContext, options);
        EMClient.getInstance().setDebugMode(DEBUG);
    }


    /**
     * 获取processAppName,防止sdk被重复初始化
     *
     * @param pID pid
     * @return name
     */
    private static String getProcessAppName(int pID, Context appContext) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) iterator.next();
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.d("Process", "Error>> :" + e.toString());
            }
        }
        return processName;
    }


    public static void loginEM(final Activity activity) {


        String phone = NEUQerCCApplication.getInstance().getCacheUtil().getString(CacheKey.PHONE_NUMBER);
        String password = NEUQerCCApplication.getInstance().getCacheUtil().getString(CacheKey.PASSWORD);


        if (phone != null && password != null) {


            Log.i(TAG, "loginEM: "+phone);
            Log.i(TAG, "loginEM: "+password);

            EMClient.getInstance().login(phone, password, new EMCallBack() {
                @Override
                public void onSuccess() {

                    //保证进入主页面后本地会话和群组都 load 完毕
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();

                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    //跳转至MainActivity
                    Log.i("login", "onSuccess: EM登录成功");
                }

                @Override
                public void onError(int code, String error) {

                    Log.i("login", "onError: EM登录失败，" + error);
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        } else {

            ToastUtil.showToast("出现异常，请重新登录");
        }


    }


}
