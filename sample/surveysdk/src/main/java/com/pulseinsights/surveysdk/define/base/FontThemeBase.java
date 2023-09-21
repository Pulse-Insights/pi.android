package com.pulseinsights.surveysdk.define.base;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public abstract class FontThemeBase extends ThemeBase {
    public String fontFace = "sans-serif";
    public int fontSize = 12;
    public String fontColor = "#000000";
    public String alignment = "left";
    public String style = "normal";

    public FontThemeBase() {

    }

    public FontThemeBase(int fontSize) {
        this.fontSize = fontSize;
    }

    public void applyNewStyle(FontThemeBase newStyle) {
        this.fontFace = newStyle.fontFace;
        this.fontSize = newStyle.fontSize;
        this.fontColor = newStyle.fontColor;
        this.alignment = newStyle.alignment;
        this.style = newStyle.style;
    }

    public void configText(TextView textView) {
        textView.setTypeface(Typeface.create(fontFace, getTypeStyle(style)));
        textView.setTextSize(fontSize);
        textView.setTextColor(Color.parseColor(fontColor));
        textView.setTextAlignment(getHorizonAlignment(alignment));
    }

}
