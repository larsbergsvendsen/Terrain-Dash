package com.terraindash

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class AndroidLauncher : Activity() {

    private lateinit var logView: TextView
    private val logLines = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { _, t ->
            Log.e("TD", "Uncaught", t)
            appendLog("UNCAUGHT: $t")
            t.stackTrace.take(10).forEach { appendLog("  $it") }
        }

        appendLog("=== Terrain Dash Debug ===")
        appendLog("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        appendLog("Android: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})")
        appendLog("")

        // Check if LibGDX native libs exist
        try {
            System.loadLibrary("gdx")
            appendLog("libgdx.so: OK")
        } catch (t: Throwable) {
            appendLog("libgdx.so FAIL: $t")
        }

        try {
            System.loadLibrary("gdx-box2d")
            appendLog("libgdx-box2d.so: OK")
        } catch (t: Throwable) {
            appendLog("libgdx-box2d.so FAIL: $t")
        }

        appendLog("")
        appendLog("Tap START to launch the game.")
        appendLog("If it crashes, the error will show here.")

        // Build UI
        logView = TextView(this).apply {
            text = logLines.joinToString("\n")
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            typeface = Typeface.MONOSPACE
            setPadding(24, 24, 24, 24)
            setTextIsSelectable(true)
        }

        val startBtn = Button(this).apply {
            text = "START GAME"
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            setBackgroundColor(Color.parseColor("#2255aa"))
            setTextColor(Color.WHITE)
            setPadding(40, 24, 40, 24)
            setOnClickListener { launchGame() }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0a0a1a"))
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 48, 0, 0)
        }

        layout.addView(startBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { bottomMargin = 32 })

        val scroll = ScrollView(this).apply { addView(logView) }
        layout.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))

        setContentView(layout)
    }

    private fun launchGame() {
        appendLog("")
        appendLog("Launching LibGDX...")

        try {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        } catch (t: Throwable) {
            appendLog("LAUNCH FAIL: $t")
            t.stackTrace.take(10).forEach { appendLog("  $it") }
        }
    }

    private fun appendLog(msg: String) {
        Log.i("TD", msg)
        logLines.add(msg)
        if (::logView.isInitialized) {
            runOnUiThread { logView.text = logLines.joinToString("\n") }
        }
    }
}
