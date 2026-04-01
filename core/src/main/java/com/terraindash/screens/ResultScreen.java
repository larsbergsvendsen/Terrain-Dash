package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.terraindash.TerrainDashGame;

public class ResultScreen extends ScreenAdapter {

    private final TerrainDashGame game;
    private final String worldId;
    private final int levelIndex;
    private final boolean completed;
    private final int coinsCollected;
    private final float distance;
    private final float time;

    private Stage stage;
    private Skin skin;

    public ResultScreen(TerrainDashGame game, String worldId, int levelIndex,
                        boolean completed, int coinsCollected, float distance, float time) {
        this.game = game;
        this.worldId = worldId;
        this.levelIndex = levelIndex;
        this.completed = completed;
        this.coinsCollected = coinsCollected;
        this.distance = distance;
        this.time = time;
    }

    @Override
    public void show() {
        // Save progress
        game.getSaveManager().addCoins(coinsCollected);
        if (completed) {
            game.getSaveManager().setLevelBestTime(worldId, levelIndex, time);
            game.getSaveManager().addPlayerXP(100 + coinsCollected * 2);
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/default-skin.json"));

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        String title = completed ? "LEVEL COMPLETE!" : "CRASHED!";
        root.add(new Label(title, skin, "title")).padBottom(40).row();

        root.add(new Label(String.format("Distance: %.0f m", distance), skin)).padBottom(10).row();
        root.add(new Label(String.format("Coins: %d", coinsCollected), skin)).padBottom(10).row();

        if (completed) {
            int minutes = (int) (time / 60);
            int seconds = (int) (time % 60);
            root.add(new Label(String.format("Time: %d:%02d", minutes, seconds), skin)).padBottom(10).row();
        }

        root.add().height(30).row();

        TextButton retryBtn = new TextButton("RETRY", skin);
        retryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, worldId, levelIndex));
            }
        });
        root.add(retryBtn).width(250).height(70).padBottom(15).row();

        if (completed) {
            TextButton nextBtn = new TextButton("NEXT LEVEL", skin);
            nextBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, worldId, levelIndex + 1));
                }
            });
            root.add(nextBtn).width(250).height(70).padBottom(15).row();
        }

        TextButton menuBtn = new TextButton("MENU", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        root.add(menuBtn).width(250).height(70);
    }

    @Override
    public void render(float delta) {
        float r = completed ? 0.1f : 0.2f;
        float g = completed ? 0.2f : 0.05f;
        Gdx.gl.glClearColor(r, g, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}
