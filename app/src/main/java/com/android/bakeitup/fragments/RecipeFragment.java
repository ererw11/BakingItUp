package com.android.bakeitup.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.android.bakeitup.R;
import com.android.bakeitup.RecipeFetcher;
import com.android.bakeitup.activities.StepListIngredientActivity;
import com.android.bakeitup.adapters.RecipeAdapter;
import com.android.bakeitup.objects.Recipe;
import com.android.bakeitup.widget.RecipeActionServices;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecyclerView mRecipeRecyclerView;
    private List<Recipe> mRecipes = new ArrayList<>();

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchRecipeTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecipeRecyclerView = v.findViewById(R.id.recipe_recycler_view);

        RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600 || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setHasFixedSize(true);

        setupAdapter();

        return v;
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecipeRecyclerView.setAdapter(new RecipeAdapter(getContext(), mRecipes, this));
            runLayoutAnimation(mRecipeRecyclerView);
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Context context = getContext();
        createRecipeIntent(context, recipe);
    }

    public void createRecipeIntent(Context context, Recipe recipe) {
        Intent recipeIntent = StepListIngredientActivity.newIntent(context, recipe);
        RecipeActionServices.startActionUpdateWidget(context, recipe);
        startActivity(recipeIntent);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchRecipeTask extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(Void... params) {
            return new RecipeFetcher().fetchRecipes();
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            mRecipes = recipes;
            setupAdapter();
        }
    }
}
