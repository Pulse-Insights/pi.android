package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.CheckboxTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class CheckBoxStyle {
    @SerializedName("background-color")
    String backgroundColor = null;

    @SerializedName("border-color")
    String borderColor = null;

    @SerializedName("border-width")
    int borderWidth = -1;

    int width = -1;
    int height = -1;

    public CheckboxTheme toTheme() {
        CheckboxTheme checkboxTheme = new CheckboxTheme();
        checkboxTheme.backgroundColor =
                new ParseHelper<String>().getObj(backgroundColor, checkboxTheme.backgroundColor);
        checkboxTheme.borderColor =
                new ParseHelper<String>().getObj(borderColor, checkboxTheme.borderColor);
        checkboxTheme.borderWidth = borderWidth > -1 ? borderWidth : checkboxTheme.borderWidth;
        checkboxTheme.width = width > -1 ? width : checkboxTheme.width;
        checkboxTheme.height = height > -1 ? height : checkboxTheme.height;
        return checkboxTheme;
    }
}
