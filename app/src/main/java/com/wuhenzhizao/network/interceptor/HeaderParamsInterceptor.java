package com.wuhenzhizao.network.interceptor;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * V2 header参数拦截注入
 * <p>
 * Created by wuhenzhizao on 16/5/11.
 */
public class HeaderParamsInterceptor extends BaseInterceptor {

    public HeaderParamsInterceptor(Context mContext) {
        super(mContext);
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.header("Content-Type", "application/json;charset=utf-8");

        Request request = builder.build();

        return chain.proceed(request);
    }
}
