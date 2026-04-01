package com.terraindash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            Log.e("TD", "Uncaught exception", throwable)
            showCrash(throwable)
        }

        try {
            Log.i("TD", "=== AndroidLauncher.onCreate() ===")
            Log.i("TD", "Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            Log.i("TD", "Android: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})")

            val config = AndroidApplicationConfiguration().apply {
                useAccelerometer = false
                useCompass = false
                useImmersiveMode = true
                numSamples = 0
                useGL30 = false
                r = 8; g = 8; b = 8; a = 8
                depth = 16; stencil = 0
            }

            Log.i("TD", "Calling initialize()...")
            initialize(TerrainDashGame(), config)
            Log.i("TD", "initialize() OK")
        } catch (t: Throwable) {
            Log.e("TD", "CRASH in onCreate", t)
            showCrash(t)
        }
    }

    private fun showCrash(t: Throwable) {
        try {
            val sb = StringBuilder()
            sb.appendLine("Device: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
            sb.appendLine("Android: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})")
            sb.appendLine()
            sb.appendLine(t.toString())
            sb.appendLine()
            for (el in t.stackTrace.take(30)) {
                sb.appendLine("  at $el")
            }
            var cause = t.cause
            while (cause != null) {
                sb.appendLine()
                sb.appendLine("Caused by: $cause")
                for (el in cause.stackTrace.take(15)) {
                    sb.appendLine("  at $el")
                }
                cause = cause.cause
            }

            val intent = Intent(this, CrashActivity::class.java)
            intent.putExtra("crash_log", sb.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("TD", "Could not show crash activity", e)
        }
    }
}
