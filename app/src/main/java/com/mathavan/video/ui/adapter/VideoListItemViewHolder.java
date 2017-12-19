package com.mathavan.video.ui.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathavan.video.R;
import com.mathavan.video.model.VideoListItem;

public class VideoListItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgThumb;
    private TextView lblFileName;
    private CardView cardView;


    public VideoListItemViewHolder(View itemView) {
        super(itemView);

        imgThumb = itemView.findViewById(R.id.imgVideoThumb);
        lblFileName = itemView.findViewById(R.id.lblFileName);
        cardView = itemView.findViewById(R.id.cardRoot);
    }

    public void onBind(VideoListItem videoListItem, View.OnClickListener onClickListener){
        imgThumb.setImageBitmap(getThumb(videoListItem.getFilePath()));
        lblFileName.setText(videoListItem.getFileName());
        cardView.setTag(videoListItem);
        cardView.setOnClickListener(onClickListener);
    }

    private Bitmap getThumb(String path){
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);
        return thumb;
    }
}
