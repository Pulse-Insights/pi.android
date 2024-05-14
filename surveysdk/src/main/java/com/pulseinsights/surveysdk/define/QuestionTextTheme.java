package com.pulseinsights.surveysdk.define;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;

public class QuestionTextTheme extends ViewThemeBase {
    public QuestionTextTheme() {
        margin = 0;
        topMargin = 10;
        bottomMargin = 10;
    }

    public void applyNewStyle(QuestionTextTheme newStyle) {
        this.margin = newStyle.margin;
        this.topMargin = newStyle.topMargin;
        this.bottomMargin = newStyle.bottomMargin;
    }

    public void resetStyle() {
        this.applyNewStyle(new QuestionTextTheme());
    }

}