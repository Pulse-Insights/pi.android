package com.pulseinsights.surveysdk.data.model;

import com.pulseinsights.surveysdk.define.InviteTextTheme;

public class InviteTextStyle {
    int margin = -1;

    public InviteTextTheme toTheme() {
        InviteTextTheme inviteTextTheme = new InviteTextTheme();
        inviteTextTheme.margin = margin > -1 ? margin : inviteTextTheme.margin;
        return inviteTextTheme;
    }
}
