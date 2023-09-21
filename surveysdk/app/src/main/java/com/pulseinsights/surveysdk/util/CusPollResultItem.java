package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulseinsights.surveysdk.Define;
import com.pulseinsights.surveysdk.LocalData;
import com.pulseinsights.surveysdk.R;
import com.pulseinsights.surveysdk.data.model.AnswerOption;

/**
 * Created by leochao on 2018/3/9.
 */

public class CusPollResultItem extends LinearLayout {
    View layout;
    Context context;
    View resultBar;
    TextView itemTitle;
    TextView valueResult;

    public CusPollResultItem(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.layout_poll_result_item, this);
        resultBar = (View) layout.findViewById(R.id.result_bar);
        resultBar.setBackgroundColor(
                Color.parseColor(LocalData.instant.themeStyles.pollBar.barColor));
        itemTitle = (TextView) layout.findViewById(R.id.content);
        valueResult = (TextView) layout.findViewById(R.id.detail_value);
        LocalData.instant.themeStyles.mediumFont.configText(itemTitle);
        LocalData.instant.themeStyles.mediumFont.configText(valueResult);
    }

    private void displayPercentage(float value) {
        float displayValue = (value > 100) ? 100 : value;
        displayValue = (displayValue < 0) ? 0 : displayValue;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT, 100 - displayValue);
        resultBar.setLayoutParams(layoutParams);
    }

    public void placeResultContent(AnswerOption item, int totalCount) {
        itemTitle.setText(item.content);
        displayPercentage(item.getPercentage(totalCount));
        valueResult.setText(composeValueString(item.getPercentage(totalCount), item.count));
    }

    private String composeValueString(int percentage, int count) {
        boolean showAbs = LocalData.instant.themeStyles.pollBar.displayAbs;
        boolean showPercentage = LocalData.instant.themeStyles.pollBar.displayPercentage;
        String multi = (count > 1) ? "s" : "";
        String abs = showAbs ? String.format(
                showPercentage ? " (%d response%s)" : "%d response%s", count, multi) : "";
        return (showPercentage ? String.valueOf(percentage) + "%" : "") + abs;
    }
}
