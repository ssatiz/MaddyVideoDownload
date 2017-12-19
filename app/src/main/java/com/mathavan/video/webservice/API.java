package com.mathavan.video.webservice;

import com.mathavan.video.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    public static String BASEURL = "http://avms.augray.com:5006/";

    @GET("download.json")
    Call<ApiResponse> getResponse();
}
