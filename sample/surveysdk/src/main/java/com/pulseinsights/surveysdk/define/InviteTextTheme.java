package com.pulseinsights.surveysdk.define;

import com.pulseinsights.surveysdk.define.base.ViewThemeBase;

public class InviteTextTheme extends ViewThemeBase {

    public void applyNewStyle(InviteTextTheme newStyle) {
        this.margin = newStyle.margin;
    }

    public void resetStyle() {
        this.applyNewStyle(new InviteTextTheme());
    }

}