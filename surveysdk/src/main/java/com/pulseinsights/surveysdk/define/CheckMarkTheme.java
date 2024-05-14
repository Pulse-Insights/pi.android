package com.pulseinsights.surveysdk.define;

import android.graphics.Color;
import android.widget.TextView;

public class CheckMarkTheme {
    public String color = "#FFFFFF";
    public int width = 0;
    public int fontSize = 16;
    public int height = 0;

    public void applyNewStyle(CheckMarkTheme newStyle) {
        this.color = newStyle.color;
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.fontSize = newStyle.fontSize;
    }

    public void decorateCheckMark(TextView textView) {
        textView.setTextColor(Color.parseColor(color));
        textView.setTextSize(fontSize);
    }

    public void resetStyle() {
        this.applyNewStyle(new CheckMarkTheme());
    }

}