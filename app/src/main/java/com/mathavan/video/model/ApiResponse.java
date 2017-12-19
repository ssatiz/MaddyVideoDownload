package com.mathavan.video.model;


import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("file_download")
    public String file_download;
    @SerializedName("ios_app_version")
    public String ios_app_version;
    @SerializedName("android_app_version")
    public String android_app_version;
    @SerializedName("ios_force_update")
    public String ios_force_update;
    @SerializedName("android_force_update")
    public String android_force_update;
    @SerializedName("update")
    public String update;
}
