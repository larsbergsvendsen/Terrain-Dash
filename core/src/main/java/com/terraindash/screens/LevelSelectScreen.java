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
import com.terraindash.RealGame;
import com.terraindash.ui.SkinFactory;

public class LevelSelectScreen extends ScreenAdapter {

    private final RealGame game;
    private final String worldId;
    private final String worldName;
    private Stage stage;

    public LevelSelectScreen(RealGame game, String worldId, String worldName) {
        this.game = game;
        this.worldId = worldId;
        this.worldName = worldName;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();
        float d = Math.max(1f, Gdx.graphics.getDensity());

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label(worldName.toUpperCase(), skin, "title")).colspan(3).padBottom(40 * d).row();

        Table grid = new Table();
        for (int i = 0; i < 5; i++) {
            final int levelIndex = i;
            int stars = game.getSaveManager().getLevelStars(worldId, i);
            StringBuilder label = new StringBuilder("Level " + (i + 1));
            for (int s = 0; s < stars; s++) label.append(" *");

            TextButton btn = new TextButton(label.toString(), skin);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game, worldId, levelIndex));
                }
            });
            grid.add(btn).width(340 * d).height(80 * d).pad(10 * d);
            if ((i + 1) % 3 == 0) grid.row();
        }
        root.add(grid).colspan(3).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        root.add(backBtn).colspan(3).width(260 * d).height(70 * d).padTop(30 * d);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.08f, 0.16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
