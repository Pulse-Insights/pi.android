package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.CheckMarkTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class CheckMarkStyle {
    @SerializedName("color")
    String color = null;

    int width = -1;
    int height = -1;

    @SerializedName("font-size")
    int fontSize = -1;

    public CheckMarkTheme toTheme() {
        CheckMarkTheme checkMarkTheme = new CheckMarkTheme();
        checkMarkTheme.color =
                new ParseHelper<String>().getObj(color, checkMarkTheme.color);
        checkMarkTheme.width = width > -1 ? width : checkMarkTheme.width;
        checkMarkTheme.height = height > -1 ? height : checkMarkTheme.height;
        checkMarkTheme.fontSize = fontSize > -1 ? fontSize : checkMarkTheme.fontSize;
        return checkMarkTheme;
    }
}
