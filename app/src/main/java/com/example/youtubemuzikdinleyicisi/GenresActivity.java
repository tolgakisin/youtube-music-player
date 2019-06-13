package com.example.youtubemuzikdinleyicisi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.youtubemuzikdinleyicisi.Model.Genre;
import com.example.youtubemuzikdinleyicisi.Model.VideoDetails;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GenresActivity extends AppCompatActivity {


    private static final int NUM_ROWS = 4;
    private static final int NUM_COLS = 3;

    private ArrayList<Genre> genreArrayList;

    private EditText searchButton;
    private ImageView iconSearch;

    private SharedPreferences sharedPreferences;
    //private String username = sharedPreferences.getString("username","defValue");
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        initComponents();

        populateButtons();
        registerEventHandler();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.showUsername);

        menuItem.setTitle(sharedPreferences.getString("username", "defValue"));
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
                Intent intent = new Intent(GenresActivity.this, LoginActivity.class);
                startActivity(intent);
//                Toast.makeText(this, "Exit",
//                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initComponents() {

        searchButton = findViewById(R.id.searchButton);
        iconSearch = findViewById(R.id.iconSearch);
        genreArrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
    }

    private String jsonDataFromGenreFile() {
        String json;
        try {
            InputStream is = getResources().openRawResource(R.raw.genres);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateButtons() {

        try {
            JSONObject jsonObject = new JSONObject(jsonDataFromGenreFile());
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = jsonObject1.getString("id");
                String name = jsonObject1.getString("name");


                Genre genre = new Genre();
                genre.setId(id);
                genre.setName(name);

                //Log.d(" ", "onResponse: Title = " + vd.getTitle());
                genreArrayList.add(genre);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(" ", "customButton: json = " + jsonDataFromGenreFile());


        TableLayout table = findViewById(R.id.tableForButtons);

        int count = 0;

        while (count < genreArrayList.size()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            table.addView(tableRow);

            for (int col = 0; col < NUM_COLS; col++) {

                Button button = new Button(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(350, TableRow.LayoutParams.MATCH_PARENT);
                //params.setMargins(10,0,10,0);
                button.setTextSize(20);
                button.setLayoutParams(params);
                tableRow.addView(button);
                button.setText(genreArrayList.get(count).getName());

                final String genreId;
                genreId = genreArrayList.get(count).getId();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), genreId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GenresActivity.this, MusicListByGenreActivity.class);
                        intent.putExtra("searchItem", "");
                        intent.putExtra("genre", genreId);
                        startActivity(intent);
                    }
                });
                count++;

                if (count == genreArrayList.size()) //
                    break;
            }

        }

    }

    public void click_onClickGenreButton() {
        //Toast.makeText(this, , Toast.LENGTH_SHORT).show();
    }

    private void registerEventHandler() {
        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchItem = searchButton.getText().toString();
                Intent intent = new Intent(GenresActivity.this, MusicListByGenreActivity.class);
                intent.putExtra("searchItem", searchItem);
                intent.putExtra("genre", "/m/04rlf");
                startActivity(intent);
            }
        });
    }
}
