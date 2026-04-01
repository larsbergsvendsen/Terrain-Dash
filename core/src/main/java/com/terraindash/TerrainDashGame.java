package com.terraindash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.terraindash.audio.SoundGenerator;
import com.terraindash.data.SaveManager;
import com.terraindash.screens.MenuScreen;
import com.terraindash.ui.SkinFactory;

public class TerrainDashGame extends Game {

    private SpriteBatch batch;
    private SaveManager saveManager;
    private SoundGenerator soundGenerator;
    private boolean audioReady = false;

    @Override
    public void create() {
        Gdx.app.log("TerrainDash", "create() start");

        try {
            batch = new SpriteBatch();
            saveManager = new SaveManager();
            setScreen(new MenuScreen(this));
            Gdx.app.log("TerrainDash", "create() complete");
        } catch (Exception e) {
            Gdx.app.error("TerrainDash", "CRASH in create(): " + e.getMessage(), e);
        }
    }

    public void initAudioIfNeeded() {
        if (audioReady) return;
        audioReady = true;

        try {
            soundGenerator = new SoundGenerator();
            soundGenerator.generateAll();
            Gdx.app.log("TerrainDash", "Sound generation complete");
        } catch (Exception e) {
            Gdx.app.error("TerrainDash", "Audio generation failed (non-fatal): " + e.getMessage(), e);
            soundGenerator = null;
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public SoundGenerator getSoundGenerator() {
        return soundGenerator;
    }

    @Override
    public void dispose() {
        super.dispose();
        SkinFactory.dispose();
        if (batch != null) batch.dispose();
        if (soundGenerator != null) soundGenerator.dispose();
    }
}
