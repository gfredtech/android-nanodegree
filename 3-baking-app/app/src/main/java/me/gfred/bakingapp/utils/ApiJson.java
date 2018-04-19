package me.gfred.bakingapp.utils;

import java.util.List;

import me.gfred.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiJson {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
