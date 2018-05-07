package me.gfred.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.adapter.MainRecyclerAdapter;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.util.ApiJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.RecipeClickListener {

    @BindView(R.id.recipe_rv)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    List<Recipe> recipeArrayList;
    static Menu myMenu;
    Callback<List<Recipe>> recipeCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
            recipeArrayList = response.body();
            inflateRecyclerView();
        }

        @Override
        public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
            t.printStackTrace();
            progressBar.setVisibility(View.INVISIBLE);
            myMenu.getItem(0).setVisible(true);
            Toast.makeText(MainActivity.this, "Error loading recipes...", Toast.LENGTH_LONG).show();

        }
    };
    private ApiJson apiJson;

    static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 1;
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            createRecipeApi();
            apiJson.getRecipes().enqueue(recipeCallback);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeArrayList;
        outState.putParcelableArrayList("recipes", recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            recipeArrayList = savedInstanceState.getParcelableArrayList("recipes");

            if (recipeArrayList != null) {
                inflateRecyclerView();
            }
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);

    }

    public void createRecipeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiJson.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiJson = retrofit.create(ApiJson.class);
    }

    void inflateRecyclerView() {
        MainRecyclerAdapter adapter = new MainRecyclerAdapter
                (MainActivity.this, recipeArrayList, MainActivity.this);

        recipeRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,
                calculateNoOfColumns(MainActivity.this)));
        progressBar.setVisibility(View.INVISIBLE);
        recipeRecyclerView.setVisibility(View.VISIBLE);
        myMenu.getItem(0).setVisible(false);
        recipeRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setVisible(false);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.retry) {
            createRecipeApi();
            apiJson.getRecipes().enqueue(recipeCallback);
            progressBar.setVisibility(View.VISIBLE);
        }

        return true;
    }
}
