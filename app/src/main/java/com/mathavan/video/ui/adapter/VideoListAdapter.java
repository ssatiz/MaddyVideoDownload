package com.mathavan.video.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathavan.video.R;
import com.mathavan.video.model.VideoListItem;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListItemViewHolder>{

    private ArrayList<VideoListItem> videoListItems;
    private View.OnClickListener onClickListener;
    private LayoutInflater inflater;

    public VideoListAdapter(Context context, ArrayList<VideoListItem> videoListItems, View.OnClickListener onClickListener) {
        this.videoListItems = videoListItems;
        this.onClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void refreshItem(ArrayList<VideoListItem> videoListItems){
        this.videoListItems = videoListItems;
        notifyDataSetChanged();
    }

    @Override
    public VideoListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_holder_video_item, parent, false);
        return new VideoListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListItemViewHolder holder, int position) {
        holder.onBind(videoListItems.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return videoListItems != null ? videoListItems.size() :0;
    }
}
