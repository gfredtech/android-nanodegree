package me.gfred.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.gfred.bakingapp.model.Recipe;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent.hasExtra("recipe")) {
            Recipe recipe = intent.getParcelableExtra("recipe");
            Toast.makeText(this,
                    recipe.getName() + " " +
                            recipe.getIngredients().get(0).getIngredient(), Toast.LENGTH_SHORT).show();
            setTitle(recipe.getName());
        }
    }
}
