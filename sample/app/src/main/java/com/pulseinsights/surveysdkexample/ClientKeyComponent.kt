package com.pulseinsights.surveysdkexample;

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.pulseinsights.surveysdk.PulseInsights

class ClientKeyComponent(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_client_key, this, true)
        val etClientKey = findViewById<EditText>(R.id.etClientKey)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tvResult = findViewById<android.widget.TextView>(R.id.tvResult)
        btnSave.setOnClickListener {
            val clientKey = etClientKey.text.toString()
            saveClientKey(clientKey)
            tvResult.visibility = android.view.View.VISIBLE
            tvResult.text =
                "Client Key has been set. Please choose one of the survey triggers above."
        }
        val btnClear = findViewById<Button>(R.id.btnClear)
        btnClear.setOnClickListener {
            removeClientKey()
            tvResult.visibility = android.view.View.VISIBLE
            tvResult.text = "Client Key has been cleared."
        }
    }

    private fun saveClientKey(clientKey: String) {
        PulseInsights.getInstant().clientKey = clientKey
    }

    private fun removeClientKey() {
        PulseInsights.getInstant().clientKey = ""
    }
}