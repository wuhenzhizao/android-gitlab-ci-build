package com.wuhenzhizao.network.interceptor;

import android.content.Context;

import okhttp3.Interceptor;


/**
 * 拦截器基类
 * <p>
 * Created by wuhenzhizao on 16/5/11.
 */
public abstract class BaseInterceptor implements Interceptor {
    protected Context mContext;

    public BaseInterceptor(Context context) {
        this.mContext = context.getApplicationContext();
    }
}
