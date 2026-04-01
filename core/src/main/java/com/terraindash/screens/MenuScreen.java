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
import com.terraindash.ui.SkinFactory;

public class MenuScreen extends ScreenAdapter {

    private final TerrainDashGame game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(TerrainDashGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("TERRAIN DASH", skin, "title");
        root.add(title).padBottom(60).row();

        TextButton playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        root.add(playButton).width(300).height(80).padBottom(20).row();

        TextButton garageButton = new TextButton("GARAGE", skin);
        garageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GarageScreen(game));
            }
        });
        root.add(garageButton).width(300).height(80).padBottom(20).row();

        TextButton endlessButton = new TextButton("ENDLESS MODE", skin);
        endlessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        root.add(endlessButton).width(300).height(80).padBottom(20).row();

        TextButton settingsButton = new TextButton("SETTINGS", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        root.add(settingsButton).width(300).height(80);

        game.getMusicManager().play("menu");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.18f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getMusicManager().update(delta);
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
    }
}
