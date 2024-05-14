package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.QuestionErrorTextTheme;

public class QuestionErrorTextStyle {
    @SerializedName("font-color")
    String fontColor = null;

    @SerializedName("top-margin")
    int topMargin = -1;

    @SerializedName("bottom-margin")
    int bottomMargin = -1;

    public QuestionErrorTextTheme toTheme() {
        QuestionErrorTextTheme questionErrorTextTheme = new QuestionErrorTextTheme();
        questionErrorTextTheme.fontColor = fontColor;
        questionErrorTextTheme.topMargin = topMargin > -1 ? topMargin : questionErrorTextTheme.topMargin;
        questionErrorTextTheme.bottomMargin = bottomMargin > -1 ? bottomMargin : questionErrorTextTheme.bottomMargin;
        return questionErrorTextTheme;
    }
}
