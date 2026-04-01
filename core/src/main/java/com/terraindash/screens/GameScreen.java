package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.terraindash.TerrainDashGame;
import com.terraindash.effects.CameraController;
import com.terraindash.ui.HUD;
import com.terraindash.utils.Constants;
import com.terraindash.world.GameWorld;

public class GameScreen extends ScreenAdapter {

    private final TerrainDashGame game;
    private final String worldId;
    private final int levelIndex;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private GameWorld gameWorld;
    private CameraController cameraController;
    private HUD hud;

    private float accumulator = 0f;
    private boolean paused = false;

    public GameScreen(TerrainDashGame game, String worldId, int levelIndex) {
        this.game = game;
        this.worldId = worldId;
        this.levelIndex = levelIndex;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);

        gameWorld = new GameWorld(worldId, levelIndex);
        cameraController = new CameraController(camera);
        hud = new HUD(game.getBatch());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(gameWorld.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        if (paused) return;

        delta = Math.min(delta, 0.05f);

        handleInput();
        updatePhysics(delta);
        updateLogic(delta);
        updateCamera(delta);
        draw();
    }

    private void handleInput() {
        float touchX = -1;
        boolean touching = Gdx.input.isTouched();

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

    private void draw() {
        Gdx.gl.glClearColor(0.4f, 0.7f, 0.9f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        gameWorld.render(batch);
        batch.end();

        hud.render(
            gameWorld.getSpeed(),
            gameWorld.getCoinsCollected(),
            gameWorld.getNitroFuel(),
            gameWorld.getDistanceTraveled(),
            gameWorld.getElapsedTime()
        );
    }

    private void onGameOver() {
        game.setScreen(new ResultScreen(game, worldId, levelIndex, false,
            gameWorld.getCoinsCollected(),
            gameWorld.getDistanceTraveled(),
            gameWorld.getElapsedTime()));
    }

    private void onLevelComplete() {
        game.setScreen(new ResultScreen(game, worldId, levelIndex, true,
            gameWorld.getCoinsCollected(),
            gameWorld.getDistanceTraveled(),
            gameWorld.getElapsedTime()));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        hud.resize(width, height);
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
    }
}
