package com.wuhenzhizao.network;

/**
 * Created by wuhenzhizao on 2017/3/14.
 */
public class Configuration {
    private String baseUrl;
    private String baseH5Url;
    private String baseWapUrl;
    private String baseImUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public Configuration setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getBaseH5Url() {
        return baseH5Url;
    }

    public Configuration setBaseH5Url(String baseH5Url) {
        this.baseH5Url = baseH5Url;
        return this;
    }

    public String getBaseWapUrl() {
        return baseWapUrl;
    }

    public Configuration setBaseWapUrl(String baseWapUrl) {
        this.baseWapUrl = baseWapUrl;
        return this;
    }

    public String getBaseImUrl() {
        return baseImUrl;
    }

    public Configuration setBaseImUrl(String baseImUrl) {
        this.baseImUrl = baseImUrl;
        return this;
    }
}
