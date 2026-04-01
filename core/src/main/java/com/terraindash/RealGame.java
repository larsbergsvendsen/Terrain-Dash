package com.terraindash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.terraindash.audio.SoundGenerator;
import com.terraindash.data.SaveManager;
import com.terraindash.screens.MenuScreen;
import com.terraindash.ui.SkinFactory;

/**
 * The real game logic, started after the debug boot screen verifies
 * that all subsystems work. Implements a manual Screen manager
 * since we extend ApplicationAdapter at the top level.
 */
public class RealGame {

    private SpriteBatch batch;
    private SaveManager saveManager;
    private SoundGenerator soundGenerator;
    private Screen currentScreen;

    public void create() {
        Gdx.app.log("TD", "RealGame.create()");
        batch = new SpriteBatch();
        saveManager = new SaveManager();

        try {
            soundGenerator = new SoundGenerator();
            soundGenerator.generateAll();
        } catch (Throwable t) {
            Gdx.app.error("TD", "SFX failed (non-fatal)", t);
            soundGenerator = null;
        }

        setScreen(new MenuScreen(this));
    }

    public void render(float delta) {
        if (currentScreen != null) {
            currentScreen.render(delta);
        }
    }

    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        currentScreen = screen;
        if (currentScreen != null) {
            currentScreen.show();
            currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public SpriteBatch getBatch() { return batch; }
    public SaveManager getSaveManager() { return saveManager; }
    public SoundGenerator getSoundGenerator() { return soundGenerator; }

    public void dispose() {
        if (currentScreen != null) currentScreen.dispose();
        if (batch != null) batch.dispose();
        if (soundGenerator != null) soundGenerator.dispose();
    }
}
