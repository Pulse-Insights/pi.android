package com.pulseinsights.surveysdk;

public class Udid {

    public static String getUdid() {
        String rtUdid = "";

        String strFormat = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx";

        for (int val = 0; val < strFormat.length(); val++) {
            char strTmpChar = strFormat.toLowerCase().charAt(val);
            String outPart = "";
            if (strTmpChar == 'x' || strTmpChar == 'y') {
                int tmpBit = (int) (Math.random() * 16) | 0;
                int generateBit = (strTmpChar == 'x') ? tmpBit : (tmpBit & 0x3 | 0x8);
                outPart = Integer.toHexString(generateBit % 16);

            } else {
                outPart = String.valueOf(strTmpChar);
            }
            rtUdid = rtUdid + outPart;
        }


        return rtUdid;
    }
}
