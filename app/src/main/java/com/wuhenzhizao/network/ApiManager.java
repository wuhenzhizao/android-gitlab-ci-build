package com.wuhenzhizao.network;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liujikun on 15/12/30.
 */
public class ApiManager {
    private static ApiManager instance;
    private Retrofit retrofit;
    private WeakReference<Context> context;
    private Configuration configuration;

    private ApiManager() {
    }

    public static ApiManager instance() {
        if (instance != null) {
            return instance;
        }
        synchronized (ApiManager.class) {
            if (instance == null) {
                instance = new ApiManager();
            }
        }
        return instance;
    }

    public void init(Context context, Configuration configuration) {
        this.context = new WeakReference<>(context.getApplicationContext());
        this.configuration = configuration;
    }

    public String getBaseUrl() {
        return configuration.getBaseUrl();
    }

    public String getBaseH5Url() {
        return configuration.getBaseH5Url();
    }

    public String getBaseWapUrl() {
        return configuration.getBaseWapUrl();
    }

    public String getBaseImUrl() {
        return configuration.getBaseImUrl();
    }

    private Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create());
    }

    public <T> T getService(Class<T> service) {
        if (retrofit == null) {
            Retrofit.Builder builder = getRetrofitBuilder();
            builder.baseUrl(configuration.getBaseUrl());
            builder.client(OkHttpClientFactory.newHttp(context.get()));

            retrofit = builder.build();
        }
        return retrofit.create(service);
    }
}
