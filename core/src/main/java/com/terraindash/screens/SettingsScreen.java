package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.terraindash.TerrainDashGame;

public class SettingsScreen extends ScreenAdapter {

    private final TerrainDashGame game;
    private Stage stage;
    private Skin skin;

    public SettingsScreen(TerrainDashGame game) {
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

        root.add(new Label("SETTINGS", skin, "title")).colspan(2).padBottom(40).row();

        // Music volume
        root.add(new Label("Music", skin)).padRight(20);
        Slider musicSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicSlider.setValue(game.getSaveManager().getMusicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float vol = ((Slider) actor).getValue();
                game.getSaveManager().setMusicVolume(vol);
                game.getMusicManager().setVolume(vol);
            }
        });
        root.add(musicSlider).width(300).row().padBottom(20);

        // SFX volume
        root.add(new Label("SFX", skin)).padRight(20);
        Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);
        sfxSlider.setValue(game.getSaveManager().getSfxVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float vol = ((Slider) actor).getValue();
                game.getSaveManager().setSfxVolume(vol);
                game.getSfxManager().setVolume(vol);
            }
        });
        root.add(sfxSlider).width(300).row().padBottom(20);

        // Tilt sensitivity
        root.add(new Label("Tilt Sensitivity", skin)).padRight(20);
        Slider tiltSlider = new Slider(0.3f, 1.0f, 0.05f, false, skin);
        tiltSlider.setValue(game.getSaveManager().getTiltSensitivity());
        tiltSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getSaveManager().setTiltSensitivity(((Slider) actor).getValue());
            }
        });
        root.add(tiltSlider).width(300).row().padBottom(40);

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
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.18f, 1f);
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
