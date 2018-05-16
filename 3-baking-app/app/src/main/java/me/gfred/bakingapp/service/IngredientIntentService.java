package me.gfred.bakingapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class IngredientIntentService extends IntentService {

    public static final String ACTION_INGREDIENT_UPDATE = "me.gfred.bakingapp.CUSTOM_INTENT";

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

    }
}
