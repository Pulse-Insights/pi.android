package com.pulseinsights.surveysdkexample;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        setToolbarTitle("Check if Answered")
        val etSurveyId = findViewById<android.widget.EditText>(R.id.etSurveyId)
        val btnCheckAnswered = findViewById<android.widget.Button>(R.id.btnCheckAnswered)
        val ivErrorIcon = findViewById<android.widget.ImageView>(R.id.ivErrorIcon)
        val tvCheckResult = findViewById<android.widget.TextView>(R.id.tvCheckResult)
        btnCheckAnswered.setOnClickListener {
            val surveyId = etSurveyId.text.toString()
            if (surveyId.isNullOrBlank()) {
                Toast.makeText(this, "Please enter a valid survey id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val isAnswered = PulseInsightsApplication.getInstance().getPulseInsights().checkSurveyAnswered(surveyId)
            Log.i("PulseInsights", "isAnswered: $isAnswered")
            ivErrorIcon.visibility =
                if (isAnswered) android.view.View.VISIBLE else android.view.View.GONE
            tvCheckResult.visibility = android.view.View.VISIBLE
            tvCheckResult.text =
                if (isAnswered) "This survey has already been answered on this device, Please Reset Device UDID below." else "Survey has not been answered on this device. Please return to the Home page to trigger a survey."
        }

        val btnReset = findViewById<android.widget.Button>(R.id.btnReset)
        val tvResetResult = findViewById<android.widget.TextView>(R.id.tvResetResult)
        btnReset.setOnClickListener {
            PulseInsightsApplication.getInstance().getPulseInsights().resetUdid()
            tvResetResult.visibility = android.view.View.VISIBLE
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val homesMenuItem = menu?.findItem(R.id.action_back_home)
        homesMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity): Intent? {
            return Intent(activity, CheckActivity::class.java)
        }
    }
}