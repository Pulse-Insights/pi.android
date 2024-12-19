package com.pulseinsights.surveysdk.define.base;

import android.content.Context;
import android.view.View;

public abstract class BtnThemeBase extends ViewThemeBase {
    public String backgroundColor = "#FFFFFF";
    public String pressedBackgroundColor = "#35353b";
    public String disableBackgroundColor = "#BABABA";
    public String borderColor = "#000000";
    public String disableBorderColor = "#BABABA";
    public int borderWidth = 5;
    public int paddingHorizontal = -1;
    public int paddingVertical = -1;

    public void setupLayout(Context context, View view) {
        setupLayout(context, view, true);
    }

    public void setupLayout(Context context, View view, boolean enable) {
        configWidthHeight(context, view , width, height);
        applyMargin(context, view);
    }
}
