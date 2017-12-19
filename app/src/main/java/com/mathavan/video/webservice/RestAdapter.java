package com.mathavan.video.webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {

    private Retrofit retrofit;
    private static RestAdapter restAdapter;

    public static RestAdapter getInstance(){
        if (restAdapter == null) {
            restAdapter = new RestAdapter();
        }
        return restAdapter;
    }


    public Retrofit getRetrofit(){
        if (retrofit == null) {
           retrofit = new Retrofit.Builder().baseUrl(API.BASEURL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
        }
        return retrofit;
    }
}
