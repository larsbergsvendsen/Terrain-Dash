package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.terraindash.TerrainDashGame;

public class WorldSelectScreen extends ScreenAdapter {

    private static final String[][] WORLDS = {
        {"countryside", "Countryside"},
        {"desert", "Desert Storm"},
        {"arctic", "Arctic Rush"},
        {"neon", "Neon City"},
        {"volcano", "Volcano Ridge"},
        {"sky", "Sky Highway"}
    };

    private final TerrainDashGame game;
    private Stage stage;
    private Skin skin;

    public WorldSelectScreen(TerrainDashGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/default-skin.json"));

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("SELECT WORLD", skin, "title")).padBottom(40).row();

        for (int i = 0; i < WORLDS.length; i++) {
            final String worldId = WORLDS[i][0];
            final String worldName = WORLDS[i][1];
            final int worldIndex = i;

            TextButton btn = new TextButton(worldName, skin);
            boolean unlocked = game.getSaveManager().getPlayerLevel() > i;

            btn.setDisabled(!unlocked);
            if (unlocked) {
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new LevelSelectScreen(game, worldId, worldName));
                    }
                });
            }

            root.add(btn).width(350).height(70).padBottom(15).row();
        }

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        root.add(backBtn).width(200).height(60).padTop(30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f);
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
