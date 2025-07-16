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

    @SerializedName("font-weight")
    String fontWeight = null;

    String style = null;
    String alignment = null;

    public FontThemeBase toTheme(FontThemeBase defaultFontTheme) {
        FontThemeBase fontTheme = defaultFontTheme;
        fontTheme.fontSize = fontSize > -1 ? fontSize : fontTheme.fontSize;
        fontTheme.fontFace =
                new ParseHelper<String>().getObj(fontFace, fontTheme.fontFace);
        fontTheme.fontColor =
                new ParseHelper<String>().getObj(fontColor, fontTheme.fontColor);
        fontTheme.style =
                new ParseHelper<String>().getObj(style, fontTheme.style);
        fontTheme.alignment =
                new ParseHelper<String>().getObj(alignment, fontTheme.alignment);
        return fontTheme;
    }
}
