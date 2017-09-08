package com.android.bakeitup.activities;

import android.support.v4.app.Fragment;

import com.android.bakeitup.fragments.RecipeFragment;

public class RecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return RecipeFragment.newInstance();
    }

}
