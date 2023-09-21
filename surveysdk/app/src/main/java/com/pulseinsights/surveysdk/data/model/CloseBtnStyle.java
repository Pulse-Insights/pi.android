package com.pulseinsights.surveysdk.data.model;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.CloseBtnTheme;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class CloseBtnStyle {

    @SerializedName("font-color")
    String fontColor = null;

    int margin = -1;
    boolean display = true;

    public CloseBtnTheme toTheme() {
        CloseBtnTheme closeBtnTheme = new CloseBtnTheme();
        closeBtnTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, closeBtnTheme.fontColor);
        closeBtnTheme.margin = margin > -1 ? margin : closeBtnTheme.margin;
        closeBtnTheme.display = display;
        return closeBtnTheme;
    }
}
