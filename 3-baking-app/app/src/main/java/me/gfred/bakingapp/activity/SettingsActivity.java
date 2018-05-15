package me.gfred.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Recipe;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Intent intent = getIntent();
        if (intent != null && intent.getParcelableArrayListExtra("entries") != null) {
            propagateBroadcast(intent.<Recipe>getParcelableArrayListExtra("entries"));
        }
    }


    void propagateBroadcast(ArrayList<Recipe> recipes) {
        Intent intent = new Intent();
        intent.setAction("me.gfred.bakingapp.CUSTOM_INTENT");
        intent.putParcelableArrayListExtra("recipes", recipes);
        sendBroadcast(intent);
    }
}
