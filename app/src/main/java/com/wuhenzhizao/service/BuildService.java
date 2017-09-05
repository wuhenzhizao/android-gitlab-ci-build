package com.wuhenzhizao.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wuhenzhizao on 2017/8/23.
 */

public interface BuildService {

    @FormUrlEncoded
    @POST("v3/projects/1137/trigger/builds")
    Call<BuildResponse> postBuildRequest(
            @Field("token") String token,
            @Field("ref") String branch,
            @Field("variables[RELEASE_BUILD]") String onlineBuild,
            @Field("variables[DEBUG_BUILD]") String devBuild);
}
