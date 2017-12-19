package com.mathavan.video.ui.listvideos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mathavan.video.R;
import com.mathavan.video.model.ApiResponse;
import com.mathavan.video.model.VideoListItem;
import com.mathavan.video.ui.adapter.VideoListAdapter;
import com.mathavan.video.ui.playvideo.VideoPlayScreen;
import com.mathavan.video.webservice.API;
import com.mathavan.video.webservice.RestAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<VideoListItem> videoListItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private VideoListAdapter adapter;
    private TextView lblMessage;
    private int totalDownloadVideos = 0;
    private List<String> downloadUrls;

    int REQUEST_ID = 123;
    String permission[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress);
        lblMessage = findViewById(R.id.lblStatus);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new VideoListAdapter(this, null, this);
        recyclerView.setAdapter(adapter);
        init();
    }

    void init(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
           callWebService();
        }else{
            ActivityCompat.requestPermissions(this, permission, REQUEST_ID);
        }
    }

    void callWebService(){
        API api = RestAdapter.getInstance().getRetrofit().create(API.class);
        Call<ApiResponse> call = api.getResponse();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        downloadUrls = Arrays.asList(apiResponse.file_download.split("\\|"));
                        downloadVideos(downloadUrls);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            callWebService();
        }else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    void downloadVideos(List<String> downloadUrls){
        for (int i=0; i<downloadUrls.size(); i++){
            new DownloadVideoTask(downloadUrls.get(i)).execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cardRoot:
                VideoListItem videoListItem = (VideoListItem) view.getTag();
                Intent intent = new Intent(this, VideoPlayScreen.class);
                intent.putExtra("video", videoListItem);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class DownloadVideoTask extends AsyncTask<Void, Void, String>{
        private String downloadurl;

        private String rootDir;
        public DownloadVideoTask(String url){
            downloadurl = url;

            rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Maddy";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            lblMessage.setText("Downloading videos ("+totalDownloadVideos+"/"+downloadUrls.size()+")");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String fileName = "Mathavan_"+System.currentTimeMillis()+".mp4";
            try {

                File rootFile = new File(rootDir);
                if (!rootFile.exists()){
                    rootFile.mkdir();
                }

                URL url = new URL(downloadurl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                FileOutputStream f = new FileOutputStream(new File(rootFile,
                        fileName));
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();

            } catch (IOException e) {
                Log.d("Error....", e.toString());
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            VideoListItem videoListItem = new VideoListItem();
            videoListItem.setFileName(fileName);
            videoListItem.setFilePath(rootDir+"/"+fileName);
            totalDownloadVideos++;
            lblMessage.setText("Downloading videos ("+totalDownloadVideos+"/"+downloadUrls.size()+")");
            videoListItems.add(videoListItem);
            progressBar.setVisibility(View.GONE);
            adapter.refreshItem(videoListItems);
        }
    }

}
