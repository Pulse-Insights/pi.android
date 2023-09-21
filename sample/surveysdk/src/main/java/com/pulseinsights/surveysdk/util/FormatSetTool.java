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
    static int[] aryMarkCount = {0, 0};

    static List<Integer> listRecord = new ArrayList<Integer>();

    private static String getPostFixTag(int onCloseType) {
        String strRt = "";
        String strContinuePreFix = "";
        List<Integer> listAdditional = new ArrayList<Integer>();
        while (listRecord.size() > 0) {
            int lastIndex = listRecord.size() - 1;
            int lastTypeId = listRecord.get(lastIndex);
            listRecord.remove(lastIndex);
            strRt += aryStringPostFix[lastTypeId];
            if (onCloseType == lastTypeId) {
                break;
            } else {
                strContinuePreFix = aryStringPreFix[lastTypeId] + strContinuePreFix;
                listAdditional.add(lastTypeId);
            }
        }
        if (listAdditional.size() > 0) {
            Collections.reverse(listAdditional);
            listRecord.addAll(listAdditional);
        }

        return strRt;
    }

    public static String transferToHtmlFormat(String strOriString) {
        String strRtItem = "";
        listRecord = new ArrayList<Integer>();
        int tempSum = 0;

        int countMarkBold = countMatch(strOriString, "*");
        countMarkBold = (countMarkBold > 0) ? ((countMarkBold % 2 == 0)
                ? countMarkBold : (countMarkBold - 1)) : 0;
        tempSum += countMarkBold;
        int countMarkItalic = countMatch(strOriString, "_");
        countMarkItalic = (countMarkItalic > 0) ? ((countMarkItalic % 2 == 0)
                ? countMarkItalic : (countMarkItalic - 1)) : 0;
        tempSum += countMarkItalic;

        aryMarkCount[TARGET_ID_BOLD] = countMarkBold;
        aryMarkCount[TARGET_ID_ITALIC] = countMarkItalic;


        if (tempSum > 0) {
            char[] aryTemp = strOriString.toCharArray();

            for (char charTemp : aryTemp) {
                if ((charTemp == '*') || (charTemp == '_')) {
                    //TODO:Main analysis flow
                    String strAdded = "";
                    int targetId = (charTemp == '*') ? TARGET_ID_BOLD : TARGET_ID_ITALIC;
                    if (aryMarkCount[targetId] > 0) {
                        if (aryMarkCount[targetId] % 2 == 0) {
                            strAdded = aryStringPreFix[targetId];
                            listRecord.add(targetId);
                        } else {
                            strAdded = getPostFixTag(targetId);
                        }
                        aryMarkCount[targetId]--;
                    } else {
                        strAdded = String.valueOf(charTemp);
                    }

                    strRtItem = strRtItem + strAdded;
                } else {
                    strRtItem = strRtItem + String.valueOf(charTemp);
                }
            }
        } else {
            strRtItem = strOriString;
        }

        return strRtItem;
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
