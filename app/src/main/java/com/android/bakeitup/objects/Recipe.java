package com.android.bakeitup.objects;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private int mRecipeId;
    private String mRecipeName;
    private List<Ingredient> mRecipeIngredients;
    private List<Step> mRecipeSteps;
    private int mRecipeServings;
    private String mRecipeImage;

    // Getters and Setters
    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public List<Ingredient> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    public void setRecipeIngredients(List<Ingredient> recipeIngredients) {
        mRecipeIngredients = recipeIngredients;
    }

    public List<Step> getRecipeSteps() {
        return mRecipeSteps;
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    public int getRecipeServings() {
        return mRecipeServings;
    }

    public void setRecipeServings(int recipeServings) {
        mRecipeServings = recipeServings;
    }

    public String getRecipeImage() {
        return mRecipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        mRecipeImage = recipeImage;
    }
}