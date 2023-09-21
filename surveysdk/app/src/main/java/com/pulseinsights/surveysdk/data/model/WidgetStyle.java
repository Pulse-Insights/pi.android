package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.WidgetTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class WidgetStyle {

    @SerializedName("background-color")
    String backgroundColor = null;
    int padding = -1;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    public WidgetTheme toTheme() {
        WidgetTheme widgetTheme = new WidgetTheme();
        widgetTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, widgetTheme.backgroundColor);
        widgetTheme.padding = padding > -1 ? padding : widgetTheme.padding;
        widgetTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, widgetTheme.borderColor);
        widgetTheme.borderWidth = borderWidth > -1 ? borderWidth : widgetTheme.borderWidth;
        return widgetTheme;
    }
}
