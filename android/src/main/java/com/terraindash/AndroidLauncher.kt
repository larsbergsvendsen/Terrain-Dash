package com.terraindash

import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TerrainDash", "AndroidLauncher.onCreate()")

        try {
            val config = AndroidApplicationConfiguration().apply {
                useAccelerometer = false
                useCompass = false
                useImmersiveMode = true
                numSamples = 0
                useGL30 = false
                r = 8
                g = 8
                b = 8
                a = 8
                depth = 16
                stencil = 0
            }

            Log.i("TerrainDash", "Calling initialize()")
            initialize(TerrainDashGame(), config)
            Log.i("TerrainDash", "initialize() returned")
        } catch (e: Exception) {
            Log.e("TerrainDash", "CRASH in onCreate: ${e.message}", e)
        }
    }
}
