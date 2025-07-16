package com.pulseinsights.surveysdk.util;

import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by LeoChao on 2016/12/6.
 */

public class FormatSetTool {

    public static void setTextByHtml(TextView targetView, String strSettingText) {
        targetView.setText(Html.fromHtml(transferToHtmlFormat(strSettingText)));
    }

    static int TARGET_ID_BOLD = 0;
    static int TARGET_ID_ITALIC = 1;
    static String[] aryStringPreFix = {"<b>", "<em>"};
    static String[] aryStringPostFix = {"</b>", "</em>"};

    public static String transferToHtmlFormat(String strOriString) {
        if (strOriString == null || strOriString.isEmpty()) {
            return strOriString;
        }

        StringBuilder result = new StringBuilder();
        boolean inBold = false;
        int i = 0;
        
        while (i < strOriString.length()) {
            char c = strOriString.charAt(i);
            
            if (c == '*') {
                if (inBold) {
                    result.append("</b>");
                    inBold = false;
                } else {
                    result.append("<b>");
                    inBold = true;
                }
            } else {
                result.append(c);
            }
            
            i++;
        }
        
        if (inBold) {
            result.append("</b>");
        }
        
        String boldProcessed = result.toString();
        result = new StringBuilder();
        boolean inItalic = false;
        i = 0;
        
        while (i < boldProcessed.length()) {
            char c = boldProcessed.charAt(i);
            
            if (c == '_') {
                if (inItalic) {
                    result.append("</em>");
                    inItalic = false;
                } else {
                    result.append("<em>");
                    inItalic = true;
                }
            } else {
                result.append(c);
            }
            
            i++;
        }
        
        if (inItalic) {
            result.append("</em>");
        }
        
        return result.toString();
    }

    public static int countMatch(String strSource, String strTarget) {
        int rtTmp = 0;

        if (strTarget.length() > 0 && strSource.length() > strTarget.length()) {
            rtTmp = strSource.length() - strSource.replace(strTarget, "").length();
            rtTmp = rtTmp / strTarget.length();
        }

        return rtTmp;
    }
}
