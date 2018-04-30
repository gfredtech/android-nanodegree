package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.fragment.RecipeFragment;
import me.gfred.bakingapp.model.Recipe;

public class RecipeActivity extends AppCompatActivity {

    Recipe recipe;

    RecipeFragment recipeFragment;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if(savedInstanceState == null) {
            recipeFragment = new RecipeFragment();
            recipeFragment.setContext(this);
            manager = getSupportFragmentManager();

            if (intent.hasExtra("recipe")) {
                if()
                recipe = intent.getParcelableExtra("recipe");
                recipeFragment.setRecipe(recipe);
                manager.beginTransaction()
                        .add(R.id.recipe_container, recipeFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
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
        }
    }
}
