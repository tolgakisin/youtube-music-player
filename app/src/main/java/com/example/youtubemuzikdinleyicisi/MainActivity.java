package com.example.youtubemuzikdinleyicisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubemuzikdinleyicisi.Adapter.MusicListAdapter;
import com.example.youtubemuzikdinleyicisi.Model.VideoDetails;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends YouTubeBaseActivity {

    public static final String TAG = "MainActivity";

    private YouTubePlayerView youtubePlayerView;
    private Intent incomingIntent;
    private String videoId;
    MaterialButton btnPlay;
    private MusicListAdapter musicListAdapter;
    private ArrayList<VideoDetails> videoDetailsArrayList;
    private String genreId;
    private String url;
    private String searchText;
    private ListView videoListView;
    //YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate Starting.");


        initComponents();
        loadVideo();
        displayVideos();
        registerEventHandlers();


    }

    private void initComponents() {
        incomingIntent = getIntent();
        videoId = incomingIntent.getStringExtra("videoId");
        searchText = incomingIntent.getStringExtra("searchText");
        genreId = incomingIntent.getStringExtra("genre");
        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&topicId=" + genreId + "&q=" + searchText + "&maxResults=40&key=" + YoutubeConfig.getApiKey();
        videoDetailsArrayList = new ArrayList<>();
        youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);

        videoListView = findViewById(R.id.videoListView);
        videoDetailsArrayList = new ArrayList<>();
        musicListAdapter = new MusicListAdapter(MainActivity.this, videoDetailsArrayList);

    }

    private void loadVideo() {
        youtubePlayerView.initialize(YoutubeConfig.getApiKey(),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    private void displayVideos() {
        Log.d(" ", "-displayVideos-");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d(" ", "-onResponse-");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");
                        JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                        String videoIdFromJson = jsonVideoId.getString("videoId");

                        if (videoId.equals(videoIdFromJson))
                            continue;

                        VideoDetails vd = new VideoDetails();
                        vd.setVideoID(videoIdFromJson);
                        vd.setTitle(jsonObjectSnippet.getString("title"));
                        vd.setDescription(jsonObjectSnippet.getString("description"));
                        vd.setUrl(jsonObjectDefault.getString("url"));

                        Log.d(" ", "onResponse: Title = " + vd.getTitle());
                        videoDetailsArrayList.add(vd);
                    }

                    videoListView.setAdapter(musicListAdapter);
                    musicListAdapter.notifyDataSetChanged(); //?
                    Log.d(" ", "onResponse: adaptör bağlandı");
                } catch (JSONException e) {
                    Log.e(" ", "onResponse: hata catch");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(" ", "onErrorResponse: hata");
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(" ", "displayVideos: requestqueue added");
        requestQueue.add(stringRequest);
    }

    private void registerEventHandlers() {
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoDetails videoDetails = (VideoDetails) videoListView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), videoDetails.getVideoID() + " " + videoDetails.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("videoId", videoDetails.videoID);
                intent.putExtra("genre", genreId);
                intent.putExtra("searchText", "");
                startActivity(intent);
            }
        });
    }
}
