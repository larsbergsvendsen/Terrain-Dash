package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
        stage = new Stage(new ExtendViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        stage.addActor(root);

        Label title = new Label("TERRAIN DASH", skin, "title");
        title.setColor(0.4f, 0.7f, 1f, 1f);
        root.add(title).padBottom(50).row();

        Label sub = new Label("A physics racing game", skin, "small");
        sub.setColor(0.5f, 0.55f, 0.65f, 1f);
        root.add(sub).padBottom(40).row();

        addButton(root, skin, "PLAY", 320, 56, 12, () -> game.setScreen(new WorldSelectScreen(game)));
        addButton(root, skin, "GARAGE", 320, 56, 12, () -> game.setScreen(new GarageScreen(game)));
        addButton(root, skin, "SETTINGS", 320, 56, 0, () -> game.setScreen(new SettingsScreen(game)));
    }

    private void addButton(Table root, Skin skin, String text, float w, float h, float pad, Runnable action) {
        TextButton btn = new TextButton(text, skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { action.run(); }
        });
        root.add(btn).width(w).height(h).padBottom(pad).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
