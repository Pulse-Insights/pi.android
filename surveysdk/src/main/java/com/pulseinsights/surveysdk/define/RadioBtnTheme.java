package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.pulseinsights.surveysdk.define.base.DrawableBtnThemeBase;

public class RadioBtnTheme extends DrawableBtnThemeBase {

    public boolean hide = false;

    public RadioBtnTheme() {
        backgroundColor = "#FFFFFF";
        borderColor = "#000000";
        borderWidth = 5;
        margin = -100;
        width = 20;
        height = 20;
        padding = 0;
        hide = false;
    }

    public void applyNewStyle(RadioBtnTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.padding = newStyle.padding;
        this.margin = newStyle.margin;
        this.hide = newStyle.hide;
    }

    public void setupDrawable(Context context, View view, boolean onState) {
        GradientDrawable boxShapeDrawable = getItemShape(context, GradientDrawable.OVAL,
                Color.parseColor(onState ? borderColor : backgroundColor), borderWidth,
                Color.parseColor(borderColor), width, height);
        view.setBackground(boxShapeDrawable);
        configWidthHeight(context, view, width, height);
    }

    public StateListDrawable getButtonDrawable(Context context) {
        StateListDrawable selector = new StateListDrawable();

        GradientDrawable focusedShape = getItemShape(context, GradientDrawable.OVAL,
                Color.parseColor(borderColor), borderWidth,
                Color.parseColor(borderColor), width, height);
        selector.addState(new int[]{android.R.attr.state_checked}, focusedShape);

        GradientDrawable defaultShape = getItemShape(context, GradientDrawable.OVAL,
                Color.parseColor(backgroundColor), borderWidth,
                Color.parseColor(borderColor), width, height);
        selector.addState(new int[]{}, defaultShape);

        return selector;
    }

    public void resetStyle() {
        this.applyNewStyle(new RadioBtnTheme());
    }

}