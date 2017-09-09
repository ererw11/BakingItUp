package com.android.bakeitup.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.bakeitup.R;
import com.android.bakeitup.fragments.StepDetailsFragment;
import com.android.bakeitup.fragments.StepListAndIngredientsFragment;
import com.android.bakeitup.objects.Recipe;

public class StepListIngredientActivity extends SingleFragmentActivity
        implements StepListAndIngredientsFragment.Callbacks {

    public static final String EXTRA_RECIPE =
            "com.android.bakeitup.extra.recipe";

    public static Intent newIntent(Context packageContext, Recipe recipe) {
        Intent intent = new Intent(packageContext, StepListIngredientActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new StepListAndIngredientsFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onStepSelected(Recipe recipe, int stepId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = StepDetailPagerActivity.newIntent(this, recipe, stepId);
            startActivity(intent);
        } else {
            Fragment newDetail = StepDetailsFragment.newInstance(recipe, stepId);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
