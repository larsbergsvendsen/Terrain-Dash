package com.terraindash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.terraindash.audio.MusicGenerator;
import com.terraindash.audio.MusicManager;
import com.terraindash.audio.SFXManager;
import com.terraindash.audio.SoundGenerator;
import com.terraindash.data.SaveManager;
import com.terraindash.screens.MenuScreen;

public class TerrainDashGame extends Game {

    private SpriteBatch batch;
    private SaveManager saveManager;
    private MusicManager musicManager;
    private SFXManager sfxManager;
    private MusicGenerator musicGenerator;
    private SoundGenerator uiSoundGenerator;

    @Override
    public void create() {
        batch = new SpriteBatch();
        saveManager = new SaveManager();
        musicManager = new MusicManager();
        sfxManager = new SFXManager();

        // Generate procedural music tracks
        musicGenerator = new MusicGenerator();
        musicGenerator.generateAll();

        // Load generated tracks into music manager
        String[] trackIds = {"menu", "countryside", "desert", "arctic", "neon", "volcano", "sky"};
        for (String id : trackIds) {
            Music track = musicGenerator.loadTrack(id);
            if (track != null) {
                musicManager.loadGenerated(id, track);
            }
        }

        musicManager.setVolume(saveManager.getMusicVolume());

        // Generate UI sounds
        uiSoundGenerator = new SoundGenerator();
        uiSoundGenerator.generateAll();

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

    public SoundGenerator getUiSounds() {
        return uiSoundGenerator;
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (musicManager != null) musicManager.dispose();
        if (sfxManager != null) sfxManager.dispose();
        if (musicGenerator != null) musicGenerator.dispose();
        if (uiSoundGenerator != null) uiSoundGenerator.dispose();
        super.dispose();
    }
}
