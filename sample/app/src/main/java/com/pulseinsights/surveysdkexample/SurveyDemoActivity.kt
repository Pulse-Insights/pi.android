package com.pulseinsights.surveysdkexample;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.pulseinsights.surveysdk.util.InlineSurveyView

class SurveyDemoActivity : BaseActivity() {

    private var from: String? = null
    private var inlineSurveyView: InlineSurveyView? = null
    private var tvViewName: TextView? = null
    private var ivHelpSingle: ImageView? = null
    private var groupBack: Group? = null
    private var groupNext: Group? = null
    private var ivNext: ImageView? = null
    private var ivBack: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_demo)
        setToolbarTitle("App Demo")
        inlineSurveyView = findViewById(R.id.inlineSurveyView)
        tvViewName = findViewById(R.id.tvViewName)
        ivHelpSingle = findViewById(R.id.ivHelpSingle)
        groupBack = findViewById(R.id.groupBack)
        groupNext = findViewById(R.id.groupNext)
        ivNext = findViewById(R.id.ivNext)
        ivNext?.setOnClickListener {
            newIntent(this, "sub_activity_b")?.let { startActivity(it) }
        }
        ivBack = findViewById(R.id.ivBack)
        ivBack?.setOnClickListener {
            newIntent(this, "sub_activity_a")?.let { startActivity(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        from = intent.getStringExtra("from")
        // form can be inline, sub_activity_a, sub_activity_b
        // need to switch between 'from' inline/sub_activity_a/sub_activity_b
        // and set the view name accordingly, call serve()
        PulseInsightsApplication.getInstance().getPulseInsights().context = this
        when (from) {
            "inline" -> {
                PulseInsightsApplication.getInstance().getPulseInsights()
                    .setViewName("inlineActivity")
                inlineSurveyView?.setIdentifier("InlineTestActivity")
                inlineSurveyView?.visibility = View.VISIBLE
                tvViewName?.text = "InlineTestActivity"
                ivHelpSingle?.visibility = View.VISIBLE
                PulseInsightsApplication.getInstance().getPulseInsights().serve()
            }

            "sub_activity_a" -> {
                PulseInsightsApplication.getInstance().getPulseInsights()
                    .setViewName("subActivityA")
                tvViewName?.text = "SubActivityA"
                groupNext?.visibility = View.VISIBLE
                ivHelpSingle?.visibility = View.GONE
                PulseInsightsApplication.getInstance().getPulseInsights().serve()
            }

            "sub_activity_b" -> {
                PulseInsightsApplication.getInstance().getPulseInsights()
                    .setViewName("subActivityB");
                tvViewName?.text = "SubActivityB"
                groupBack?.visibility = View.VISIBLE
                ivHelpSingle?.visibility = View.GONE
                PulseInsightsApplication.getInstance().getPulseInsights().serve()
            }

        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val homesMenuItem = menu?.findItem(R.id.action_back_home)
        homesMenuItem?.isVisible = true
        val helpMenuItem = menu?.findItem(R.id.action_help)
        helpMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity, from: String): Intent? {
            val intent = Intent(activity, SurveyDemoActivity::class.java)
            intent.putExtra("from", from)
            return intent
        }
    }
}