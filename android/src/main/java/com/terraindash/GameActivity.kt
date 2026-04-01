package com.terraindash

import android.os.Bundle
import android.util.Log
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class GameActivity : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("TD", "GameActivity.onCreate()")

        val config = AndroidApplicationConfiguration().apply {
            useAccelerometer = false
            useCompass = false
            useImmersiveMode = true
            numSamples = 0
            useGL30 = false
        }

        Log.i("TD", "Calling initialize()")
        initialize(TerrainDashGame(), config)
        Log.i("TD", "initialize() returned")
    }
}
