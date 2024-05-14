package com.pulseinsights.surveysdk.define;

public class BrandTheme {
    public boolean display = true;

    public void applyNewStyle(BrandTheme newStyle) {
        this.display = newStyle.display;
    }

    public void resetStyle() {
        this.applyNewStyle(new BrandTheme());
    }

}