package com.pulseinsights.surveysdk.define;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;

public class QuestionErrorTextTheme  extends ViewThemeBase {

    public String fontColor = "#000000";

    public QuestionErrorTextTheme() {
        margin = 0;
        topMargin = 10;
        bottomMargin = 10;
    }

    public void applyNewStyle(QuestionErrorTextTheme newStyle) {
        this.margin = newStyle.margin;
        this.fontColor = newStyle.fontColor;
        this.topMargin = newStyle.topMargin;
        this.bottomMargin = newStyle.bottomMargin;
    }

    public void resetStyle() {
        this.applyNewStyle(new QuestionErrorTextTheme());
    }

    public void configText(TextView textView) {
        textView.setTextColor(Color.parseColor(fontColor));
    }
}
