package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.terraindash.TerrainDashGame;
import com.terraindash.ui.SkinFactory;

public class SettingsScreen extends ScreenAdapter {

    private final TerrainDashGame game;
    private Stage stage;

    public SettingsScreen(TerrainDashGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("SETTINGS", skin, "title")).colspan(2).padBottom(40).row();

        root.add(new Label("SFX Volume", skin)).padRight(20);
        Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);
        sfxSlider.setValue(game.getSaveManager().getSfxVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSaveManager().setSfxVolume(((Slider) actor).getValue());
            }
        });
        root.add(sfxSlider).width(300).padBottom(20);
        root.row();

        root.add(new Label("Tilt Sensitivity", skin)).padRight(20);
        Slider tiltSlider = new Slider(0.3f, 1.0f, 0.05f, false, skin);
        tiltSlider.setValue(game.getSaveManager().getTiltSensitivity());
        tiltSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSaveManager().setTiltSensitivity(((Slider) actor).getValue());
            }
        });
        root.add(tiltSlider).width(300).padBottom(40);
        root.row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        root.add(backBtn).colspan(2).width(200).height(60);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.14f, 1f);
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
