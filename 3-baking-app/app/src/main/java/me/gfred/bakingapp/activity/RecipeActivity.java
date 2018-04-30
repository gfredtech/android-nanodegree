package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.fragment.RecipeFragment;
import me.gfred.bakingapp.fragment.StepFragment;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.model.Step;

public class RecipeActivity extends AppCompatActivity {



    Recipe recipe;
    Step step;

    RecipeFragment recipeFragment;
    StepFragment stepFragment;
    FragmentManager manager;

    boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if(findViewById(R.id.tablet_pane) != null) {
            mTwoPane = true;
        }

        if(savedInstanceState == null) {
            recipeFragment = new RecipeFragment();
            recipeFragment.setContext(this);
            manager = getSupportFragmentManager();

            if (intent.hasExtra("recipe")) {
                recipe = intent.getParcelableExtra("recipe");
                recipeFragment.setRecipe(recipe);
                manager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();

                if(mTwoPane) {
                    stepFragment = new StepFragment();
                    stepFragment.setArgs(0, recipe.getSteps());
                    manager.beginTransaction()
                            .add(R.id.step_container, stepFragment)
                            .commit();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
        outState.putParcelable("step", step);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.getParcelable("recipe") != null) {
            recipeFragment = new RecipeFragment();
            recipeFragment.setContext(this);
            manager = getSupportFragmentManager();
            recipe = savedInstanceState.getParcelable("recipe");
            recipeFragment.setRecipe(recipe);

            manager.beginTransaction()
                    .replace(R.id.recipe_container, recipeFragment)
                    .commit();
            if(mTwoPane) {
                step = savedInstanceState.getParcelable("step");
                stepFragment = new StepFragment();
                stepFragment.setArgs(recipe.getSteps().indexOf(step), recipe.getSteps());
                manager.beginTransaction()
                        .replace(R.id.step_container, stepFragment)
                        .commit();
            }
        }
    }
}
