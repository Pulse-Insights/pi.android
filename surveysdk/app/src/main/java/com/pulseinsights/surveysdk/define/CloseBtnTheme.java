package com.pulseinsights.surveysdk.define;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;

public class CloseBtnTheme extends ViewThemeBase {
    public String fontColor = "#000000";
    public boolean display = true;

    public CloseBtnTheme() {
        margin = 20;
    }

    public void applyNewStyle(CloseBtnTheme newStyle) {
        this.fontColor = newStyle.fontColor;
        this.margin = newStyle.margin;
        this.display = newStyle.display;
    }

    public void resetStyle() {
        this.applyNewStyle(new CloseBtnTheme());
    }

    public void setBtnContainer(Context context, RelativeLayout container) {
        applyMargin(context, container);
        container.setVisibility(display ? View.VISIBLE : View.GONE);
    }

}
