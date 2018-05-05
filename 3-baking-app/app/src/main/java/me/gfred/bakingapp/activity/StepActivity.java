package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.fragment.StepFragment;
import me.gfred.bakingapp.model.Step;

public class StepActivity extends AppCompatActivity implements StepFragment.OnNavigationClickListener {

    ArrayList<Step> step;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        if (savedInstanceState != null) {

            step = savedInstanceState.getParcelableArrayList("step");
            index = savedInstanceState.getInt("index");

            if (step != null) {
                StepFragment fragment = new StepFragment();
                setTitle(step.get(index).getShortDescription());

                fragment.setStep(step.get(index), index, step.size());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, fragment)
                        .commit();


            }
        } else if (intent.hasExtra("steps") && intent.hasExtra("index")) {

            StepFragment stepFragment = new StepFragment();
            FragmentManager manager = getSupportFragmentManager();

            step = intent.getParcelableArrayListExtra("steps");
            index = intent.getIntExtra("index", 0);

            stepFragment.setStep(step.get(index), index, step.size());
            setTitle(step.get(index).getShortDescription());

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

    @Override
    public void onNavigationClicked(boolean next) {
        if (next) {
            nextClick();
        } else {
            previousClick();
        }
    }

    private void nextClick() {
        index++;
        if (index == step.size()) index = 0;

        StepFragment stepFragment = new StepFragment();
        stepFragment.setStep(step.get(index), index, step.size());
        setTitle(step.get(index).getShortDescription());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

    }

    private void previousClick() {
        index--;
        if (index == -1) index = step.size() - 1;

        StepFragment stepFragment = new StepFragment();
        stepFragment.setStep(step.get(index), index, step.size());
        setTitle(step.get(index).getShortDescription());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

    }
}
