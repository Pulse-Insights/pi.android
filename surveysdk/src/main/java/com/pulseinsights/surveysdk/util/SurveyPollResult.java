package com.pulseinsights.surveysdk.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pulseinsights.surveysdk.Define;
import com.pulseinsights.surveysdk.R;
import com.pulseinsights.surveysdk.data.model.AnswerOption;
import com.pulseinsights.surveysdk.data.model.PollResult;

/**
 * Created by leochao on 2018/3/9.
 */

public class SurveyPollResult extends RelativeLayout {
    View layout;
    Context context;
    LinearLayout resultItems;

    public SurveyPollResult(Context context) {
        super(context);
        init(context);
    }

    public SurveyPollResult(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.pulse_insight_poll_result, this);
        resultItems = (LinearLayout) layout.findViewById(R.id.result_items);
    }

    public void setResultItems(PollResult results) {
        resultItems.removeAllViews();
        resultItems.removeAllViewsInLayout();
        for (AnswerOption inputItem : results.countAnswers) {
            CusPollResultItem pollItem = new CusPollResultItem(context);
            resultItems.addView(pollItem);
            pollItem.placeResultContent(inputItem, results.getTotalCount());
        }
    }
}
