package com.example.android.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MovieDetails;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Video;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Context mContext;
    private List<Video> videoList;

    //Constructor which takes a Context (c) and a list of videos (videoList)
    public VideosAdapter(Context c, List<Video> videoList){
        this.mContext = c;
        this.videoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new VideoViewHolder(itemView);
    }

    //Method to populate each view holder
    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        //Get video of the corresponding position
        final Video video = videoList.get(position);

        holder.key = video.getKey();
        holder.site = video.getSite();

        holder.name.setText(video.getName());
        holder.type.setText(video.getType());

        //Creation of a click/tap listener to launch youtube intent
        holder.playVideo.setOnClickListener(view -> {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+holder.key));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+holder.key));

            try{
                mContext.startActivity(youtubeIntent);
            } catch(ActivityNotFoundException e){
                mContext.startActivity(webIntent);
            }
        });

        //Creation of click/tap listener to launch sharing intent
        holder.shareVideo.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, holder.name.getText());
            shareIntent.setType("text/plain");

            try{
                mContext.startActivity(shareIntent);
            } catch(ActivityNotFoundException e){
                MovieDetails movieDetailsActivity =(MovieDetails) mContext;
                movieDetailsActivity.showToast(R.string.sharing_error);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    //View holder class
    class VideoViewHolder extends RecyclerView.ViewHolder{

        ImageView playVideo;
        ImageView shareVideo;
        TextView name;
        TextView type;
        String key;
        String site;

        VideoViewHolder(View view){
            super(view);
            playVideo = view.findViewById(R.id.iv_play_video);
            shareVideo = view.findViewById(R.id.iv_share_video);
            name = view.findViewById(R.id.tv_name_video);
            type = view.findViewById(R.id.tv_type_video);
        }
    }

}
