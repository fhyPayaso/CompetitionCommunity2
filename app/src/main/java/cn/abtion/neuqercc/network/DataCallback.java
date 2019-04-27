package cn.abtion.neuqercc.network;

import android.util.Log;

import cn.abtion.neuqercc.common.exceptions.ResultException;
import cn.abtion.neuqercc.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * packaged callback for network response
 *
 * @author abtion.
 * @since 17/9/22 18:04.
 * email caiheng@hrsoft.net
 */

public abstract class DataCallback<T> implements Callback<T> {

    public abstract void onDataResponse(Call<T> call, Response<T> response);

    public abstract void onDataFailure(Call<T> call, Throwable t);

    public abstract void dismissDialog();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        dismissDialog();
        if (response.code() != 200) {
            GlobalAPIErrorHandler.handler(response.code());
        } else {
            onDataResponse(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onDataFailure(call, t);
        dismissDialog();

        ToastUtil.showToast(t.toString());
        Log.i(TAG, "onFailure: " + t.toString());


        if (t instanceof ResultException) {
            GlobalAPIErrorHandler.handler((ResultException) t);
        } else {
            ToastUtil.showToast("网络连接失败，请稍后再试");
        }
    }
}
