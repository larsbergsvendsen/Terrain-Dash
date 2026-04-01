package com.terraindash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.terraindash.audio.MusicManager;
import com.terraindash.audio.SFXManager;
import com.terraindash.data.SaveManager;
import com.terraindash.screens.MenuScreen;

public class TerrainDashGame extends Game {

    private SpriteBatch batch;
    private SaveManager saveManager;
    private MusicManager musicManager;
    private SFXManager sfxManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        saveManager = new SaveManager();
        musicManager = new MusicManager();
        sfxManager = new SFXManager();

        setScreen(new MenuScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public SFXManager getSfxManager() {
        return sfxManager;
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (musicManager != null) musicManager.dispose();
        if (sfxManager != null) sfxManager.dispose();
        super.dispose();
    }
}
