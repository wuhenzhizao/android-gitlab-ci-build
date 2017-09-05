package com.wuhenzhizao.network;

import android.content.Context;
import android.util.Log;

import com.wuhenzhizao.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * The factories that create OkHttpClient instances.
 * <p>
 * Created by chenbaocheng on 16/6/20.
 */
public class OkHttpClientFactory {

    /**
     * Create an OkHttpClient ONLY supports the HTTP protocol
     *
     * @return OkHttpClient instance
     */
    public static OkHttpClient newHttp(Context context) {
        return new Http().create(context);
    }

    private static abstract class Factory {
        protected abstract void buildOkHttpClient(Context context, OkHttpClient.Builder builder);

        public OkHttpClient create(Context context) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            buildOkHttpClient(context, clientBuilder);

            return clientBuilder.build();
        }
    }

    private static class Http extends Factory {

        @Override
        protected void buildOkHttpClient(Context context, OkHttpClient.Builder builder) {
            // Http factory needs no more building process here

            if (BuildConfig.DEBUG) {
                builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

                    @Override
                    public void log(String message) {
                        Log.d("api", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BASIC));
            }
        }
    }
}
