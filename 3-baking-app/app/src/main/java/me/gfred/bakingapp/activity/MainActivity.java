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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gfred.bakingapp.R;
import me.gfred.bakingapp.adapter.MainRecyclerAdapter;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.service.IngredientIntentService;
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

    @BindView(R.id.retry_button)
    Button retryButton;

    List<Recipe> recipeArrayList;


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
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                retryButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings_menu) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            if (recipeArrayList != null) {
                ArrayList<String> entriesName = new ArrayList<>();
                for (Recipe recipe : recipeArrayList) {
                    entriesName.add(recipe.getName());
                }
                intent.putExtra("entries", entriesName);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);

    }

    Callback<List<Recipe>> recipeCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
            recipeArrayList = response.body();
            inflateRecyclerView();
            IngredientIntentService.startActionUpdateIngredients(MainActivity.this);
        }

        @Override
        public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
            t.printStackTrace();
            progressBar.setVisibility(View.INVISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Error loading recipes...", Toast.LENGTH_SHORT).show();

        }
    };
    private ApiJson apiJson;

    static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 270;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 1;
        return (int) Math.floor(noOfColumns);
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
        retryButton.setVisibility(View.INVISIBLE);
        recipeRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.retry_button)
    void retryClick() {
        createRecipeApi();
        apiJson.getRecipes().enqueue(recipeCallback);
        retryButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
