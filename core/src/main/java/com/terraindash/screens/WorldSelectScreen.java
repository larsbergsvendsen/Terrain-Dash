package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.terraindash.RealGame;
import com.terraindash.ui.SkinFactory;

public class WorldSelectScreen extends ScreenAdapter {

    private static final String[][] WORLDS = {
        {"countryside", "Countryside"},
        {"desert", "Desert Storm"},
        {"arctic", "Arctic Rush"},
        {"neon", "Neon City"},
        {"volcano", "Volcano Ridge"},
        {"sky", "Sky Highway"}
    };

    private final RealGame game;
    private Stage stage;

    public WorldSelectScreen(RealGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true);
        root.top().padTop(30);
        stage.addActor(root);

        root.add(new Label("SELECT WORLD", skin, "title")).padBottom(25).row();

        // Two-column grid
        Table grid = new Table();
        for (int i = 0; i < WORLDS.length; i++) {
            final String wid = WORLDS[i][0];
            final String wname = WORLDS[i][1];
            boolean unlocked = game.getSaveManager().getPlayerLevel() > i;

            String label = (unlocked ? "" : "[locked] ") + wname;
            TextButton btn = new TextButton(label, skin);
            btn.setDisabled(!unlocked);
            if (unlocked) {
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        game.setScreen(new LevelSelectScreen(game, wid, wname));
                    }
                });
            }
            grid.add(btn).width(350).height(52).pad(6);
            if (i % 2 == 1) grid.row();
        }
        root.add(grid).row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) { game.setScreen(new MenuScreen(game)); }
        });
        root.add(back).width(200).height(48).padTop(20);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
