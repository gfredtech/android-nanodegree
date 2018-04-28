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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("recipe")) {
            recipe = intent.getParcelableExtra("recipe");
            setTitle(recipe.getName());
            RecipeFragment recipeFragment = new RecipeFragment();

            recipeFragment.setRecipe(recipe);
            recipeFragment.setContext(this);

            FragmentManager manager = getSupportFragmentManager();

            manager.beginTransaction()
                    .add(R.id.recipe_container, recipeFragment)
                    .commit();


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
    }
}
