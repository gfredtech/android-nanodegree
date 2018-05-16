package me.gfred.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

import me.gfred.bakingapp.R;
import me.gfred.bakingapp.activity.SettingsActivity;
import me.gfred.bakingapp.model.Ingredient;
import me.gfred.bakingapp.model.Recipe;
import me.gfred.bakingapp.service.IngredientIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        Intent intent = new Intent(context, SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_ingredients, pendingIntent);


        if (recipe != null) {
            Toast.makeText(context, "GfredTech", Toast.LENGTH_LONG).show();
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
            views.setTextViewText(R.id.widget_recipe_ingredients, stringifyIngredients(recipe.getIngredients()));

        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        IngredientIntentService.startActionUpdateIngredients(context);

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


}

