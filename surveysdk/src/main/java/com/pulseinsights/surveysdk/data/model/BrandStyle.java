package com.pulseinsights.surveysdk.data.model;

import com.pulseinsights.surveysdk.define.BrandTheme;

public class BrandStyle {
    boolean display = true;

    public BrandTheme toTheme() {
        BrandTheme brandTheme = new BrandTheme();
        brandTheme.display = display;
        return brandTheme;
    }
}
