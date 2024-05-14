package com.pulseinsights.surveysdk.data.model.base;

import com.google.gson.annotations.SerializedName;
import com.pulseinsights.surveysdk.define.base.ImageThemeBase;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class ImageStyleBase {
    int width = -1;
    int height = -1;
    int margin = -1;

    @SerializedName("horizontal-align")
    String horizonAlign = null;

    public ImageThemeBase toTheme(ImageThemeBase defaultTheme) {
        ImageThemeBase imageThemeBase = defaultTheme;
        imageThemeBase.width = width > -1 ? width : imageThemeBase.width;
        imageThemeBase.height = height > -1 ? height : imageThemeBase.height;
        imageThemeBase.margin = margin > -1 ? margin : imageThemeBase.margin;
        imageThemeBase.horizonAlign =
                new ParseHelper<String>().getObj(horizonAlign, defaultTheme.horizonAlign);
        return imageThemeBase;
    }
}
