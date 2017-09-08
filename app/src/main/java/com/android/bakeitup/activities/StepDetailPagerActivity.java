package com.android.bakeitup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.bakeitup.R;
import com.android.bakeitup.fragments.StepDetailsFragment;
import com.android.bakeitup.objects.Recipe;
import com.android.bakeitup.objects.Step;

import java.util.List;

public class StepDetailPagerActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE =
            "com.android.bakeitup.activities.recipe";
    private static final String EXTRA_STEP_ID =
            "com.android.bakeitup.activities.step_id";

    private ViewPager mViewPager;
    private Recipe mRecipe;
    private List<Step> mSteps;

    public static Intent newIntent(Context packageContext, Recipe recipe, int stepId) {
        Intent intent = new Intent(packageContext, StepDetailPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        intent.putExtra(EXTRA_STEP_ID, stepId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_detail_pager);

        mViewPager = (ViewPager) findViewById(R.id.step_detail_view_pager);
        mRecipe = (Recipe) getIntent()
                .getSerializableExtra(EXTRA_RECIPE);
        mSteps = mRecipe.getRecipeSteps();
        int currentStepId = getIntent().getIntExtra(EXTRA_STEP_ID, 0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {

                Step step = mSteps.get(position);
                return StepDetailsFragment.newInstance(mRecipe, step.getStepId());
            }

            @Override
            public int getCount() {
                return mSteps.size();
            }
        });

        for (int i = 0; i < mSteps.size(); i++) {
            if (mSteps.get(i).getStepId() == currentStepId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
