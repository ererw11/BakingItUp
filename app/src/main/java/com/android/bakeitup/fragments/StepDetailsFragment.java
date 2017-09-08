package com.android.bakeitup.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakeitup.R;
import com.android.bakeitup.objects.Recipe;
import com.android.bakeitup.objects.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailsFragment extends Fragment {

    private static final String TAG = StepListAndIngredientsFragment.class.getSimpleName();

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEP_ID = "step_id";

    @BindView(R.id.recipe_short_description_text_view)
    TextView mShortDescriptionTextView;
    @BindView(R.id.recipe_description_text_view)
    TextView mDescriptionTextView;
    @BindView(R.id.swipe_for_next_step_text_view)
    TextView mSwipeForNextPageTextView;
    @BindView(R.id.step_details_exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    private Unbinder mUnbinder;

    private int mStepID;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerPosition;

    public static StepDetailsFragment newInstance(Recipe recipe, int stepId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE, recipe);
        args.putSerializable(ARG_STEP_ID, stepId);

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Recipe recipe = (Recipe) getArguments().getSerializable(ARG_RECIPE);
        List<Step> stepList = recipe.getRecipeSteps();
        mStepID = getArguments().getInt(ARG_STEP_ID);
        mStep = (Step) stepList.get(mStepID);

        if (savedInstanceState != null) {
            mExoPlayerPosition = savedInstanceState.getLong("current_position");
            stopExoPlayerOnViewPagerChange(savedInstanceState.getInt("current_step"));
        } else {
            mExoPlayerPosition = 0;
        }
    }

    private void stopExoPlayerOnViewPagerChange(Integer currentStep) {
        if (mStepID != currentStep) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_step_details, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        mShortDescriptionTextView.setText(mStep.getStepShortDescription());
        mDescriptionTextView.setText(mStep.getStepDescription());

        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), R.drawable.bake_it_up_android_man));

        if (mStep.getStepVideoUrl() != null) {
            initializePlayer(Uri.parse(mStep.getStepVideoUrl()));
        } else {
            initializePlayer(Uri.parse(mStep.getStepVideoUrl()));
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(mStep.getStepVideoUrl()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeExoPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        removeExoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeExoPlayer();
    }

    public void removeExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeExoPlayer();
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri Uri of the Step video
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of ExoPlayer
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), selector, control);
            mExoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mExoPlayerPosition);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mExoPlayerPosition = mExoPlayer.getCurrentPosition();
        outState.putLong("current_position", mExoPlayerPosition);
        outState.putInt("current_step", mStepID);
    }
}
