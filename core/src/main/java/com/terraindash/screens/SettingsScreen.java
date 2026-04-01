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
import com.terraindash.RealGame;
import com.terraindash.ui.SkinFactory;

public class SettingsScreen extends ScreenAdapter {

    private final RealGame game;
    private Stage stage;

    public SettingsScreen(RealGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();
        float d = Math.max(1f, Gdx.graphics.getDensity());

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("SETTINGS", skin, "title")).colspan(2).padBottom(40 * d).row();

        root.add(new Label("SFX Volume", skin)).padRight(20 * d);
        Slider sfx = new Slider(0f, 1f, 0.05f, false, skin);
        sfx.setValue(game.getSaveManager().getSfxVolume());
        sfx.addListener(new ChangeListener() { @Override public void changed(ChangeEvent e, Actor a) {
            game.getSaveManager().setSfxVolume(((Slider) a).getValue()); }});
        root.add(sfx).width(360 * d).padBottom(20 * d); root.row();

        root.add(new Label("Tilt Sensitivity", skin)).padRight(20 * d);
        Slider tilt = new Slider(0.3f, 1f, 0.05f, false, skin);
        tilt.setValue(game.getSaveManager().getTiltSensitivity());
        tilt.addListener(new ChangeListener() { @Override public void changed(ChangeEvent e, Actor a) {
            game.getSaveManager().setTiltSensitivity(((Slider) a).getValue()); }});
        root.add(tilt).width(360 * d).padBottom(40 * d); root.row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new MenuScreen(game)); }});
        root.add(back).colspan(2).width(260 * d).height(70 * d);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
