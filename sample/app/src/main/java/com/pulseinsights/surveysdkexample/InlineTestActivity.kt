package com.pulseinsights.surveysdkexample;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InlineTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inline)
        setToolbarTitle("Trigger Inline Survey")

        val btnTriggerInlineSurvey = findViewById<Button>(R.id.btnTriggerInlineSurvey)
        btnTriggerInlineSurvey.setOnClickListener {
            SurveyDemoActivity.newIntent(this, "inline")?.let { intent ->
                startActivity(intent)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val helpMenuItem = menu?.findItem(R.id.action_help)
        helpMenuItem?.isVisible = true
        val homeMenuItem = menu?.findItem(R.id.action_back_home)
        homeMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity): Intent? {
            return Intent(activity, InlineTestActivity::class.java)
        }
    }
}