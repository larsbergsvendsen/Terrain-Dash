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

public class LevelSelectScreen extends ScreenAdapter {

    private final RealGame game;
    private final String worldId, worldName;
    private Stage stage;

    public LevelSelectScreen(RealGame game, String worldId, String worldName) {
        this.game = game; this.worldId = worldId; this.worldName = worldName;
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

        root.add(new Label(worldName.toUpperCase(), skin, "title")).padBottom(30).colspan(5).row();

        for (int i = 0; i < 5; i++) {
            final int idx = i;
            int stars = game.getSaveManager().getLevelStars(worldId, i);
            String label = String.valueOf(i + 1);
            if (stars > 0) { StringBuilder sb = new StringBuilder(label); for (int s = 0; s < stars; s++) sb.append("*"); label = sb.toString(); }

            TextButton btn = new TextButton(label, skin);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent e, float x, float y) {
                    game.setScreen(new GameScreen(game, worldId, idx));
                }
            });
            root.add(btn).width(120).height(80).pad(8);
        }
        root.row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) { game.setScreen(new WorldSelectScreen(game)); }
        });
        root.add(back).colspan(5).width(200).height(48).padTop(30);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.06f, 0.13f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
