package com.pulseinsights.surveysdkexample

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.pulseinsights.surveysdk.PulseInsights

class ContextDataView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {
    private var llKeyValueContainer: LinearLayout? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_context_data, this, true)
        llKeyValueContainer = findViewById<LinearLayout>(R.id.llKeyValueContainer)
        val btnAddKeyValue = findViewById<Button>(R.id.btnAddKeyValue)
        val btnClearKeyValue = findViewById<Button>(R.id.btnClearKeyValue)
        val btnSaveContextData = findViewById<Button>(R.id.btnSaveContextData)
        val tvContextDataResult = findViewById<TextView>(R.id.tvContextDataResult)

        btnAddKeyValue.setOnClickListener { addKeyValuePair(context) }

        btnClearKeyValue.setOnClickListener { clearKeyValuePairs() }

        btnSaveContextData.setOnClickListener {
            saveContextData()
            tvContextDataResult.visibility = VISIBLE
            tvContextDataResult.text = "Context data has been set."
        }
    }

    private fun addKeyValuePair(context: Context) {
        val keyValueView: View = LayoutInflater.from(context)
            .inflate(R.layout.key_value_pair, llKeyValueContainer, false)
        llKeyValueContainer!!.addView(keyValueView)
    }

    private fun clearKeyValuePairs() {
        llKeyValueContainer!!.removeAllViews()
        PulseInsights.getInstant().clearContextData()
    }

    private fun saveContextData() {
        val data: MutableMap<String, String> = HashMap()
        for (i in 0 until llKeyValueContainer!!.childCount) {
            val keyValueView = llKeyValueContainer!!.getChildAt(i)
            val etKey = keyValueView.findViewById<EditText>(R.id.etKey)
            val etValue = keyValueView.findViewById<EditText>(R.id.etValue)
            val key = etKey.text.toString()
            val value = etValue.text.toString()
            if (!key.isEmpty() && !value.isEmpty()) {
                data[key] = value
            }
        }
        PulseInsights.getInstant().setContextData(data, true)
    }
}