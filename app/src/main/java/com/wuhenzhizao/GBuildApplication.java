package com.wuhenzhizao;

import android.app.Application;

import com.wuhenzhizao.network.ApiManager;
import com.wuhenzhizao.network.Configuration;

/**
 * Created by wuhenzhizao on 2017/8/23.
 */

public class GBuildApplication extends Application {
    private static GBuildApplication application;

    public static GBuildApplication getApplication() {
        return application;
    }

    public void onCreate() {
        super.onCreate();
        application = this;
        // 设置成gitlab提供的地址前缀
        ApiManager.instance().init(this, new Configuration().setBaseUrl("http://localhost:80/api/"));
    }
}
