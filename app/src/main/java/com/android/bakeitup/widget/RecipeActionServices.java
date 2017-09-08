package com.android.bakeitup.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.android.bakeitup.objects.Recipe;

public class RecipeActionServices extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGET =
            "com.android.bakeitup.action.share_ingredients";
    public static final String EXTRA_RECIPE =
            "com.android.bakeitup.extra.recipe";

    public RecipeActionServices() {
        super("RecipeActionServices");
    }

    public static void startActionUpdateWidget(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeActionServices.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGET.equals(action)) {
                Recipe recipe = (Recipe) intent.getSerializableExtra(EXTRA_RECIPE);
                handleActionUpdateRecipeWidget(recipe);
            }
        }
    }

    private void handleActionUpdateRecipeWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}
