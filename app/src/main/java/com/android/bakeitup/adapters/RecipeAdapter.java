package com.android.bakeitup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bakeitup.R;
import com.android.bakeitup.objects.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private final RecipeAdapterOnClickHandler mClickHandler;
    private List<Recipe> mRecipeItems;
    private Context mContext;

    public RecipeAdapter(Context context, List<Recipe> recipeItems, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mRecipeItems = recipeItems;
        mClickHandler = clickHandler;
    }

    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, viewGroup, false);
        return new RecipeAdapter.RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {
        Recipe recipe = mRecipeItems.get(position);
        String recipeTitle = recipe.getRecipeName();
        int recipeServings = recipe.getRecipeServings();
        String recipeImage = recipe.getRecipeImage();
        holder.bindRecipe(recipeTitle, recipeServings, recipeImage);
    }

    @Override
    public int getItemCount() {
        if (mRecipeItems == null) {
            return 0;
        }
        return mRecipeItems.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRecipeTitleTextView;
        private TextView mRecipeServingTextView;
        private ImageView mRecipeImageView;

        public RecipeHolder(View itemView) {
            super(itemView);

            mRecipeTitleTextView = (TextView) itemView.findViewById(R.id.recipe_card_title);
            mRecipeServingTextView = (TextView) itemView.findViewById(R.id.recipe_serving_number);
            mRecipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindRecipe(String recipeName, int recipeServings, String recipeImage) {
            mRecipeTitleTextView.setText(recipeName);
            mRecipeServingTextView.setText("Serves " + Integer.toString(recipeServings));
            if (recipeImage.equals("")) {
                Picasso.with(mContext)
                        .load(R.drawable.bake_it_up_android_man)
                        .into(mRecipeImageView);
            } else {
                Picasso.with(mContext)
                        .load(recipeImage)
                        .into(mRecipeImageView);
            }

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe selectedRecipe = mRecipeItems.get(adapterPosition);
            mClickHandler.onClick(selectedRecipe);

        }
    }
}