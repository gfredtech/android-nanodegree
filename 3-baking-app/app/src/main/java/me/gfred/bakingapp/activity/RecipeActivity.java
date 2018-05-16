package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.fragment.RecipeFragment;
import me.gfred.bakingapp.fragment.StepFragment;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.model.Step;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnStepClickedListener,
        StepFragment.OnNavigationClickListener {

    static boolean mTwoPane;
    Recipe recipe;
    int stepIndex;
    RecipeFragment recipeFragment;
    StepFragment stepFragment;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        manager = getSupportFragmentManager();

        if (findViewById(R.id.tablet_pane) != null) {
            // handle's two-pane UI
            mTwoPane = true;

            if (savedInstanceState == null && intent.hasExtra("recipe")) {
                recipe = intent.getParcelableExtra("recipe");
                recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(recipe);
                manager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();

                stepFragment = new StepFragment();
                stepIndex = 0;
                int size = recipe.getSteps().size();
                Step step = recipe.getSteps().get(stepIndex);
                stepFragment.setStep(step, stepIndex, size);

                manager.beginTransaction()
                        .add(R.id.step_container, stepFragment)
                        .commit();


            } else if (savedInstanceState != null &&
                    savedInstanceState.getParcelable("recipe") != null) {

                recipe = savedInstanceState.getParcelable("recipe");
                stepIndex = savedInstanceState.getInt("stepIndex");

                recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(recipe);
                manager.beginTransaction()
                        .replace(R.id.recipe_container, recipeFragment)
                        .commit();

                stepFragment = new StepFragment();
                int size = recipe.getSteps().size();
                Step step = recipe.getSteps().get(stepIndex);
                stepFragment.setStep(step, stepIndex, size);

                manager.beginTransaction()
                        .replace(R.id.step_container, stepFragment)
                        .commit();

            }


        } else {
            //phome, so we inflate only recipe fragment
            mTwoPane = false;

            if (savedInstanceState == null && intent.hasExtra("recipe")) {
                recipe = intent.getParcelableExtra("recipe");
                recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(recipe);

                manager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();

            } else if (savedInstanceState != null &&
                    savedInstanceState.getParcelable("recipe") != null) {

                recipe = savedInstanceState.getParcelable("recipe");
                stepIndex = savedInstanceState.getInt("stepIndex");

                recipeFragment = new RecipeFragment();
                recipeFragment.setRecipe(recipe);
                manager.beginTransaction()
                        .replace(R.id.recipe_container, recipeFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
        outState.putInt("stepIndex", stepIndex);
    }

    @Override
    public void onStepClicked(int stepIndex) {

        if (mTwoPane) {
            stepFragment = new StepFragment();
            int size = recipe.getSteps().size();
            Step step = recipe.getSteps().get(stepIndex);
            stepFragment.setStep(step, stepIndex, size);
            this.stepIndex = stepIndex;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();

        } else {

            Intent intent = new Intent(RecipeActivity.this, StepActivity.class);
            intent.putParcelableArrayListExtra("steps", new ArrayList<Step>() {{
                addAll(recipe.getSteps());
            }});
            intent.putExtra("stepIndex", stepIndex);
            startActivity(intent);
        }
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
        stepFragment = new StepFragment();
        int size = recipe.getSteps().size();
        Step step = recipe.getSteps().get(stepIndex);
        stepFragment.setStep(step, stepIndex, size);
        stepFragment.resetPosition();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

    }

    private void previousClick() {
        stepIndex--;
        stepFragment = new StepFragment();
        int size = recipe.getSteps().size();
        Step step = recipe.getSteps().get(stepIndex);
        stepFragment.setStep(step, stepIndex, size);
        stepFragment.resetPosition();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

    }
}
