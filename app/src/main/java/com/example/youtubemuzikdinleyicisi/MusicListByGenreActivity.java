package com.example.youtubemuzikdinleyicisi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MusicListByGenreActivity extends AppCompatActivity {

    private Intent incomingIntent;
    private ListView videoListView;
    private ArrayList<VideoDetails> videoDetailsArrayList;
    private MusicListAdapter musicListAdapter;
    private String genreId;
    private String url;
    private String searchText;
    private ImageView likeIcon;
    private SharedPreferences sharedPreferences;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list_by_genre);
        initComponents();
        displayVideos();
        registerEventHandlers();
    }

    private void initComponents() {

        incomingIntent = getIntent();
        genreId = incomingIntent.getStringExtra("genre");
        searchText = incomingIntent.getStringExtra("searchItem");
        Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_SHORT).show();
        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&topicId=" + genreId + "&q=" + searchText + "&maxResults=40&key=" + YoutubeConfig.getApiKey();
        //searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&topicId=/m/04rlf&maxResults=40&key=" + YoutubeConfig.getApiKey();

        videoListView = findViewById(R.id.videoListView);
        videoDetailsArrayList = new ArrayList<>();
        musicListAdapter = new MusicListAdapter(MusicListByGenreActivity.this, videoDetailsArrayList);
        likeIcon = findViewById(R.id.likeIcon);
        Log.d("-- - ", "initComponents: asd");

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.showUsername);

        menuItem.setTitle(sharedPreferences.getString("username", "defValue"));
        //menuItem.setCheckable(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.ana_menu, menu);

        this.menu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.logout:

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(MusicListByGenreActivity.this, LoginActivity.class);
                startActivity(intent);

//                Toast.makeText(this, "Exit",
//                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void displayVideos() {
        Log.d(" ", "displayVideos: deyim");
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

                        String videoId = jsonVideoId.getString("videoId");

                        VideoDetails vd = new VideoDetails();
                        vd.setVideoID(videoId);
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
                Intent intent = new Intent(MusicListByGenreActivity.this, MainActivity.class);
                intent.putExtra("videoId", videoDetails.videoID);
                intent.putExtra("searchText", "");
                intent.putExtra("genre", genreId);
                startActivity(intent);
            }
        });


    }
}
