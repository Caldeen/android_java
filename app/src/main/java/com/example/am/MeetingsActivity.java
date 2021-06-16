package com.example.am;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import data.classes.Meeting;
import helpers.RetrofitClient;
import helpers.SharedPrefsHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.MeetingsService;

public class MeetingsActivity extends AppCompatActivity {
    private ImageView settingsImageView;
    private List<Meeting> meetings;
    private RecyclerView meetingsRecyclerView;
    private MeetingsAdapter meetingsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsHandler.loadTheme(this);
        setContentView(R.layout.meetings_activity);
        settingsImageView = findViewById(R.id.imageView4);
        meetingsRecyclerView = findViewById(R.id.recyclerView2);
        meetings = new ArrayList<>();
        meetingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        meetingsAdapter = new MeetingsAdapter(meetings,this);
        meetingsRecyclerView.setAdapter(meetingsAdapter);

        MeetingsService meetingsService = RetrofitClient.getRetrofit().create(MeetingsService.class);
        Call<List<Meeting>> listCall = meetingsService
                .getMeetings(SharedPrefsHandler.getToken(getApplicationContext()));
        listCall.enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                meetings = response.body();
                meetingsAdapter.setMeetings(meetings);
                meetingsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(nextScreen);
            }
        });

    }
    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        Log.println(Log.INFO, "info", "restarted");
    }
}
