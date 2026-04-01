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
 * Rendered as a separate Stage overlay on top of the game world.
 */
public class HUD implements Disposable {

    private final Stage stage;
    private final BitmapFont font;
    private final BitmapFont smallFont;

    private final Label speedLabel;
    private final Label coinsLabel;
    private final Label distanceLabel;
    private final Label timeLabel;
    private final Label nitroLabel;

    public HUD(SpriteBatch batch) {
        stage = new Stage(new ScreenViewport(), batch);

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.5f);

        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle smallStyle = new Label.LabelStyle(smallFont, Color.WHITE);

        speedLabel = new Label("0 km/h", style);
        coinsLabel = new Label("0", style);
        distanceLabel = new Label("0 m", smallStyle);
        timeLabel = new Label("0:00", smallStyle);
        nitroLabel = new Label("", style);
        nitroLabel.setColor(1f, 0.6f, 0f, 1f);

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().pad(15);

        topTable.add(speedLabel).expandX().left().padLeft(15);
        topTable.add(distanceLabel).expandX().center();
        topTable.add(coinsLabel).expandX().right().padRight(15);
        topTable.row().padTop(5);
        topTable.add();
        topTable.add(timeLabel).expandX().center();
        topTable.add();

        stage.addActor(topTable);

        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().right().pad(15);
        bottomTable.add(nitroLabel);

        stage.addActor(bottomTable);
    }

    /**
     * Render the HUD overlay. This ends the current game batch and renders
     * the Stage in screen coordinates, then returns.
     */
    public void render(float speed, int coins, float nitro, float distance, float time) {
        speedLabel.setText(String.format("%.0f km/h", speed * 3.6f));
        coinsLabel.setText(coins + " coins");
        distanceLabel.setText(String.format("%.0f m", distance));

        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        timeLabel.setText(String.format("%d:%02d", minutes, seconds));

        if (nitro > 0) {
            nitroLabel.setVisible(true);
            int bars = (int) (nitro / 3f * 10);
            StringBuilder sb = new StringBuilder("NITRO ");
            for (int i = 0; i < 10; i++) {
                sb.append(i < bars ? "|" : ".");
            }
            nitroLabel.setText(sb.toString());
            float pulse = 0.7f + 0.3f * (float) Math.sin(time * 10);
            nitroLabel.setColor(1f, pulse * 0.6f, 0f, 1f);
        } else {
            nitroLabel.setVisible(false);
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
        if (smallFont != null) smallFont.dispose();
    }
}
