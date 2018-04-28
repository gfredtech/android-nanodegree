package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.gfred.bakingapp.model.Step;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if(intent.hasExtra("step")) {
            Step step = intent.getParcelableExtra("step");
            Toast.makeText(this, step.getShortDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
