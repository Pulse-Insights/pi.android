package com.pulseinsights.surveysdk.define.base;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class ThemeBase {
    public int getHorizonAlignment(String strGravity) {
        if (strGravity.equalsIgnoreCase("left")) {
            return View.TEXT_ALIGNMENT_TEXT_START;
        } else if (strGravity.equalsIgnoreCase("right")) {
            return View.TEXT_ALIGNMENT_TEXT_END;
        } else {
            return View.TEXT_ALIGNMENT_CENTER;
        }
    }

    public int getHorizonGravity(String strGravity) {
        if (strGravity.equalsIgnoreCase("left")) {
            return Gravity.LEFT;
        } else if (strGravity.equalsIgnoreCase("right")) {
            return Gravity.RIGHT;
        } else {
            return Gravity.CENTER;
        }
    }

    public int getTypeStyle(String strType) {
        if (strType.equalsIgnoreCase("italic")) {
            return Typeface.ITALIC;
        } else if (strType.equalsIgnoreCase("oblique")) {
            return Typeface.ITALIC;
        } else if (strType.equalsIgnoreCase("bold")) {
            return Typeface.BOLD;
        } else if (strType.equalsIgnoreCase("bold_italic")) {
            return Typeface.BOLD_ITALIC;
        } else {
            return Typeface.NORMAL;
        }
    }

    public void configContainerMargin(View container, int marginLeft,
                                      int marginTop, int marginRight, int marginButton) {
        ViewGroup.MarginLayoutParams params = (
                ViewGroup.MarginLayoutParams) container.getLayoutParams();
        if (params == null) {
            RelativeLayout.LayoutParams createParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            createParams.setMargins(marginLeft, marginTop, marginRight, marginButton);
            container.setLayoutParams(createParams);
        } else {
            params.setMargins(marginLeft, marginTop, marginRight, marginButton);
            container.setLayoutParams(params);
        }
    }
}
