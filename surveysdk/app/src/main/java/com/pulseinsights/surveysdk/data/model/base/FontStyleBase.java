package com.pulseinsights.surveysdk.data.model.base;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.base.FontThemeBase;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class FontStyleBase {
    @SerializedName("font-size")
    int fontSize = -1;

    @SerializedName("font-face")
    String fontFace = null;

    @SerializedName("font-color")
    String fontColor = null;

    String style = null;
    String alignment = null;

    public FontThemeBase toTheme(FontThemeBase defaultFontTheme) {
        FontThemeBase submitBtnTheme = defaultFontTheme;
        submitBtnTheme.fontSize = fontSize > -1 ? fontSize : submitBtnTheme.fontSize;
        submitBtnTheme.fontFace =
                new ParseHelper<String>().getObj(fontFace, submitBtnTheme.fontFace);
        submitBtnTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, submitBtnTheme.fontColor);
        submitBtnTheme.style =
                new ParseHelper<String>().getObj(style, submitBtnTheme.style);
        submitBtnTheme.alignment =
                new ParseHelper<String>().getObj(alignment, submitBtnTheme.alignment);
        return submitBtnTheme;
    }
}
