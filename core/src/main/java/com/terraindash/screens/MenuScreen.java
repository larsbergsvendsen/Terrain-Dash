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

public class MenuScreen extends ScreenAdapter {

    private final RealGame game;
    private Stage stage;

    public MenuScreen(RealGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = SkinFactory.create();
        float density = Math.max(1f, Gdx.graphics.getDensity());
        float btnW = 380 * density;
        float btnH = 90 * density;
        float pad = 16 * density;

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("TERRAIN DASH", skin, "title")).padBottom(pad * 4).row();

        TextButton playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        root.add(playButton).width(btnW).height(btnH).padBottom(pad).row();

        TextButton garageButton = new TextButton("GARAGE", skin);
        garageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GarageScreen(game));
            }
        });
        root.add(garageButton).width(btnW).height(btnH).padBottom(pad).row();

        TextButton settingsButton = new TextButton("SETTINGS", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        root.add(settingsButton).width(btnW).height(btnH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.14f, 1f);
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
    }
}
