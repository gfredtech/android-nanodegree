package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.fragment.StepFragment;
import me.gfred.bakingapp.model.Step;

public class StepActivity extends AppCompatActivity {

    ArrayList<Step> step;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        StepFragment stepFragment = new StepFragment();
        FragmentManager manager = getSupportFragmentManager();

        if(savedInstanceState != null) {
            step = savedInstanceState.getParcelable("step");
            index = savedInstanceState.getInt("index");
            if (step != null)  {
                stepFragment.setArgs(index, step);

                manager.beginTransaction()
                        .replace(R.id.step_container, stepFragment)
                        .commit();
            }
        }

        else if(intent.hasExtra("steps") && intent.hasExtra("index")) {
            step = intent.getParcelableArrayListExtra("steps");
            index = intent.getIntExtra("index", 0);
            stepFragment.setArgs(index, step);

            manager.beginTransaction()
                    .add(R.id.step_container, stepFragment)
                    .commit();
//            Toast.makeText(this, step.get(index).getShortDescription(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("step", step);
        outState.putInt("index", index);
    }
}
