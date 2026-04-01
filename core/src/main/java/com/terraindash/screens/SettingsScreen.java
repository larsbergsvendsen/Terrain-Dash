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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.terraindash.RealGame;
import com.terraindash.ui.SkinFactory;

public class SettingsScreen extends ScreenAdapter {

    private final RealGame game;
    private Stage stage;

    public SettingsScreen(RealGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true); root.center();
        stage.addActor(root);

        root.add(new Label("SETTINGS", skin, "title")).colspan(2).padBottom(35).row();

        root.add(new Label("SFX Volume", skin)).padRight(20);
        Slider sfx = new Slider(0, 1, 0.05f, false, skin);
        sfx.setValue(game.getSaveManager().getSfxVolume());
        sfx.addListener(new ChangeListener() { @Override public void changed(ChangeEvent e, Actor a) {
            game.getSaveManager().setSfxVolume(((Slider)a).getValue()); }});
        root.add(sfx).width(300).padBottom(18); root.row();

        root.add(new Label("Tilt Sensitivity", skin)).padRight(20);
        Slider tilt = new Slider(0.3f, 1, 0.05f, false, skin);
        tilt.setValue(game.getSaveManager().getTiltSensitivity());
        tilt.addListener(new ChangeListener() { @Override public void changed(ChangeEvent e, Actor a) {
            game.getSaveManager().setTiltSensitivity(((Slider)a).getValue()); }});
        root.add(tilt).width(300).padBottom(35); root.row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new MenuScreen(game)); }});
        root.add(back).colspan(2).width(200).height(48);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
