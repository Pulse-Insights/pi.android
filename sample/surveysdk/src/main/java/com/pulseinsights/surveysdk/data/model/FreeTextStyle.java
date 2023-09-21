package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.FreeTextTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class FreeTextStyle {
    @SerializedName("background-color")
    String backgroundColor = null;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    @SerializedName("font-color")
    String fontColor = null;

    @SerializedName("max-lines")
    int maxLines = -1;

    @SerializedName("placeholder-font-color")
    String placeholderFontColor = null;

    public FreeTextTheme toTheme() {
        FreeTextTheme freeTextTheme = new FreeTextTheme();
        freeTextTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, freeTextTheme.backgroundColor);
        freeTextTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, freeTextTheme.borderColor);
        freeTextTheme.borderWidth = borderWidth > -1 ? borderWidth : freeTextTheme.borderWidth;
        freeTextTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, freeTextTheme.fontColor);
        freeTextTheme.maxLines = maxLines > -1 ? maxLines : freeTextTheme.maxLines;
        freeTextTheme.placeholderFontColor = new ParseHelper<String>()
                .getObj(placeholderFontColor, freeTextTheme.placeholderFontColor);
        return freeTextTheme;
    }

}
