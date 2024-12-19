package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;

import com.pulseinsights.surveysdk.define.base.BtnThemeBase;
import com.pulseinsights.surveysdk.util.MeasureTools;

public class SubmitBtnTheme extends BtnThemeBase {
    //Include the button on the invite widget
    public String fontFace = "sans-serif";
    public int fontSize = 16;
    public String fontColor = "#FFFFFF";
    public String disableFontColor = "#FFFFFF";
    public String horizonAlign = "left";
    public int borderRadius = 12;

    public SubmitBtnTheme() {
        backgroundColor = "#1274B8";
        borderColor = "#F1F1F1";
        borderWidth = 0;
        width = 0;
        height = 0;
        padding = 10;
        margin = 20;
    }

    @Override
    public void setupLayout(Context context, View view) {
        super.setupLayout(context, view);
        int pxPadding = MeasureTools.dip2px(context, padding);
        int pxPaddingHorizontal = paddingHorizontal > -1 ? MeasureTools.dip2px(context, paddingHorizontal) : pxPadding;
        int pxPaddingVertical = paddingVertical > -1 ? MeasureTools.dip2px(context, paddingVertical) : pxPadding;
        view.setPadding(pxPaddingHorizontal, pxPaddingVertical, pxPaddingHorizontal, pxPaddingVertical);
        view.setBackground(getButtonDrawable(true));
    }

    public void setupLayout(Context context, View view, boolean enable) {
        super.setupLayout(context, view, enable);
        int pxPadding = MeasureTools.dip2px(context, padding);
        int pxPaddingHorizontal = paddingHorizontal > -1 ? MeasureTools.dip2px(context, paddingHorizontal) : pxPadding;
        int pxPaddingVertical = paddingVertical > -1 ? MeasureTools.dip2px(context, paddingVertical) : pxPadding;
        view.setPadding(pxPaddingHorizontal, pxPaddingVertical, pxPaddingHorizontal, pxPaddingVertical);
        view.setBackground(getButtonDrawable(enable));
    }

    public void applyNewStyle(SubmitBtnTheme newStyle) {
        this.backgroundColor = newStyle.backgroundColor;
        this.borderColor = newStyle.borderColor;
        this.borderWidth = newStyle.borderWidth;
        this.fontFace = newStyle.fontFace;
        this.fontSize = newStyle.fontSize;
        this.fontColor = newStyle.fontColor;
        this.width = newStyle.width;
        this.height = newStyle.height;
        this.padding = newStyle.padding;
        this.margin = newStyle.margin;
        this.horizonAlign = newStyle.horizonAlign;
        this.borderRadius = newStyle.borderRadius;
        this.paddingHorizontal = newStyle.paddingHorizontal;
        this.paddingVertical = newStyle.paddingVertical;
    }

    private StateListDrawable getButtonDrawable(boolean enable) {
        StateListDrawable stateListDrawable = new StateListDrawable();

        // Pressed state
        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(Color.parseColor(pressedBackgroundColor));
        pressedDrawable.setCornerRadius(borderRadius);
        pressedDrawable.setStroke(borderWidth, Color.parseColor(borderColor));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);

        // Default state
        GradientDrawable defaultDrawable = new GradientDrawable();
        defaultDrawable.setColor(Color.parseColor(backgroundColor));
        defaultDrawable.setCornerRadius(borderRadius);
        defaultDrawable.setStroke(borderWidth, Color.parseColor(borderColor));
        stateListDrawable.addState(new int[]{}, defaultDrawable);

        return stateListDrawable;
    }

    public void configButtonText(TextView textView) {
        configButtonText(textView, true);
    }

    public void configButtonText(TextView textView, boolean enable) {
        textView.setTextColor(Color.parseColor(enable ? fontColor : disableFontColor));
        textView.setTextSize(fontSize);
        textView.setTypeface(Typeface.create(fontFace, Typeface.NORMAL));
        textView.setTextAlignment(getHorizonAlignment(horizonAlign));
    }

    public void resetStyle() {
        this.applyNewStyle(new SubmitBtnTheme());
    }

}