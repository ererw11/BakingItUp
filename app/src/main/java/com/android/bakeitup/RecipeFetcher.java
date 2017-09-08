package com.android.bakeitup;

import android.net.Uri;
import android.util.Log;

import com.android.bakeitup.objects.Ingredient;
import com.android.bakeitup.objects.Recipe;
import com.android.bakeitup.objects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeFetcher {

    final static String R_ID = "id";
    final static String R_NAME = "name";
    final static String R_INGREDIENTS = "ingredients";
    final static String I_QUANTITY = "quantity";
    final static String I_MEASURE = "measure";
    final static String I_INGREDIENT = "ingredient";
    final static String R_STEPS = "steps";
    final static String S_ID = "id";
    final static String S_SHORT_DESCRIPTION = "shortDescription";
    final static String S_DESCRIPTION = "description";
    final static String S_VIDEO_URL = "videoURL";
    final static String S_THUMBNAIL_URL = "thumbnailURL";
    final static String R_SERVINGS = "servings";
    final static String R_IMAGE = "image";
    private static final String TAG = RecipeFetcher.class.getSimpleName();
    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Recipe> fetchRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        try {
            String builtUri = Uri.parse(RECIPE_URL).buildUpon().build().toString();
            String jsonString = getUrlString(builtUri);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseRecipes(recipes, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return recipes;
    }

    private void parseRecipes(List<Recipe> recipes, JSONArray jsonBody) throws IOException, JSONException {

        for (int i = 0; i < jsonBody.length(); i++) {

            List<Ingredient> recipeIngredients;
            List<Step> recipeSteps;

            JSONObject currentRecipe = jsonBody.getJSONObject(i);

            Recipe recipe = new Recipe();
            recipe.setRecipeId(currentRecipe.getInt(R_ID));
            recipe.setRecipeName(currentRecipe.getString(R_NAME));

            // Ingredient Array
            recipeIngredients = new ArrayList<>();
            JSONArray ingredients = currentRecipe.getJSONArray(R_INGREDIENTS);
            for (int ii = 0; ii < ingredients.length(); ii++) {

                JSONObject currentIngredient = ingredients.getJSONObject(ii);

                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientQuantity(currentIngredient.getDouble(I_QUANTITY));
                ingredient.setIngredientMeasure(currentIngredient.getString(I_MEASURE));
                ingredient.setIngredient(currentIngredient.getString(I_INGREDIENT));

                recipeIngredients.add(ingredient);
            }
            recipe.setRecipeIngredients(recipeIngredients);

            // Step Array
            recipeSteps = new ArrayList<>();
            JSONArray steps = currentRecipe.getJSONArray(R_STEPS);
            for (int iii = 0; iii < steps.length(); iii++) {

                JSONObject currentStep = steps.getJSONObject(iii);

                Step step = new Step();
                step.setStepId(currentStep.getInt(S_ID));
                step.setStepShortDescription(currentStep.getString(S_SHORT_DESCRIPTION));
                step.setStepDescription(currentStep.getString(S_DESCRIPTION));
                step.setStepVideoUrl(currentStep.getString(S_VIDEO_URL));
                step.setStepThumbnailUrl(currentStep.getString(S_THUMBNAIL_URL));

                recipeSteps.add(step);
            }
            recipe.setRecipeSteps(recipeSteps);

            recipe.setRecipeServings(currentRecipe.getInt(R_SERVINGS));
            recipe.setRecipeImage(currentRecipe.getString(R_IMAGE));

            recipes.add(recipe);
        }
    }
}
