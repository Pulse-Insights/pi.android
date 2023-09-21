package com.pulseinsights.surveysdkexample;

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        // update ststus bar color to R.color.black_bg
        val window = this.window
        window.statusBarColor = resources.getColor(R.color.black_bg)

        val cachedID = PulseInsightsApplication.getInstance().getCachedAccountId()
        Log.i("PulseInsights", "cachedID: $cachedID")
        // check if any account id is saved in shared preferences
        // if yes, then input it into the account id field
        val etAccountId = findViewById<EditText>(R.id.etAccountId)
        etAccountId.setText(cachedID)
        val saveBtn = findViewById<Button>(R.id.btnStart)
        saveBtn.setOnClickListener {
            // set up account id into pulse insight sdk
            // account id should be like PI-XXXXXXXX
            val accountId = etAccountId.text.toString()
            if (accountId.isNullOrBlank() || accountId.length < 11) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter a valid account id")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                return@setOnClickListener
            }
            PulseInsightsApplication.getInstance().saveAccountId(accountId)
            // go to main activity
            startActivity(MainActivity.newIntent(this))
        }
    }
}