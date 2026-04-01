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

public class ResultScreen extends ScreenAdapter {

    private final RealGame game;
    private final String worldId;
    private final int levelIndex;
    private final boolean completed;
    private final int coinsCollected;
    private final float distance, time;
    private Stage stage;

    public ResultScreen(RealGame game, String worldId, int levelIndex,
                        boolean completed, int coinsCollected, float distance, float time) {
        this.game = game; this.worldId = worldId; this.levelIndex = levelIndex;
        this.completed = completed; this.coinsCollected = coinsCollected;
        this.distance = distance; this.time = time;
    }

    @Override
    public void show() {
        game.getSaveManager().addCoins(coinsCollected);
        if (completed) {
            game.getSaveManager().setLevelBestTime(worldId, levelIndex, time);
            game.getSaveManager().addPlayerXP(100 + coinsCollected * 2);
            int stars = 1;
            if (coinsCollected >= 15) stars = 2;
            if (coinsCollected >= 20 && time < 60) stars = 3;
            game.getSaveManager().setLevelStars(worldId, levelIndex, stars);
        }

        stage = new Stage(new ExtendViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        Skin skin = SkinFactory.create();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        stage.addActor(root);

        Label title = new Label(completed ? "LEVEL COMPLETE!" : "CRASHED!", skin, "title");
        title.setColor(completed ? 0.3f : 1f, completed ? 1f : 0.3f, completed ? 0.5f : 0.2f, 1f);
        root.add(title).padBottom(30).row();

        root.add(new Label(String.format("Distance: %.0f m", distance), skin)).padBottom(8).row();
        root.add(new Label(String.format("Coins: %d", coinsCollected), skin)).padBottom(8).row();
        if (completed) {
            int m = (int)(time / 60), s = (int)(time % 60);
            root.add(new Label(String.format("Time: %d:%02d", m, s), skin)).padBottom(8).row();
        }

        root.add().height(20).row();

        TextButton retry = new TextButton("RETRY", skin);
        retry.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new GameScreen(game, worldId, levelIndex)); }});
        root.add(retry).width(280).height(52).padBottom(10).row();

        if (completed && levelIndex < 4) {
            TextButton next = new TextButton("NEXT LEVEL", skin);
            next.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new GameScreen(game, worldId, levelIndex + 1)); }});
            root.add(next).width(280).height(52).padBottom(10).row();
        }

        TextButton menu = new TextButton("MENU", skin);
        menu.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new MenuScreen(game)); }});
        root.add(menu).width(280).height(52);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.09f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
