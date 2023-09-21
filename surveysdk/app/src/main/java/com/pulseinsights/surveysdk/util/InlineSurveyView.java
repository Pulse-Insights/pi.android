package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.util.AttributeSet;

import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.PulseInsights;

public class InlineSurveyView extends SurveyMainView {

    private boolean viewOnDisplay = false;

    public InlineSurveyView(Context context) {
        super(context);
        this.switchInlineType(true);
    }

    public InlineSurveyView(Context context, String trackId) {
        super(context);
        this.switchInlineType(true);
        this.setIdentifier(trackId);
    }

    public InlineSurveyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.switchInlineType(true);
    }

    public void setIdentifier(String identify) {
        //When this class is running with inline Type and
        //the trackIdentify assigned with non-empty,
        //the logic will submit the callback object with the track identify
        // as the key for the main PI object call
        if (!trackIdentify.isEmpty()) {
            LocalData.instant.inlineLink.remove(trackIdentify);
        }
        this.trackIdentify = identify;
        this.inlineEnable = !identify.isEmpty();
        if (inlineEnable && inlineType) {
            LocalData.instant.inlineLink.put(identify, new SurveyInlineResult() {
                @Override
                public void onServeResult() {
                    setupSurveyContent(LocalData.instant.surveyTickets);
                }

                @Override
                public void onFinish() {
                    finish();
                }

                @Override
                public boolean onDisplay() {
                    return viewOnDisplay;
                }
            });
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // When this view onDisplay on the screen, following logic lines will be executed:
        // 1. Indicate this view is onDisplayed,
        //    so the main PI object can know if it can forward the survey data
        // 2. Close the view before the main PI object fetch and forward the survey data
        viewOnDisplay = true;
        closeView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewOnDisplay = false;
        //In case the user left the view without close the unfinished inline survey
        PulseInsights.getInstant().finishInlineMode();
    }
}
