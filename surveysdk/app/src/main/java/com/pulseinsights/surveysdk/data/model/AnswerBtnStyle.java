package com.pulseinsights.surveysdk.data.model;


import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.AnswerBtnTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class AnswerBtnStyle {
    @SerializedName("background-color")
    String backgroundColor = null;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    @SerializedName("font-color")
    String fontColor = null;

    @SerializedName("tab-effect")
    Boolean tabEffect = false;

    @SerializedName("tab-effect-text-color")
    String tabEffectTextColor = null;

    @SerializedName("tab-effect-background-color")
    String tabEffectBackgroundColor = null;

    @SerializedName("tab-effect-border-color")
    String tabEffectBorderColor = null;

    @SerializedName("per-row-multi-selected-background-color")
    String perRowBackgroundColor = null;

    @SerializedName("padding-horizontal")
    int paddingHorizontal = -1;

    @SerializedName("padding-vertical")
    int paddingVertical = -1;

    int width = -1;
    int height = -1;
    int padding = -1;
    int margin = -1;

    public AnswerBtnTheme toTheme() {
        AnswerBtnTheme answerBtnTheme = new AnswerBtnTheme();
        answerBtnTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, answerBtnTheme.backgroundColor);
        answerBtnTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, answerBtnTheme.borderColor);
        answerBtnTheme.borderWidth = borderWidth > -1 ? borderWidth : answerBtnTheme.borderWidth;
        answerBtnTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, answerBtnTheme.fontColor);
        answerBtnTheme.width = width > -1 ? width : answerBtnTheme.width;
        answerBtnTheme.height = height > -1 ? height : answerBtnTheme.height;
        answerBtnTheme.padding = padding > -1 ? padding : answerBtnTheme.padding;
        answerBtnTheme.margin = margin > -1 ? margin : answerBtnTheme.margin;
        answerBtnTheme.tabEffect = tabEffect == null ? answerBtnTheme.tabEffect : tabEffect;
        answerBtnTheme.tabEffectTextColor = new ParseHelper<String>()
                .getObj(tabEffectTextColor, answerBtnTheme.tabEffectTextColor);
        answerBtnTheme.tabEffectBackgroundColor = new ParseHelper<String>()
                .getObj(tabEffectBackgroundColor, answerBtnTheme.tabEffectBackgroundColor);
        answerBtnTheme.tabEffectBorderColor = new ParseHelper<String>()
                .getObj(tabEffectBorderColor, answerBtnTheme.tabEffectBorderColor);
        answerBtnTheme.perRowBackgroundColor = new ParseHelper<String>()
                .getObj(perRowBackgroundColor, answerBtnTheme.perRowBackgroundColor);
        answerBtnTheme.paddingVertical = paddingVertical > -1 ? paddingVertical : answerBtnTheme.paddingVertical;
        answerBtnTheme.paddingHorizontal = paddingHorizontal > -1 ? paddingHorizontal : answerBtnTheme.paddingHorizontal;
        return answerBtnTheme;
    }
}
