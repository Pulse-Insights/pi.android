package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.view.View;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;
import com.pulseinsights.surveysdk.util.MeasureTools;

public class HelperTheme extends ViewThemeBase {
    public int paddingHorizontal;
    public int paddingVertical;
    public String gravity;
    public String fontColor;

    public HelperTheme() {
        margin = 0;
        topMargin = -1;
        bottomMargin = -1;
        padding = 0;
        paddingHorizontal = 0;
        paddingVertical = 0;
        gravity = "";
        fontColor = "";
    }

    public void applyNewStyle(HelperTheme newStyle) {
        this.margin = newStyle.margin;
        this.topMargin = newStyle.topMargin;
        this.bottomMargin = newStyle.bottomMargin;
        this.padding = newStyle.padding;
        this.paddingVertical = newStyle.paddingVertical;
        this.paddingHorizontal = newStyle.paddingHorizontal;
        this.gravity = newStyle.gravity;
        this.fontColor = newStyle.fontColor;
    }

    public void applyPadding(Context context, View container) {
        int pxPadding = MeasureTools.dip2px(context, padding);
        int pxPaddingHorizontal = MeasureTools.dip2px(context, paddingHorizontal);
        int pxPaddingVertical = MeasureTools.dip2px(context, paddingVertical);
        int pl = pxPaddingHorizontal != pxPadding ? pxPaddingHorizontal : padding;
        int pt = pxPaddingVertical != pxPadding ? pxPaddingVertical : padding;
        container.setPadding(pl, pt, pl, pt);
    }

    public void resetStyle() {
        this.applyNewStyle(new HelperTheme());
    }
}
