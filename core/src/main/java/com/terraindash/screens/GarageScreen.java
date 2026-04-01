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

public class GarageScreen extends ScreenAdapter {

    private static final String[][] VEHICLES = {
        {"buggy", "Buggy"}, {"monstertruck", "Monster Truck"}, {"sportscar", "Sports Car"},
        {"rocketbike", "Rocket Bike"}, {"tank", "Tank"}, {"hovercraft", "Hovercraft"}
    };
    private static final String[] UPGRADES = {"engine", "suspension", "wheels", "nitro", "armor"};
    private static final int[] COSTS = {200, 300, 450, 650, 900, 1200, 1600, 2100, 2700, 3500};

    private final RealGame game;
    private Stage stage;
    private Skin skin;
    private int sel = 0;

    public GarageScreen(RealGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        skin = SkinFactory.create();
        buildUI();
    }

    private void buildUI() {
        stage.clear();
        Table root = new Table();
        root.setFillParent(true);
        root.top().padTop(20);
        stage.addActor(root);

        root.add(new Label("GARAGE", skin, "title")).colspan(3).padBottom(10).row();
        root.add(new Label("Coins: " + game.getSaveManager().getCoins(), skin, "small")).colspan(3).padBottom(15).row();

        String vid = VEHICLES[sel][0], vname = VEHICLES[sel][1];
        boolean unlocked = game.getSaveManager().isVehicleUnlocked(vid);

        TextButton prev = new TextButton("<", skin);
        prev.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            sel = (sel - 1 + VEHICLES.length) % VEHICLES.length; buildUI(); }});
        TextButton next = new TextButton(">", skin);
        next.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            sel = (sel + 1) % VEHICLES.length; buildUI(); }});

        root.add(prev).width(60).height(48);
        root.add(new Label(vname + (unlocked ? "" : " [LOCKED]"), skin)).padLeft(12).padRight(12);
        root.add(next).width(60).height(48);
        root.row().padTop(12);

        if (unlocked) {
            for (String up : UPGRADES) {
                int lv = game.getSaveManager().getUpgradeLevel(vid, up);
                String label = up.substring(0,1).toUpperCase() + up.substring(1) + " Lv." + lv;
                if (lv < 10) label += "  [" + COSTS[lv] + "]";
                else label += "  MAX";
                TextButton btn = new TextButton(label, skin);
                final String ut = up; final int ul = lv;
                if (lv < 10) btn.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
                    if (game.getSaveManager().spendCoins(COSTS[ul])) {
                        game.getSaveManager().setUpgradeLevel(vid, ut, ul + 1); buildUI(); }}});
                else btn.setDisabled(true);
                root.add(btn).colspan(3).width(420).height(42).padBottom(5).row();
            }
        }

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new MenuScreen(game)); }});
        root.add(back).colspan(3).width(200).height(44).padTop(15);
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
