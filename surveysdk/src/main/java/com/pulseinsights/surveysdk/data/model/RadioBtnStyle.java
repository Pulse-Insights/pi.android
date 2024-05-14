package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.RadioBtnTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class RadioBtnStyle {
    @SerializedName("background-color")
    String backgroundColor = null;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    boolean hide = false;

    int width = -1;
    int height = -1;
    int padding = -1;
    int margin = -100;

    public RadioBtnTheme toTheme() {
        RadioBtnTheme radioBtnTheme = new RadioBtnTheme();
        radioBtnTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, radioBtnTheme.backgroundColor);
        radioBtnTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, radioBtnTheme.borderColor);
        radioBtnTheme.borderWidth = borderWidth > -1 ? borderWidth : radioBtnTheme.borderWidth;
        radioBtnTheme.width = width > -1 ? width : radioBtnTheme.width;
        radioBtnTheme.height = height > -1 ? height : radioBtnTheme.height;
        radioBtnTheme.padding = padding > -1 ? padding : radioBtnTheme.padding;
        radioBtnTheme.margin = margin != -100 ? margin : radioBtnTheme.margin;
        radioBtnTheme.hide = hide;
        return radioBtnTheme;
    }
}
