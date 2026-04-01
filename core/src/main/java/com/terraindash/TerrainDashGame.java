package com.terraindash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.terraindash.audio.SoundGenerator;
import com.terraindash.data.SaveManager;
import com.terraindash.ui.SkinFactory;

public class TerrainDashGame extends Game {

    private SpriteBatch batch;
    private SaveManager saveManager;
    private SoundGenerator soundGenerator;
    private boolean audioReady = false;
    private int frameCount = 0;

    @Override
    public void create() {
        Gdx.app.log("TD", "=== create() START ===");
        try {
            batch = new SpriteBatch();
            Gdx.app.log("TD", "SpriteBatch created");

            saveManager = new SaveManager();
            Gdx.app.log("TD", "SaveManager created");

            // Start with a simple loading screen that just clears to a color.
            // The real menu screen is set after the first frame renders.
            setScreen(new BootScreen(this));
            Gdx.app.log("TD", "=== create() DONE ===");
        } catch (Throwable t) {
            Gdx.app.error("TD", "FATAL in create(): " + t.getMessage(), t);
        }
    }

    void showMenu() {
        try {
            Gdx.app.log("TD", "showMenu()");
            setScreen(new com.terraindash.screens.MenuScreen(this));
        } catch (Throwable t) {
            Gdx.app.error("TD", "FATAL in showMenu(): " + t.getMessage(), t);
        }
    }

    public void initAudioIfNeeded() {
        if (audioReady) return;
        audioReady = true;
        try {
            soundGenerator = new SoundGenerator();
            soundGenerator.generateAll();
            Gdx.app.log("TD", "SFX generated OK");
        } catch (Throwable t) {
            Gdx.app.error("TD", "SFX generation failed (non-fatal): " + t.getMessage(), t);
            soundGenerator = null;
        }
    }

    public SpriteBatch getBatch() { return batch; }
    public SaveManager getSaveManager() { return saveManager; }
    public SoundGenerator getSoundGenerator() { return soundGenerator; }

    @Override
    public void dispose() {
        try {
            super.dispose();
            SkinFactory.dispose();
            if (batch != null) batch.dispose();
            if (soundGenerator != null) soundGenerator.dispose();
        } catch (Throwable t) {
            Gdx.app.error("TD", "Error in dispose: " + t.getMessage(), t);
        }
    }

    /**
     * Minimal boot screen: renders one green frame, then switches to real menu.
     * This ensures the GL context is fully ready before we do anything complex.
     */
    static class BootScreen extends ScreenAdapter {
        private final TerrainDashGame game;
        private int frames = 0;

        BootScreen(TerrainDashGame game) {
            this.game = game;
        }

        @Override
        public void render(float delta) {
            try {
                Gdx.gl.glClearColor(0.1f, 0.6f, 0.2f, 1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                frames++;
                if (frames == 3) {
                    Gdx.app.log("TD", "BootScreen: 3 frames rendered, switching to menu");
                    game.showMenu();
                }
            } catch (Throwable t) {
                Gdx.app.error("TD", "BootScreen render error: " + t.getMessage(), t);
            }
        }
    }
}
