package me.gfred.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.utils.ApiJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainRecyclerAdapter.RecipeClickListener {

    @BindView(R.id.recipe_rv)
    RecyclerView recipeRecyclerView;
    List<Recipe> recipeArrayList;

    private ApiJson apiJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createRecipeApi();
        apiJson.getRecipes().enqueue(recipeCallback);

    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }

    public void createRecipeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiJson.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        apiJson = retrofit.create(ApiJson.class);
    }



        Callback<List<Recipe>> recipeCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
            recipeArrayList = response.body();

            MainRecyclerAdapter adapter = new MainRecyclerAdapter
                    (MainActivity.this, recipeArrayList, MainActivity.this);

            recipeRecyclerView.setLayoutManager(new LinearLayoutManager
                    (MainActivity.this, LinearLayoutManager.VERTICAL, false));
            recipeRecyclerView.setAdapter(adapter);
        }

        @Override
        public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
            t.printStackTrace();
        }
    };
}
