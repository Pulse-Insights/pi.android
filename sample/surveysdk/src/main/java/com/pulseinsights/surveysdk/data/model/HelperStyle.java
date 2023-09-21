package com.pulseinsights.surveysdk.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.HelperTheme;

public class HelperStyle {
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

    public HelperTheme toTheme() {
        HelperTheme helperTheme = new HelperTheme();
        helperTheme.margin = margin;
        helperTheme.topMargin =
                topMargin > -1 ? topMargin : helperTheme.topMargin;
        helperTheme.bottomMargin =
                bottomMargin > -1 ? bottomMargin : helperTheme.bottomMargin;
        helperTheme.padding = !TextUtils.isEmpty(padding) ? Integer.parseInt(padding) : 0;
        helperTheme.paddingHorizontal = !TextUtils.isEmpty(paddingHorizontal) ? Integer.parseInt(paddingHorizontal) : 0;
        helperTheme.paddingVertical = !TextUtils.isEmpty(paddingVertical) ? Integer.parseInt(paddingVertical) : 0;
        helperTheme.gravity = gravity;
        helperTheme.fontColor = fontColor;
        return helperTheme;
    }
}
