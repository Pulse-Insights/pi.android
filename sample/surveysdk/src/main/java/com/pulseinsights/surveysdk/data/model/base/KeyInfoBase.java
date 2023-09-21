package com.pulseinsights.surveysdk.data.model.base;

import com.pulseinsights.surveysdk.data.model.EndDeviceInfo;
import com.pulseinsights.surveysdk.jsontool.ParseHelper;

public class KeyInfoBase {
    String udid = "";

    public EndDeviceInfo toKeyInfo() {
        EndDeviceInfo endDeviceInfo = new EndDeviceInfo();
        endDeviceInfo.udid = new ParseHelper<String>().getObj(udid, "");
        return endDeviceInfo;
    }

}
