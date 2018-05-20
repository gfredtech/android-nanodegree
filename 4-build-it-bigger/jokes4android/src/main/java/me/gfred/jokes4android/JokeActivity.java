package me.gfred.jokes4android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        TextView view = findViewById(R.id.joke_textview);

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("joke") != null) {
            view.setText(intent.getStringExtra("joke"));
        }
    }

}
