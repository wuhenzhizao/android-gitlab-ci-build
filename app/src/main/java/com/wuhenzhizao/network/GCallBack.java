package com.wuhenzhizao.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wuhenzhizao.utils.ObjectUtils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Constructor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by chenbaocheng on 16/5/11.
 */
public abstract class GCallBack<T extends GBaseBean> implements Callback<T> {

    public static final String DATA_FIELD_NAME = "data";

    private Class<T> responseType;

    public GCallBack() {
    }

    public GCallBack(Class<T> responseType) {
        this.responseType = responseType;
    }

    /**
     * check if there is valid data
     *
     * @param body response body
     * @return false if body is null or data is null, otherwise true
     */
    private boolean hasValidData(T body) {
        if (body == null) {
            // 没有body
            return false;
        }

        try {
            if (ObjectUtils.getFieldValue(body, DATA_FIELD_NAME) == null) {
                // data是null
                return false;
            }
        } catch (NoSuchFieldException e) {
            // 没有定义data字段，当前业务不需要data字段
            return true;
        } catch (IllegalAccessException e) {
            // data字段访问失败，视同于没有定义data字段
            Logger.t("api").e(e, "Cannot access data field.");
            return true;
        }

        return true;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            // HTTP Code在200-300之间，JSON解析成功

            if (hasValidData(response.body())) {
                // 数据可用
                onSuccess(response, call);
            } else {
                // 请求本身成功，但是body或data字段不可用
                onSuccessWithInvalidData(response, call);
            }

            return;
        }

        Gson gson = new GsonBuilder().create();

        String errorBody, message;
        T body = null;
        GBaseBean baseResponse = null;
        try {
            errorBody = response.errorBody().string();

            if (responseType != null) {
                // 使用给定类型
                body = gson.fromJson(errorBody, responseType);
            }

            if (body != null) {
                message = body.getMessage();
            } else {
                // 没有给定类型,使用MBean来解析message字段
                baseResponse = gson.fromJson(errorBody, GBaseBean.class);
                message = baseResponse.getMessage();
            }
        } catch (Exception e) {
            // 读取错误信息异常
            onFailure(call, e);
            return;
        }

        int code = response.code();
        if (body == null && message == null) {
            // 读取错误信息失败
            onFailure(call, new Exception("Failed to parse error body. HTTP CODE : " + code));
            return;
        }

        Response<T> newResponse = body == null ? response : buildResponse(body, response.raw());

        if (code >= 400 && code < 500) {
            onResponseWithCode400(newResponse, message, call);
            return;
        }

        if (code >= 500 && code < 600) {
            // 5XX错误
            onResponseWithCode500(newResponse, message, call);
            return;
        }
    }

    private Response<T> buildResponse(T body, okhttp3.Response rawResponse) {
        return newInstance(Response.class, rawResponse, body, null);
    }

    public static <T> T newInstance(String className) {

        try {
            Class<T> cls = (Class<T>) Class.forName(className);
            return newInstance(cls);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> cls) {
        return newInstance(cls, null, null);
    }

    public static <T> T newInstance(Class<T> cls, Object... object) {
        return newInstance(cls, null, object);
    }

    public static <T> T newInstance(Class<T> cls, Class<?>[] parameterTypes, Object[] parameters) {
        if (parameters == null) {
            parameters = new Object[]{};
        }

        if (parameterTypes != null) {
            try {
                Constructor<T> constructor = cls.getDeclaredConstructor(parameterTypes);
                constructor.setAccessible(true);
                return constructor.newInstance(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (Constructor<?> constructor : cls.getDeclaredConstructors()) {
                try {
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * Callback for the response whose body is null or body.data is null.
     *
     * @param response the retrofit response
     * @param call     the message returned by server
     */
    protected void onSuccessWithInvalidData(Response<T> response, Call<T> call) {
        onFailure(call, new Exception("Response has no body or data"));
    }

    /**
     * Callback for the response whose HTTP CODE is between 400 and 499.
     *
     * @param response the retrofit response
     * @param message  the message returned by server
     * @param call     current call
     */
    protected void onResponseWithCode400(Response<T> response, String message, Call<T> call) {
        onError(response.code(), message, call);
    }

    /**
     * Callback for the response whose HTTP CODE is between 500 and 599.
     *
     * @param response the retrofit response
     * @param message  the message returned by server
     * @param call     current call
     */
    protected void onResponseWithCode500(Response<T> response, String message, Call<T> call) {
        Logger.t("api").e("HTTP CODE: %d, message: %s", response.code(), message);
        onFailure(call, new Exception(message));
    }

    /**
     * Callback for a successful response.
     * Response.body and response.body.data will never be null.
     *
     * @param response the retrofit response
     * @param call     current call
     */
    protected abstract void onSuccess(Response<T> response, Call<T> call);

    /**
     * Callback for the response whose HTTP CODE is between 400 and 499
     *
     * @param httpCode the HTTP CODE
     * @param message  the message from error body
     * @param call     current call
     */
    protected abstract void onError(int httpCode, String message, Call<T> call);
}
