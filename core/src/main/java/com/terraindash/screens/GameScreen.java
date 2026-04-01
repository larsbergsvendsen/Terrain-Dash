package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.terraindash.RealGame;
import com.terraindash.audio.SoundGenerator;
import com.terraindash.effects.CameraController;
import com.terraindash.ui.HUD;
import com.terraindash.utils.AssetGenerator;
import com.terraindash.utils.Constants;
import com.terraindash.world.GameWorld;

public class GameScreen extends ScreenAdapter {

    private final RealGame game;
    private final String worldId;
    private final int levelIndex;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private GameWorld gameWorld;
    private CameraController cameraController;
    private HUD hud;
    private AssetGenerator assets;

    private float accumulator = 0f;
    private boolean paused = false;
    private boolean gameEnded = false;
    private boolean initialized = false;
    private float flipWarningCooldown = 0f;

    public GameScreen(RealGame game, String worldId, int levelIndex) {
        this.game = game;
        this.worldId = worldId;
        this.levelIndex = levelIndex;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show() worldId=" + worldId + " level=" + levelIndex);

        try {
            camera = new OrthographicCamera();
            viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
            viewport.apply(true);

            hud = new HUD(game.getBatch());

            InputMultiplexer multiplexer = new InputMultiplexer();
            multiplexer.addProcessor(hud.getStage());
            Gdx.input.setInputProcessor(multiplexer);

            Gdx.app.log("GameScreen", "show() done, will init world on first render");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "CRASH in show(): " + e.getMessage(), e);
        }
    }

    private void initWorld() {
        if (initialized) return;
        initialized = true;

        try {
            Gdx.app.log("GameScreen", "initWorld() generating assets...");
            assets = new AssetGenerator();
            assets.generateAll();
            Gdx.app.log("GameScreen", "initWorld() assets done, creating world...");

            gameWorld = new GameWorld(worldId, levelIndex, assets);
            cameraController = new CameraController(camera);

            Gdx.app.log("GameScreen", "initWorld() complete");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "CRASH in initWorld(): " + e.getMessage(), e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // no music

        if (!initialized) {
            initWorld();
            return;
        }

        if (gameWorld == null) return;

        if (paused || gameEnded) {
            draw();
            return;
        }

        delta = Math.min(delta, 0.05f);

        try {
            int prevCoins = gameWorld.getCoinsCollected();
            boolean prevNitro = gameWorld.isNitroActive();

            handleInput();
            updatePhysics(delta);
            updateLogic(delta);
            updateCamera(delta);
            updateAudio(delta, prevCoins, prevNitro);
            draw();
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error in render: " + e.getMessage(), e);
        }
    }

    private void handleInput() {
        boolean touching = Gdx.input.isTouched();
        float touchX = -1;

        if (touching) {
            touchX = (float) Gdx.input.getX() / Gdx.graphics.getWidth();
        }

        gameWorld.handleInput(touching, touchX);
    }

    private void updatePhysics(float delta) {
        accumulator += delta;
        while (accumulator >= Constants.TIME_STEP) {
            gameWorld.stepPhysics(Constants.TIME_STEP);
            accumulator -= Constants.TIME_STEP;
        }
    }

    private void updateLogic(float delta) {
        gameWorld.update(delta);

        if (gameWorld.isVehicleFlipped()) {
            onGameOver();
        }

        if (gameWorld.isLevelComplete()) {
            onLevelComplete();
        }
    }

    private void updateCamera(float delta) {
        cameraController.update(
            delta,
            gameWorld.getVehiclePosition(),
            gameWorld.getVehicleSpeed()
        );
    }

    private void updateAudio(float delta, int prevCoins, boolean prevNitro) {
        SoundGenerator soundGen = game.getSoundGenerator();
        if (soundGen == null) return;

        if (gameWorld.getCoinsCollected() > prevCoins) {
            float pitch = 1f + (gameWorld.getCoinsCollected() % 8) * 0.05f;
            if (soundGen.get("coin") != null) {
                soundGen.get("coin").play(0.5f, pitch, 0f);
            }
        }

        if (gameWorld.isNitroActive() && !prevNitro) {
            if (soundGen.get("nitro") != null) {
                soundGen.get("nitro").play(0.6f);
            }
        }

        flipWarningCooldown -= delta;
        if (gameWorld.getFlipTimer() > 0.3f && flipWarningCooldown <= 0) {
            if (soundGen.get("flip_warning") != null) {
                soundGen.get("flip_warning").play(0.4f);
            }
            flipWarningCooldown = 0.5f;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.4f, 0.7f, 0.9f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);

        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        gameWorld.render(batch, camera);

        hud.render(
            gameWorld.getSpeed(),
            gameWorld.getCoinsCollected(),
            gameWorld.getNitroFuel(),
            gameWorld.getDistanceTraveled(),
            gameWorld.getElapsedTime()
        );
    }

    private void onGameOver() {
        gameEnded = true;
        SoundGenerator soundGen = game.getSoundGenerator();
        if (soundGen != null && soundGen.get("crash") != null) {
            soundGen.get("crash").play(0.7f);
        }
        cameraController.shake(0.5f, 0.5f);
        Gdx.app.postRunnable(() -> {
            game.setScreen(new ResultScreen(game, worldId, levelIndex, false,
                gameWorld.getCoinsCollected(),
                gameWorld.getDistanceTraveled(),
                gameWorld.getElapsedTime()));
        });
    }

    private void onLevelComplete() {
        gameEnded = true;
        SoundGenerator soundGen = game.getSoundGenerator();
        if (soundGen != null && soundGen.get("star") != null) {
            soundGen.get("star").play(0.6f);
        }
        Gdx.app.postRunnable(() -> {
            game.setScreen(new ResultScreen(game, worldId, levelIndex, true,
                gameWorld.getCoinsCollected(),
                gameWorld.getDistanceTraveled(),
                gameWorld.getElapsedTime()));
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        if (hud != null) hud.resize(width, height);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
        if (gameWorld != null) gameWorld.dispose();
        if (hud != null) hud.dispose();
        if (assets != null) assets.dispose();
    }
}
