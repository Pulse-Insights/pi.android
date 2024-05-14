package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.QuestionTextTheme;

public class QuestionTextStyle {
    int margin = -1;

    @SerializedName("top-margin")
    int topMargin = -1;

    @SerializedName("bottom-margin")
    int bottomMargin = -1;

    public QuestionTextTheme toTheme() {
        QuestionTextTheme questionTextTheme = new QuestionTextTheme();
        questionTextTheme.margin = margin > -1 ? margin : questionTextTheme.margin;
        questionTextTheme.topMargin =
                topMargin > -1 ? topMargin : questionTextTheme.topMargin;
        questionTextTheme.bottomMargin =
                bottomMargin > -1 ? bottomMargin : questionTextTheme.bottomMargin;
        return questionTextTheme;
    }
}
