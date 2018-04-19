package me.gfred.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.RecipeClickListener{

    @BindView(R.id.recipe_rv)
    RecyclerView recipeRecyclerView;
    ArrayList<Recipe> recipeArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, recipeArrayList, this);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false));
        recipeRecyclerView.setAdapter(adapter);

        Gson gson = new Gson();
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }
}
