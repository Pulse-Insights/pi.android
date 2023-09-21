package com.pulseinsights.surveysdk.define.base;

import android.content.Context;
import android.view.View;

public abstract class DrawableBtnThemeBase extends BtnThemeBase {
    public abstract void setupDrawable(Context context, View view, boolean onState);
}
