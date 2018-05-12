package me.gfred.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import java.util.List;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.util.ApiJson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static Recipe currentRecipe;
    static String someString;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        someString = preferences.getString("ingredients", "None");

        createRecipeApi();
        apiJson.getRecipes().enqueue(recipeCallback);
        System.out.println("sheisse");

        if (currentRecipe != null) {
            views.setTextViewText(R.id.widget_recipe_name, currentRecipe.getName());
            views.setTextViewText(R.id.widget_recipe_ingredients, stringifyIngredients(currentRecipe.getIngredients()));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static String stringifyIngredients(List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Ingredient n : ingredients) {
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

        return builder.toString();
    }

    public static void createRecipeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiJson.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiJson = retrofit.create(ApiJson.class);
    }

    static Callback<List<Recipe>> recipeCallback = new Callback<List<Recipe>>() {

        @Override
        public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
            for (Recipe recipe : response.body()) {
                if (recipe.getName() == someString) {
                    currentRecipe = recipe;
                    break;
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

        }
    };

    private static ApiJson apiJson;
}

