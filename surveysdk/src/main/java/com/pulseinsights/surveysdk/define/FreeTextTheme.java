package com.pulseinsights.surveysdk.define;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class FreeTextTheme {
    public String borderColor = "#808080";
    public int borderWidth = 3;
    public String backgroundColor = "#FFFFFF";
    public String fontColor = "#000000";
    public int maxLines = 0;
    public String placeholderFontColor = "#989898";

    public void applyNewStyle(FreeTextTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.fontColor = newStyle.fontColor;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
        this.maxLines = newStyle.maxLines;
        this.placeholderFontColor = newStyle.placeholderFontColor;
    }

    public GradientDrawable getBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(borderWidth, Color.parseColor(borderColor));
        drawable.setCornerRadius(10);
        drawable.setColor(Color.parseColor(backgroundColor));
        return drawable;
    }

    public void resetStyle() {
        this.applyNewStyle(new FreeTextTheme());
    }

}