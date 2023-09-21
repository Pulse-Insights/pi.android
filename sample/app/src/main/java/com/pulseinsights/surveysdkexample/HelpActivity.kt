package com.pulseinsights.surveysdkexample;

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : BaseActivity() {

    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setToolbarTitle("Help")
        webView = findViewById(R.id.wvHelp)

        // Set a WebViewClient to handle URL loading
        webView.webViewClient = WebViewClient()

        val url = intent.getStringExtra("url")
        if (url != null) {
            webView.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val homesMenuItem = menu?.findItem(R.id.action_back_home)
        homesMenuItem?.isVisible = false
        val helpMenuItem = menu?.findItem(R.id.action_help)
        helpMenuItem?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        fun newIntent(activity: AppCompatActivity, url: String): Intent? {
            val intent = Intent(activity, HelpActivity::class.java)
            intent.putExtra("url", url)
            return intent
        }
    }
}