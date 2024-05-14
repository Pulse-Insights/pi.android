package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;

public class WidgetTheme extends ViewThemeBase {
    // Common style for all of the views ( include the invite widget )
    public String backgroundColor = "#FFFFFF";
    public String borderColor = "#858585";
    public int borderWidth = 0;

    public WidgetTheme() {
        padding = 0;
    }

    public void configLayout(Context context, View view) {
        applyPadding(context, view);
        view.setBackground(getDrawable());
    }

    public void applyNewStyle(WidgetTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.padding = newStyle.padding;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
    }

    public GradientDrawable getDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(0);
        drawable.setColor(Color.parseColor(backgroundColor));
        drawable.setStroke(borderWidth,
                Color.parseColor(borderColor));
        return drawable;
    }

    public void resetStyle() {
        this.applyNewStyle(new WidgetTheme());
    }

}
