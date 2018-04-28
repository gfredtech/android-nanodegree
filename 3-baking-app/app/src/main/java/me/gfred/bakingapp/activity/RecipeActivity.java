package me.gfred.bakingapp.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.adapter.RecipeRecyclerAdapter;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.recipe_image)
    ImageView recipeImage;

    @BindView(R.id.ingredients_tv)
    TextView ingredientsTextView;

    @BindView(R.id.step_recyclerview)
    RecyclerView stepRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if(intent.hasExtra("recipe")) {
            Recipe recipe = intent.getParcelableExtra("recipe");
            setTitle(recipe.getName());
            populateUI(recipe);

        }
    }


    void populateUI(Recipe recipe) {
        if(recipe.getImage() != null && recipe.getImage().length() > 0) {
            recipeImage.setVisibility(View.VISIBLE);
            Picasso.get().load(recipe.getImage())
                    .into(recipeImage);
        }

        StringBuilder builder = new StringBuilder();
        int i = 1;
        for(Ingredient n: recipe.getIngredients()) {
            builder.append(i)
                    .append(". ")
                    .append(n.getIngredient())
                    .append(" (")
                    .append(n.getQuantity())
                    .append(" ")
                    .append(n.getMeasure())
                    .append(" )\n");
            i += 1;
        }

        if(builder.length() > 0) ingredientsTextView.setText(builder.toString());
        else ingredientsTextView.setVisibility(View.INVISIBLE);

        RecipeRecyclerAdapter adapter = new RecipeRecyclerAdapter(this, recipe.getSteps());
        stepRecyclerView.setLayoutManager(new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false));

        stepRecyclerView.setAdapter(adapter);
    }


}
