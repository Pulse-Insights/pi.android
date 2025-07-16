package com.pulseinsights.surveysdk.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.AnswersHelperTheme;

public class AnswersHelperStyle {
    @SerializedName("container-margin")
    int margin = 0;

    @SerializedName("container-top-margin")
    int topMargin = -1;

    @SerializedName("container-bottom-margin")
    int bottomMargin = -1;

    @SerializedName("padding")
    String padding;

    @SerializedName("padding-horizontal")
    String paddingHorizontal;

    @SerializedName("padding-vertical")
    String paddingVertical;

    @SerializedName("alignment")
    String gravity = "";

    @SerializedName("font-color")
    String fontColor = null;

    public AnswersHelperTheme toTheme() {
        AnswersHelperTheme answersHelperTheme = new AnswersHelperTheme();
        answersHelperTheme.margin = margin;
        answersHelperTheme.topMargin =
                topMargin > -1 ? topMargin : answersHelperTheme.topMargin;
        answersHelperTheme.bottomMargin =
                bottomMargin > -1 ? bottomMargin : answersHelperTheme.bottomMargin;
        answersHelperTheme.padding = !TextUtils.isEmpty(padding) ? Integer.parseInt(padding) : 0;
        answersHelperTheme.paddingHorizontal = !TextUtils.isEmpty(paddingHorizontal) ? Integer.parseInt(paddingHorizontal) : 0;
        answersHelperTheme.paddingVertical = !TextUtils.isEmpty(paddingVertical) ? Integer.parseInt(paddingVertical) : 0;
        answersHelperTheme.gravity = gravity;
        answersHelperTheme.fontColor = fontColor;
        return answersHelperTheme;
    }
} 