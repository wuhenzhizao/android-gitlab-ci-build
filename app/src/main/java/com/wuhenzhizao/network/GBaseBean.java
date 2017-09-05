package com.wuhenzhizao.network;

import java.io.Serializable;

/**
 * Created by zhulianggang on 17/1/23.
 */

public class GBaseBean implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GBaseBean{" +
                "message='" + message + '\'' +
                '}';
    }
}
