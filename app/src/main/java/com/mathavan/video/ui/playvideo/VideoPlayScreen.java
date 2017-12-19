package com.mathavan.video.ui.playvideo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mathavan.video.R;
import com.mathavan.video.model.VideoListItem;

public class VideoPlayScreen extends AppCompatActivity{

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.videoView);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("video")) {
            VideoListItem videoListItem = (VideoListItem) extras.getSerializable("video");
            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(videoListItem.getFilePath()));
            videoView.requestFocus();
            videoView.start();
        }
    }
}
