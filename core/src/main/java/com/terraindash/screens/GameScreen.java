package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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

    // UI overlay for drawing buttons
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    private float accumulator = 0f;
    private boolean paused = false;
    private boolean gameEnded = false;
    private boolean initialized = false;
    private float flipWarningCooldown = 0f;

    // Control state
    private boolean gasPressed = false;
    private float tiltInput = 0f;

    // Button layout (screen pixels, set in resize)
    private float btnSize;
    private float gasX, gasY;
    private float tiltLeftX, tiltLeftY;
    private float tiltRightX, tiltRightY;

    public GameScreen(RealGame game, String worldId, int levelIndex) {
        this.game = game;
        this.worldId = worldId;
        this.levelIndex = levelIndex;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show()");
        try {
            camera = new OrthographicCamera();
            viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
            viewport.apply(true);

            uiCamera = new OrthographicCamera();
            uiViewport = new ScreenViewport(uiCamera);

            hud = new HUD(game.getBatch());
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "show() error: " + e.getMessage(), e);
        }
    }

    private void initWorld() {
        if (initialized) return;
        initialized = true;
        try {
            assets = new AssetGenerator();
            assets.generateAll();
            gameWorld = new GameWorld(worldId, levelIndex, assets);
            cameraController = new CameraController(camera);
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "initWorld() error: " + e.getMessage(), e);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.2f, 0.35f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!initialized) { initWorld(); return; }
        if (gameWorld == null) return;

        if (paused || gameEnded) { draw(); return; }

        delta = Math.min(delta, 0.05f);

        try {
            int prevCoins = gameWorld.getCoinsCollected();
            boolean prevNitro = gameWorld.isNitroActive();

            handleTouchInput();
            gameWorld.handleInput(tiltInput, gasPressed);

            accumulator += delta;
            while (accumulator >= Constants.TIME_STEP) {
                gameWorld.stepPhysics(Constants.TIME_STEP);
                accumulator -= Constants.TIME_STEP;
            }

            gameWorld.update(delta);

            if (gameWorld.isVehicleFlipped()) { onGameOver(); return; }
            if (gameWorld.isLevelComplete()) { onLevelComplete(); return; }

            cameraController.update(delta, gameWorld.getVehiclePosition(), gameWorld.getVehicleSpeed());

            updateAudio(prevCoins, prevNitro);
            draw();
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "render error: " + e.getMessage(), e);
        }
    }

    private void handleTouchInput() {
        gasPressed = false;
        tiltInput = 0f;

        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;

            float tx = Gdx.input.getX(i);
            float ty = Gdx.input.getY(i);

            if (hitButton(tx, ty, gasX, gasY)) {
                gasPressed = true;
            }
            if (hitButton(tx, ty, tiltLeftX, tiltLeftY)) {
                tiltInput -= 1f;
            }
            if (hitButton(tx, ty, tiltRightX, tiltRightY)) {
                tiltInput += 1f;
            }
        }

        tiltInput = Math.max(-1f, Math.min(1f, tiltInput));
    }

    private boolean hitButton(float tx, float ty, float bx, float by) {
        float halfSize = btnSize * 0.65f;
        return tx > bx - halfSize && tx < bx + halfSize
            && ty > by - halfSize && ty < by + halfSize;
    }

    private void updateAudio(int prevCoins, boolean prevNitro) {
        SoundGenerator sg = game.getSoundGenerator();
        if (sg == null) return;

        if (gameWorld.getCoinsCollected() > prevCoins && sg.get("coin") != null) {
            float pitch = 1f + (gameWorld.getCoinsCollected() % 8) * 0.05f;
            sg.get("coin").play(0.5f, pitch, 0f);
        }
        if (gameWorld.isNitroActive() && !prevNitro && sg.get("nitro") != null) {
            sg.get("nitro").play(0.6f);
        }
        flipWarningCooldown -= Gdx.graphics.getDeltaTime();
        if (gameWorld.getFlipTimer() > 0.3f && flipWarningCooldown <= 0 && sg.get("flip_warning") != null) {
            sg.get("flip_warning").play(0.4f);
            flipWarningCooldown = 0.5f;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.3f, 0.55f, 0.85f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);
        gameWorld.render(batch, camera);

        // Draw HUD
        hud.render(gameWorld.getSpeed(), gameWorld.getCoinsCollected(),
            gameWorld.getNitroFuel(), gameWorld.getDistanceTraveled(), gameWorld.getElapsedTime());

        // Draw control buttons in screen space
        drawControls(batch);
    }

    private void drawControls(SpriteBatch batch) {
        if (assets == null) return;

        uiViewport.apply(true);
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        float half = btnSize / 2f;
        float screenW = uiViewport.getWorldWidth();
        float screenH = uiViewport.getWorldHeight();

        // Convert screen-touch coords to UI world coords for drawing
        float gasDrawX = screenW - btnSize * 0.8f;
        float gasDrawY = btnSize * 0.8f;
        float tiltLDrawX = btnSize * 0.8f;
        float tiltLDrawY = btnSize * 0.8f;
        float tiltRDrawX = btnSize * 2.2f;
        float tiltRDrawY = btnSize * 0.8f;

        Color prevColor = batch.getColor().cpy();

        TextureRegion gasReg = assets.getRegion("btn_gas");
        TextureRegion tiltLReg = assets.getRegion("btn_tilt_left");
        TextureRegion tiltRReg = assets.getRegion("btn_tilt_right");

        batch.setColor(gasPressed ? new Color(1f, 1f, 1f, 0.9f) : new Color(1f, 1f, 1f, 0.5f));
        if (gasReg != null) batch.draw(gasReg, gasDrawX - half, gasDrawY - half, btnSize, btnSize);

        batch.setColor(tiltInput < 0 ? new Color(1f, 1f, 1f, 0.9f) : new Color(1f, 1f, 1f, 0.5f));
        if (tiltLReg != null) batch.draw(tiltLReg, tiltLDrawX - half, tiltLDrawY - half, btnSize, btnSize);

        batch.setColor(tiltInput > 0 ? new Color(1f, 1f, 1f, 0.9f) : new Color(1f, 1f, 1f, 0.5f));
        if (tiltRReg != null) batch.draw(tiltRReg, tiltRDrawX - half, tiltRDrawY - half, btnSize, btnSize);

        batch.setColor(prevColor);
        batch.end();
    }

    private void onGameOver() {
        gameEnded = true;
        SoundGenerator sg = game.getSoundGenerator();
        if (sg != null && sg.get("crash") != null) sg.get("crash").play(0.7f);
        if (cameraController != null) cameraController.shake(0.5f, 0.5f);
        Gdx.app.postRunnable(() -> game.setScreen(new ResultScreen(game, worldId, levelIndex, false,
            gameWorld.getCoinsCollected(), gameWorld.getDistanceTraveled(), gameWorld.getElapsedTime())));
    }

    private void onLevelComplete() {
        gameEnded = true;
        SoundGenerator sg = game.getSoundGenerator();
        if (sg != null && sg.get("star") != null) sg.get("star").play(0.6f);
        Gdx.app.postRunnable(() -> game.setScreen(new ResultScreen(game, worldId, levelIndex, true,
            gameWorld.getCoinsCollected(), gameWorld.getDistanceTraveled(), gameWorld.getElapsedTime())));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        uiViewport.update(width, height, true);
        if (hud != null) hud.resize(width, height);

        // Compute button positions in screen-touch coordinates (y=0 at top)
        btnSize = Math.min(width, height) * 0.16f;
        float margin = btnSize * 0.8f;

        gasX = width - margin;
        gasY = height - margin;

        tiltLeftX = margin;
        tiltLeftY = height - margin;

        tiltRightX = margin + btnSize * 1.4f;
        tiltRightY = height - margin;
    }

    @Override
    public void pause() { paused = true; }
    @Override
    public void resume() { paused = false; }

    @Override
    public void dispose() {
        if (gameWorld != null) gameWorld.dispose();
        if (hud != null) hud.dispose();
        if (assets != null) assets.dispose();
    }
}
