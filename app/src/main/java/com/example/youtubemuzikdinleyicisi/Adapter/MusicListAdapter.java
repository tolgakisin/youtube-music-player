package com.example.youtubemuzikdinleyicisi.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubemuzikdinleyicisi.Model.VideoDetails;
import com.example.youtubemuzikdinleyicisi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicListAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;

    private boolean isLiked = false;

    public MusicListAdapter(Activity activity, ArrayList<VideoDetails> videoDetailsArrayList) {
        this.activity = activity;
        this.videoDetailsArrayList = videoDetailsArrayList;
    }

    @Override
    public int getCount() {
        return videoDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(" ", "getView: ");
        VideoDetails videoDetails = (VideoDetails) getItem(position);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.element_youtubevideolist, null);

        TextView lblTitle = layout.findViewById(R.id.lblTitle);
        lblTitle.setText(videoDetails.getTitle());

        ImageView imgVideo = layout.findViewById(R.id.imgVideo);
        //Uri uri = (Uri) videoDetails.getUrl();
        //imgVideo.setImageURI(Uri.parse(videoDetails.getUrl()));
        Picasso.with(activity).load(videoDetails.getUrl()).into(imgVideo);

        ImageView likeIcon = layout.findViewById(R.id.likeIcon);


        return layout;
    }
}
