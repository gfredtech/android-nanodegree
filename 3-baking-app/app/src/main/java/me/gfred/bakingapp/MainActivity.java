package me.gfred.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }
}
