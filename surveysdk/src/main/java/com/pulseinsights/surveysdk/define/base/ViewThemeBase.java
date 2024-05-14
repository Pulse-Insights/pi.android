package com.pulseinsights.surveysdk.define.base;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.pulseinsights.surveysdk.util.MeasureTools;

public abstract class ViewThemeBase extends ThemeBase {
    public int margin = 0;
    public int topMargin = -1;
    public int bottomMargin = -1;
    public int padding = 0;
    public int width = 0;
    public int height = 0;
    // all of the measure value will be in dp except the width/height for image will be in px

    public void applyPadding(Context context, View container) {
        int pxPadding = MeasureTools.dip2px(context, padding);
        container.setPadding(pxPadding, pxPadding, pxPadding, pxPadding);
    }

    public void applyMargin(Context context, View container) {
        int pxMargin = MeasureTools.dip2px(context, margin);
        int pxTopMargin = MeasureTools.dip2px(context, (topMargin == -1) ? margin : topMargin);
        int pxBottomMargin =
                MeasureTools.dip2px(context, (bottomMargin == -1) ? margin : bottomMargin);

        configContainerMargin(container,
                pxMargin, pxTopMargin,
                pxMargin, pxBottomMargin);
    }

    public void configWidthHeight(Context context, View view, int width, int height) {
        if (view == null) return;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (height > 0 && params != null) {
            params.height = MeasureTools.dip2px(context, height);
        }
        if (width > 0 && params != null) {
            params.width = MeasureTools.dip2px(context, width);
        }
    }

    public GradientDrawable getItemShape(
            Context context, int shape, int solidColor, int strokeWidth,
            int strokeColor, int shapeWidth, int shapeHeight) {
        int widthPx = MeasureTools.dip2px(context, shapeWidth);
        int heightPx = MeasureTools.dip2px(context, shapeHeight);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(solidColor);
        drawable.setSize(widthPx, heightPx);
        return drawable;
    }
}
