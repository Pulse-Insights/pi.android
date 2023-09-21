package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.pulseinsights.surveysdk.define.base.DrawableBtnThemeBase;

public class CheckboxTheme extends DrawableBtnThemeBase {

    public CheckboxTheme() {
        backgroundColor = "#8BCC6B";
        borderColor = "#000000";
        borderWidth = 5;
        width = 25;
        height = 25;
        margin = 2;
    }

    public void applyNewStyle(CheckboxTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.margin = newStyle.margin;
    }

    public void setupDrawable(Context context, View view, boolean onState) {
        GradientDrawable boxShapeDrawable = getItemShape(context, GradientDrawable.OVAL,
                Color.parseColor(backgroundColor), borderWidth,
                Color.parseColor(borderColor), width, height);
        view.setBackground(boxShapeDrawable);
        configWidthHeight(context, view, width, height);
    }

    public void resetStyle() {
        this.applyNewStyle(new CheckboxTheme());
    }

}