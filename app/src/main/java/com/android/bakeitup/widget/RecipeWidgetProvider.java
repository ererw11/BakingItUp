package com.android.bakeitup.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.bakeitup.R;
import com.android.bakeitup.activities.RecipeActivity;
import com.android.bakeitup.activities.StepListIngredientActivity;
import com.android.bakeitup.fragments.StepListAndIngredientsFragment;
import com.android.bakeitup.objects.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    // setImageViewResource to update the widget’s image
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int appWidgetId) {
        Intent intent;

        // Construct the RemoteViews object
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        if (recipe != null) {
            // Get the Recipe Title
            String recipeTitle = recipe.getRecipeName().toString();
            // Update Recipe Title
            view.setTextViewText(R.id.widget_recipe_text_view, recipeTitle);

            // Get the ingredients from the current recipe
            String currentRecipeIngredients = StepListAndIngredientsFragment.writeIngredientString(recipe.getRecipeIngredients());
            // Update Ingredients List
            view.setTextViewText(R.id.widget_ingredients_list_text_view, currentRecipeIngredients);

            intent = StepListIngredientActivity.newIntent(context, recipe);
        } else {
            view.setTextViewText(R.id.widget_ingredients_list_text_view, context.getString(R.string.widget_blank_text));
            intent = new Intent(context, RecipeActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        view.setOnClickPendingIntent(R.id.widget_ingredients_list_text_view, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
