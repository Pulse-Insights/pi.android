package com.pulseinsights.surveysdk.define;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.pulseinsights.surveysdk.define.base.BtnThemeBase;


public class AnswerBtnTheme extends BtnThemeBase {
    public String fontColor = "#000000";
    public boolean tabEffect = false;
    public String tabEffectTextColor = "#000000";
    public String tabEffectBackgroundColor = "#0d0d0f";
    public String tabEffectBorderColor = "#000000";
    public String perRowBackgroundColor = "#BABABA";

    public String selectedBackgroundColor = "#0d0d0f";
    public AnswerBtnTheme() {
        backgroundColor = "#FFFFFF";
        selectedBackgroundColor = "#0d0d0f";
        borderColor = "#858585";
        borderWidth = 3;
        width = 0;
        height = 0;
        margin = 0;
        padding = 20;
        tabEffect = false;
    }

    public void applyNewStyle(AnswerBtnTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.selectedBackgroundColor = newStyle.selectedBackgroundColor;
        this.fontColor = newStyle.fontColor;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.padding = newStyle.padding;
        this.margin = newStyle.margin;
        this.tabEffect = newStyle.tabEffect;
        this.tabEffectTextColor = newStyle.tabEffectTextColor;
        this.tabEffectBackgroundColor = newStyle.tabEffectBackgroundColor;
        this.tabEffectBorderColor = newStyle.tabEffectBorderColor;
        this.perRowBackgroundColor = newStyle.perRowBackgroundColor;
        this.paddingHorizontal = newStyle.paddingHorizontal;
        this.paddingVertical = newStyle.paddingVertical;
    }

    public GradientDrawable getDrawable() {

        return getDrawable(backgroundColor);
    }

    public GradientDrawable getDrawable(String background) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(borderWidth,
                Color.parseColor(borderColor));
        drawable.setCornerRadius(0);
        drawable.setColor(Color.parseColor(background));
        return drawable;
    }

    public GradientDrawable getPressInDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(borderWidth,
                Color.parseColor(tabEffectBorderColor));
        drawable.setCornerRadius(0);
        drawable.setColor(Color.parseColor(tabEffectBackgroundColor));
        return drawable;
    }

    public void configTextPressInColor(TextView textView) {
        textView.setTextColor(Color.parseColor(tabEffectTextColor));
    }

    public void resetStyle() {
        this.applyNewStyle(new AnswerBtnTheme());
    }

}