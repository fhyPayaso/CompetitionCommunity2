package cn.abtion.neuqercc.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * URL拦截器
 *
 * @author: fhyPayaso
 * @since: 2019/2/20 2:03 PM
 */
public class UrlInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl httpUrl = oldRequest.url();
        String urlStr = httpUrl.toString();
        urlStr = urlStr.replace("saiyou/public/index.php/", "");

        Request newRequest = oldRequest.newBuilder()
                .url(urlStr)
                .build();
        return chain.proceed(newRequest);
    }
}
