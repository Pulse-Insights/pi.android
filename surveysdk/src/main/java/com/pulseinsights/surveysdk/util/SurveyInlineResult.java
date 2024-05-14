package com.pulseinsights.surveysdk.util;

/**
 * Created by leochao on 2018/2/27.
 */

public interface SurveyInlineResult {
    void onServeResult();

    void onFinish();

    boolean onDisplay();
}
