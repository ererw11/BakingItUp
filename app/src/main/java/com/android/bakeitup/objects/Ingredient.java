package com.android.bakeitup.objects;


import java.io.Serializable;

public class Ingredient implements Serializable {

    private double mIngredientQuantity;
    private String mIngredientMeasure;
    private String mIngredient;

    // Getters and Setters
    public double getIngredientQuantity() {
        return mIngredientQuantity;
    }

    public void setIngredientQuantity(double ingredientQuantity) {
        mIngredientQuantity = ingredientQuantity;
    }

    public String getIngredientMeasure() {
        return mIngredientMeasure;
    }

    public void setIngredientMeasure(String ingredientMeasure) {
        mIngredientMeasure = ingredientMeasure;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }
}