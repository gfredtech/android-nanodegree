package me.gfred.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    static ArrayList<Recipe> recipeArrayList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        if (recipeArrayList != null) {
            Recipe currentRecipe = currentRecipeInPreference(context);
            Toast.makeText(context, "GfredTech", Toast.LENGTH_LONG).show();
            if (currentRecipe != null) {
                views.setTextViewText(R.id.widget_recipe_name, currentRecipe.getName());
                views.setTextViewText(R.id.widget_recipe_ingredients, stringifyIngredients(currentRecipe.getIngredients()));
            }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.hasExtra("recipes")) {
            if (recipeArrayList == null)
                recipeArrayList = intent.getParcelableArrayListExtra("recipes");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), IngredientWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
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

    static String stringifyIngredients(List<Ingredient> ingredients) {
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


    static Recipe currentRecipeInPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String type = preferences.getString("ingredient", "None");
        for (Recipe recipe : recipeArrayList) {
            if (recipe.getId().equals(Integer.valueOf(type))) {
                return recipe;
            }
        }

        return null;
    }


}

