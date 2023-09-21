package com.pulseinsights.surveysdkexample;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import com.pulseinsights.surveysdkexample.BaseActivity

class OtherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)
        setToolbarTitle("Trigger a survey")

        val btnTriggerSurvey = findViewById<Button>(R.id.btnTriggerSurvey)
        btnTriggerSurvey.setOnClickListener {
            PulseInsightsApplication.getInstance().getPulseInsights().serve()
        }

        val btnTriggerSubActivityA = findViewById<Button>(R.id.btnTriggerSurvey02)
        btnTriggerSubActivityA.setOnClickListener {
            SurveyDemoActivity.newIntent(this, "sub_activity_a")?.let { startActivity(it) }
        }

        val btnTriggerSubActivityB = findViewById<Button>(R.id.btnTriggerSurvey03)
        btnTriggerSubActivityB.setOnClickListener {
            SurveyDemoActivity.newIntent(this, "sub_activity_b")?.let { startActivity(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        PulseInsightsApplication.getInstance().getPulseInsights().context = this;
        PulseInsightsApplication.getInstance().getPulseInsights().setViewName("mainActivity")
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val homesMenuItem = menu?.findItem(R.id.action_back_home)
        homesMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity): Intent? {
            return Intent(activity, OtherActivity::class.java)
        }
    }
}