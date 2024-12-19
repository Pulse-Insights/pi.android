package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.SubmitBtnTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class SubmitBtnStyle {
    @SerializedName("background-color")
    String backgroundColor = null;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    @SerializedName("font-size")
    int fontSize = -1;

    @SerializedName("font-face")
    String fontFace = null;

    @SerializedName("font-color")
    String fontColor = null;

    @SerializedName("border-radius")
    int borderRadius = -1;

    @SerializedName("padding-horizontal")
    int paddingHorizontal = -1;

    @SerializedName("padding-vertical")
    int paddingVertical = -1;

    int width = -1;
    int height = -1;
    int padding = -1;
    int margin = -1;

    @SerializedName("horizontal-align")
    String horizonAlign = null;

    public SubmitBtnTheme toTheme() {
        SubmitBtnTheme submitBtnTheme = new SubmitBtnTheme();
        submitBtnTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, submitBtnTheme.backgroundColor);
        submitBtnTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, submitBtnTheme.borderColor);
        submitBtnTheme.borderWidth = borderWidth > -1 ? borderWidth : submitBtnTheme.borderWidth;
        submitBtnTheme.fontSize = fontSize > -1 ? fontSize : submitBtnTheme.fontSize;
        submitBtnTheme.fontFace =
                new ParseHelper<String>().getObj(fontFace, submitBtnTheme.fontFace);
        submitBtnTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, submitBtnTheme.fontColor);
        submitBtnTheme.width = width > -1 ? width : submitBtnTheme.width;
        submitBtnTheme.height = height > -1 ? height : submitBtnTheme.height;
        submitBtnTheme.padding = padding > -1 ? padding : submitBtnTheme.padding;
        submitBtnTheme.margin = margin > -1 ? margin : submitBtnTheme.margin;
        submitBtnTheme.horizonAlign =
                new ParseHelper<String>().getObj(horizonAlign, submitBtnTheme.horizonAlign);
        submitBtnTheme.borderRadius = borderRadius > -1 ? borderRadius : submitBtnTheme.borderRadius;
        submitBtnTheme.paddingVertical = paddingVertical > -1 ? paddingVertical : submitBtnTheme.paddingVertical;
        submitBtnTheme.paddingHorizontal = paddingHorizontal > -1 ? paddingHorizontal : submitBtnTheme.paddingHorizontal;
        return submitBtnTheme;
    }

}
