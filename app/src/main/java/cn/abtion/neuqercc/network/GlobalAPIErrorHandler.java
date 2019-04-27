package cn.abtion.neuqercc.network;

import cn.abtion.neuqercc.common.exceptions.ResultException;
import cn.abtion.neuqercc.utils.ToastUtil;

/**
 * @author abtion.
 * @since 17/9/22 18:05.
 * email caiheng@hrsoft.net
 */

public class GlobalAPIErrorHandler {

    public static void handler(int code) {
        switch (code) {
            case 500:
                ToastUtil.showToast("服务器出错啦");
                break;
            default:
                ToastUtil.showToast(code + " : 请求不被允许，请确定是否有权进行该操作");
                break;
        }
    }

    public static void handler(ResultException r) {
        switch (r.getCode()) {

            default:
                ToastUtil.showToast(r.getMsg());
                break;
        }
    }
}
