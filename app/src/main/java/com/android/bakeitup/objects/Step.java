package com.android.bakeitup.objects;

import java.io.Serializable;

public class Step implements Serializable {

    private int mStepId;
    private String mStepShortDescription;
    private String mStepDescription;
    private String mStepVideoUrl;
    private String mStepThumbnailUrl;

    public int getStepId() {
        return mStepId;
    }

    public void setStepId(int stepId) {
        mStepId = stepId;
    }

    public String getStepShortDescription() {
        return mStepShortDescription;
    }

    public void setStepShortDescription(String stepShortDescription) {
        mStepShortDescription = stepShortDescription;
    }

    public String getStepDescription() {
        return mStepDescription;
    }

    public void setStepDescription(String stepDescription) {
        mStepDescription = stepDescription;
    }

    public String getStepVideoUrl() {
        return mStepVideoUrl;
    }

    public void setStepVideoUrl(String stepVideoUrl) {
        mStepVideoUrl = stepVideoUrl;
    }

    public String getStepThumbnailUrl() {
        return mStepThumbnailUrl;
    }

    public void setStepThumbnailUrl(String stepThumbnailUrl) {
        mStepThumbnailUrl = stepThumbnailUrl;
    }
}