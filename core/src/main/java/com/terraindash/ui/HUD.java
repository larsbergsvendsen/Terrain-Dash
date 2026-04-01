package com.terraindash.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Heads-Up Display showing speed, coins, nitro bar,
 * distance, and elapsed time during gameplay.
 */
public class HUD implements Disposable {

    private Stage stage;
    private BitmapFont font;

    private Label speedLabel;
    private Label coinsLabel;
    private Label distanceLabel;
    private Label timeLabel;
    private Label nitroLabel;

    public HUD(SpriteBatch batch) {
        stage = new Stage(new ScreenViewport(), batch);
        font = new BitmapFont();
        font.getData().setScale(2f);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        speedLabel = new Label("0 km/h", style);
        coinsLabel = new Label("0", style);
        distanceLabel = new Label("0 m", style);
        timeLabel = new Label("0:00", style);
        nitroLabel = new Label("NITRO", style);
        nitroLabel.setColor(Color.ORANGE);

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().pad(20);

        topTable.add(speedLabel).expandX().left().padLeft(20);
        topTable.add(distanceLabel).expandX().center();
        topTable.add(coinsLabel).expandX().right().padRight(20);
        topTable.row();
        topTable.add().expandX();
        topTable.add(timeLabel).expandX().center();
        topTable.add().expandX();

        stage.addActor(topTable);

        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().right().pad(20);
        bottomTable.add(nitroLabel);

        stage.addActor(bottomTable);
    }

    public void render(float speed, int coins, float nitro, float distance, float time) {
        speedLabel.setText(String.format("%.0f km/h", speed * 3.6f));
        coinsLabel.setText(String.valueOf(coins));
        distanceLabel.setText(String.format("%.0f m", distance));

        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        timeLabel.setText(String.format("%d:%02d", minutes, seconds));

        nitroLabel.setVisible(nitro > 0);
        if (nitro > 0) {
            nitroLabel.setText(String.format("NITRO %.1f", nitro));
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (font != null) font.dispose();
    }
}
