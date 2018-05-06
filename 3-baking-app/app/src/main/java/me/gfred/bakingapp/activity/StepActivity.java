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

    ArrayList<Step> steps;
    int stepIndex;

    StepFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        if (savedInstanceState != null) {

            steps = savedInstanceState.getParcelableArrayList("steps");
            stepIndex = savedInstanceState.getInt("stepIndex");

            if (steps != null) {
                fragment = new StepFragment();
                setTitle(steps.get(stepIndex).getShortDescription());
                fragment.setStep(steps.get(stepIndex), stepIndex, steps.size());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_container, fragment)
                        .commit();

            }

        } else if (intent.hasExtra("steps") && intent.hasExtra("stepIndex")) {

            fragment = new StepFragment();
            FragmentManager manager = getSupportFragmentManager();

            steps = intent.getParcelableArrayListExtra("steps");
            stepIndex = intent.getIntExtra("stepIndex", 0);

            fragment.setStep(steps.get(stepIndex), stepIndex, steps.size());
            setTitle(steps.get(stepIndex).getShortDescription());

            manager.beginTransaction()
                    .add(R.id.step_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps", steps);
        outState.putInt("stepIndex", stepIndex);

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
        stepIndex++;
        fragment = new StepFragment();
        fragment.setStep(steps.get(stepIndex), stepIndex, steps.size());
        setTitle(steps.get(stepIndex).getShortDescription());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();

    }

    private void previousClick() {
        stepIndex--;
        fragment = new StepFragment();
        fragment.setStep(steps.get(stepIndex), stepIndex, steps.size());
        setTitle(steps.get(stepIndex).getShortDescription());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();

    }
}
