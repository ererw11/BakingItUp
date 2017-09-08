package com.android.bakeitup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakeitup.R;
import com.android.bakeitup.activities.StepListIngredientActivity;
import com.android.bakeitup.objects.Ingredient;
import com.android.bakeitup.objects.Recipe;
import com.android.bakeitup.objects.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepListAndIngredientsFragment extends Fragment {

    private static Recipe mRecipe;
    @BindView(R.id.ingredients_header)
    TextView mIngredientsHeader;
    @BindView(R.id.ingredients_list_text_view)
    TextView mIngredientsListTextView;
    private Unbinder unbinder;
    private Bundle mRecipeBundle;
    private RecyclerView mStepListRecyclerView;
    private StepListAdapter mStepListAdapter;
    private List<Step> mStepList = new ArrayList<>();
    private Intent mIntentThatStartedThisActivity;
    private Callbacks mCallbacks;

    public static Recipe getRecipe() {
        if (mRecipe != null) {
            return mRecipe;
        }
        return new Recipe();
    }

    public static String writeIngredientString(List<Ingredient> ingredientList) {
        StringBuilder ingredientStringBuilder = new StringBuilder();
        for (Ingredient ingredientDetails : ingredientList) {
            String qua = Double.toString(ingredientDetails.getIngredientQuantity()) + " ";
            String mea = ingredientDetails.getIngredientMeasure() + " ";
            String ing = ingredientDetails.getIngredient();
            String ingredientLine = qua + mea + ing;

            ingredientStringBuilder.append(ingredientLine + "\n");
        }
        return ingredientStringBuilder.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        mStepListRecyclerView = (RecyclerView) v.
                findViewById(R.id.step_recycler_view);
        mStepListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIntentThatStartedThisActivity = getActivity().getIntent();
        mRecipeBundle = mIntentThatStartedThisActivity.getExtras();
        updateUI(mRecipeBundle);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(mRecipeBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void updateUI(Bundle recipeBundle) {
        if (recipeBundle != null) {
            List<Ingredient> ingredientList = new ArrayList<>();
            mRecipe = (Recipe) mIntentThatStartedThisActivity.getSerializableExtra(StepListIngredientActivity.EXTRA_RECIPE);
            mStepList = mRecipe.getRecipeSteps();

            String ingredientHeader = getString(R.string.ingredient_header_base) + " " + mRecipe.getRecipeName();
            mIngredientsHeader.setText(ingredientHeader);

            ingredientList = mRecipe.getRecipeIngredients();

            mIngredientsListTextView.setText(writeIngredientString(ingredientList));
        }

        List<Step> steps = mStepList;

        if (mStepListAdapter == null) {
            mStepListAdapter = new StepListAdapter(steps);
            mStepListRecyclerView.setAdapter(mStepListAdapter);
        } else {
            mStepListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_ingredient_and_step_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_recipe_ingredients:
                shareIngredients();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareIngredients() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, writeIngredientString(mRecipe.getRecipeIngredients()));
        intent.putExtra(Intent.EXTRA_SUBJECT, mRecipe.getRecipeName()); //
        intent = Intent.createChooser(intent, getString(R.string.share_ingredients_via));
        startActivity(intent);
    }

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onStepSelected(Recipe recipe, int stepId);
    }

    private class StepListHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mStepShortDescriptionTextView;

        private Step mStep;

        public StepListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.step_list_item, parent, false));
            itemView.setOnClickListener(this);

            mStepShortDescriptionTextView = (TextView) itemView.findViewById(R.id.step_short_description_text_view);
        }

        public void bind(Step step) {
            mStep = step;
            mStepShortDescriptionTextView.setText(mStep.getStepShortDescription());
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onStepSelected(mRecipe, mStep.getStepId());
        }
    }

    private class StepListAdapter extends RecyclerView.Adapter<StepListHolder> {

        private List<Step> mSteps;

        public StepListAdapter(List<Step> steps) {
            mSteps = steps;
        }

        @Override
        public StepListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new StepListHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StepListHolder holder, int position) {
            Step step = mSteps.get(position);
            holder.bind(step);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }
    }
}
