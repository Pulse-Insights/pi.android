package com.pulseinsights.surveysdkexample;

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)  // Use the base layout

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//         Set the title of the toolbar
        setToolbarTitle("")
        // update status bar color
        val window: Window = this.window
        window.statusBarColor = resources.getColor(R.color.black_bg)
    }

    override fun setContentView(layoutResID: Int) {
        val activityContent: FrameLayout = findViewById(R.id.activity_content)
        layoutInflater.inflate(layoutResID, activityContent, true)
    }

    protected fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun goToHelpPage(view: View) {
        startActivity(
            HelpActivity.newIntent(
                this,
                "https://docs.pulseinsights.com/configuring-pulse-insights-console/editor/demo-app-help"
            )
        )
    }

    fun goToHelpPage(item: MenuItem) {
        startActivity(
            HelpActivity.newIntent(
                this,
                "https://docs.pulseinsights.com/configuring-pulse-insights-console/editor/demo-app-help"
            )
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_back_home -> {
                MainActivity.newIntent(this)?.let {
                    startActivity(it)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
