package com.terraindash

import android.app.Activity
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue

class CrashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val log = intent.getStringExtra("crash_log") ?: "No crash info available"

        val tv = TextView(this).apply {
            text = "TERRAIN DASH CRASHED\n\n$log"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#1a1a2e"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
            typeface = Typeface.MONOSPACE
            setPadding(32, 48, 32, 32)
            setTextIsSelectable(true)
        }

        val sv = ScrollView(this).apply { addView(tv) }
        setContentView(sv)
    }
}
