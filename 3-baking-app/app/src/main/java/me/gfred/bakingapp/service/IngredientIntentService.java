package me.gfred.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.util.ApiJson;
import me.gfred.bakingapp.widget.IngredientWidget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientIntentService extends IntentService {

    public static final String ACTION_INGREDIENT_UPDATE = "me.gfred.bakingapp.ACTION_INGREDIENT_UPDATE";

    public IngredientIntentService() {
        super("IngredientIntentService");
    }

    public static void startActionUpdateIngredients(Context context) {
        Intent intent = new Intent(context, IngredientIntentService.class);
        intent.setAction(ACTION_INGREDIENT_UPDATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INGREDIENT_UPDATE.equals(action)) {
                handleIngredientUpdate();
            }
        }
    }

    private void handleIngredientUpdate() {
        createRecipeApi();
        apiJson.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                Recipe currentRecipe = null;
                if (recipes != null) {
                    currentRecipe = currentRecipeInPreference(recipes, IngredientIntentService.this);
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(IngredientIntentService.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(IngredientIntentService.this,
                        IngredientWidget.class));

                IngredientWidget.updateAppWidgets(IngredientIntentService.this, appWidgetManager, currentRecipe, appWidgetIds);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void createRecipeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiJson.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiJson = retrofit.create(ApiJson.class);
    }

    private ApiJson apiJson;

    static Recipe currentRecipeInPreference(List<Recipe> recipeArrayList, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String type = preferences.getString("ingredient", "None");
        if (type.equals("None")) return null;
        for (Recipe recipe : recipeArrayList) {
            if (recipe.getId().equals(Integer.valueOf(type))) {
                return recipe;
            }
        }

        return null;
    }
}
