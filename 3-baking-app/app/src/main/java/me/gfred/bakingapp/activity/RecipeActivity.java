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

    Recipe recipe;
    int stepIndex;

    RecipeFragment recipeFragment;
    StepFragment stepFragment;
    FragmentManager manager;

    static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        manager = getSupportFragmentManager();

        if (findViewById(R.id.tablet_pane) != null) {
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
                stepFragment.setStep(recipe.getSteps().get(stepIndex), stepIndex, recipe.getSteps().size());
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
                stepFragment.setStep(recipe.getSteps().get(stepIndex), stepIndex, recipe.getSteps().size());
                manager.beginTransaction()
                        .replace(R.id.step_container, stepFragment)
                        .commit();

            }


        } else {

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
    public void onStepClicked(int index) {

        if (mTwoPane) {
            StepFragment stepFragment = new StepFragment();
            stepFragment.setStep(recipe.getSteps().get(index), index, recipe.getSteps().size());
            stepIndex = index;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();

        } else {

            Intent intent = new Intent(RecipeActivity.this, StepActivity.class);
            intent.putParcelableArrayListExtra("steps", new ArrayList<Step>() {{
                addAll(recipe.getSteps());
            }});
            intent.putExtra("index", index);
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
        if (stepIndex == -1) stepIndex = recipe.getSteps().size() - 1;
        StepFragment stepFragment = new StepFragment();
        stepFragment.setStep(recipe.getSteps().get(stepIndex), stepIndex, recipe.getSteps().size());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();

    }

    private void previousClick() {

        stepIndex--;
        if (stepIndex == -1) stepIndex = recipe.getSteps().size() - 1;
        StepFragment stepFragment = new StepFragment();
        stepFragment.setStep(recipe.getSteps().get(stepIndex), stepIndex, recipe.getSteps().size());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, stepFragment)
                .commit();


    }
}
