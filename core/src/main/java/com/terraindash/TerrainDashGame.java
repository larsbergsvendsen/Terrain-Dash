package com.terraindash;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * Ultra-safe game entry point that logs every step to screen.
 * Once we know it works, we restore the real game.
 */
public class TerrainDashGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    private Viewport viewport;
    private final List<String> log = new ArrayList<>();
    private int phase = 0;
    private int frameCount = 0;
    private RealGame realGame;
    private boolean realGameRunning = false;

    @Override
    public void create() {
        try {
            log("create() start");
            batch = new SpriteBatch();
            log("SpriteBatch OK");

            font = new BitmapFont();
            font.setColor(Color.WHITE);
            font.getData().setScale(1.5f);
            log("BitmapFont OK");

            viewport = new ScreenViewport();
            log("Viewport OK");

            log("create() done - will show debug log");
            phase = 1;
        } catch (Throwable t) {
            log("CRASH in create(): " + t);
            logTrace(t);
        }
    }

    @Override
    public void render() {
        frameCount++;
        try {
            Gdx.gl.glClearColor(0.05f, 0.05f, 0.15f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (realGameRunning && realGame != null) {
                realGame.render(Gdx.graphics.getDeltaTime());
                return;
            }

            if (batch == null || font == null) return;

            // Phase 1: Show log for a few frames, then try to start real game
            if (phase == 1 && frameCount == 5) {
                log("Frame 5 - testing SkinFactory...");
                tryCreateSkin();
            }
            if (phase == 1 && frameCount == 10) {
                log("Frame 10 - testing AssetGenerator...");
                tryCreateAssets();
            }
            if (phase == 1 && frameCount == 15) {
                log("Frame 15 - testing SaveManager...");
                tryCreateSaveManager();
            }
            if (phase == 1 && frameCount == 20) {
                log("Frame 20 - testing SoundGenerator...");
                tryCreateSounds();
            }
            if (phase == 1 && frameCount == 30) {
                log("Frame 30 - launching real game...");
                tryLaunchRealGame();
            }

            // Draw debug log
            viewport.apply(true);
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
            float y = viewport.getWorldHeight() - 20;
            int startLine = Math.max(0, log.size() - 35);
            for (int i = startLine; i < log.size(); i++) {
                font.draw(batch, log.get(i), 15, y);
                y -= 20;
            }
            font.draw(batch, "Frame: " + frameCount, 15, 20);
            batch.end();

        } catch (Throwable t) {
            log("CRASH in render(): " + t);
            logTrace(t);
        }
    }

    private void tryCreateSkin() {
        try {
            com.terraindash.ui.SkinFactory.create();
            log("  SkinFactory OK");
        } catch (Throwable t) {
            log("  SkinFactory FAIL: " + t);
            logTrace(t);
        }
    }

    private void tryCreateAssets() {
        try {
            com.terraindash.utils.AssetGenerator ag = new com.terraindash.utils.AssetGenerator();
            ag.generateAll();
            ag.dispose();
            log("  AssetGenerator OK");
        } catch (Throwable t) {
            log("  AssetGenerator FAIL: " + t);
            logTrace(t);
        }
    }

    private void tryCreateSaveManager() {
        try {
            new com.terraindash.data.SaveManager();
            log("  SaveManager OK");
        } catch (Throwable t) {
            log("  SaveManager FAIL: " + t);
            logTrace(t);
        }
    }

    private void tryCreateSounds() {
        try {
            com.terraindash.audio.SoundGenerator sg = new com.terraindash.audio.SoundGenerator();
            sg.generateAll();
            sg.dispose();
            log("  SoundGenerator OK");
        } catch (Throwable t) {
            log("  SoundGenerator FAIL: " + t);
            logTrace(t);
        }
    }

    private void tryLaunchRealGame() {
        try {
            realGame = new RealGame();
            realGame.create();
            realGameRunning = true;
            log("  RealGame launched OK!");
        } catch (Throwable t) {
            log("  RealGame FAIL: " + t);
            logTrace(t);
            realGameRunning = false;
        }
    }

    private void log(String msg) {
        if (log.size() > 100) log.remove(0);
        log.add(msg);
        Gdx.app.log("TD", msg);
    }

    private void logTrace(Throwable t) {
        for (StackTraceElement el : t.getStackTrace()) {
            String s = el.toString();
            if (s.contains("terraindash")) {
                log("    " + s);
            }
        }
        if (t.getCause() != null) {
            log("  Caused by: " + t.getCause());
            logTrace(t.getCause());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) viewport.update(width, height, true);
        if (realGameRunning && realGame != null) realGame.resize(width, height);
    }

    @Override
    public void dispose() {
        if (realGame != null) realGame.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        com.terraindash.ui.SkinFactory.dispose();
    }
}
