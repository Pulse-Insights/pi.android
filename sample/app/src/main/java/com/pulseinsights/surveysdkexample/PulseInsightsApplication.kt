package com.pulseinsights.surveysdkexample;

import android.app.Application
import android.content.SharedPreferences
import android.content.Context
import android.util.Log
import com.pulseinsights.surveysdk.ExtraConfig
import com.pulseinsights.surveysdk.PulseInsights

class PulseInsightsApplication : Application() {
    companion object {
        private var PULSE_INSIGHT_ACCOUNT_ID: String = ""
        lateinit var appSharedPreferences: SharedPreferences
        lateinit var appEditor: SharedPreferences.Editor
        private lateinit var instance: PulseInsightsApplication

        fun getInstance(): PulseInsightsApplication {
            return instance
        }
    }

    private lateinit var pulseInsights: PulseInsights

    override fun onCreate() {
        super.onCreate()
        instance = this
        appSharedPreferences = getSharedPreferences("DemoApp", Context.MODE_PRIVATE)
        appEditor = getSharedPreferences("DemoApp", Context.MODE_PRIVATE).edit()
        PULSE_INSIGHT_ACCOUNT_ID = appSharedPreferences.getString("ACCOUNT_ID", "").toString()
        getPulseInsights()
    }

    @Synchronized
    fun getPulseInsightsDefault(): PulseInsights {
        if (!::pulseInsights.isInitialized) {
            val piConfig = ExtraConfig()
            piConfig.automaticStart = false
            piConfig.customData = HashMap()
            piConfig.customData["name"] = "tester"
            piConfig.customData["type"] = "worker"
            piConfig.customData["age"] = "12"
            pulseInsights = PulseInsights(this, PULSE_INSIGHT_ACCOUNT_ID, piConfig)
        }
        pulseInsights.setDebugMode(true)
        Log.i("PulseInsights", "BuildConfig.FLAVOR: " + BuildConfig.FLAVOR)
        val urlPreFix = if (BuildConfig.FLAVOR.equals("stag", ignoreCase = true)) "staging-" else ""
        pulseInsights.setHost("https://$urlPreFix" + "survey.pulseinsights.com")
        return pulseInsights
    }

    fun getPulseInsights(): PulseInsights {
        return getInstance().getPulseInsightsDefault()
    }

    fun saveAccountId(newId: String) {
        PULSE_INSIGHT_ACCOUNT_ID = newId
        appEditor.putString("ACCOUNT_ID", newId).apply()
        pulseInsights.configAccountId(newId)
    }

    fun getCachedAccountId(): String? {
        return PULSE_INSIGHT_ACCOUNT_ID
    }
}