package com.pulseinsights.surveysdkexample;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pulseinsights.surveysdkexample.BaseActivity
import com.pulseinsights.surveysdkexample.CheckActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val llTriggerSurvey = findViewById<android.widget.LinearLayout>(R.id.llTriggerSurvey)
        llTriggerSurvey.setOnClickListener {
            // show alert dialog to distinguish between inline and other
            // if inline, then go to inline activity
            // else go to other activity
            AlertDialog.Builder(this)
                .setTitle("Trigger Survey")
                .setMessage("Is the survey an inline widget?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    startActivity(InlineTestActivity.newIntent(this))
                }
                .setNegativeButton("No") { _, _ ->
                    startActivity(OtherActivity.newIntent(this))
                }
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        val llCheck = findViewById<android.widget.LinearLayout>(R.id.llCheck)
        llCheck.setOnClickListener {
            // go to check activity
            startActivity(CheckActivity.newIntent(this))
        }

        val llHelp = findViewById<android.widget.LinearLayout>(R.id.llHelp)
        llHelp.setOnClickListener {
            // open help url
            goToHelpPage(it)
        }
    }

    override fun onResume() {
        super.onResume()
        PulseInsightsApplication.getInstance().getPulseInsights().context = this;
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val homesMenuItem = menu?.findItem(R.id.action_back_home)
        homesMenuItem?.isVisible = false
        val helpMenuItem = menu?.findItem(R.id.action_help)
        helpMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity): Intent? {
            // we shouldn't create new intent for the same activity
            // we should go to the same activity
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            return intent
        }
    }
}