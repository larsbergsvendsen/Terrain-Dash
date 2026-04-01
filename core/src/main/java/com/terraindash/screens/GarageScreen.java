package com.terraindash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.terraindash.RealGame;
import com.terraindash.ui.SkinFactory;

public class GarageScreen extends ScreenAdapter {

    private static final String[][] VEHICLES = {
        {"buggy", "Buggy"}, {"monstertruck", "Monster Truck"}, {"sportscar", "Sports Car"},
        {"rocketbike", "Rocket Bike"}, {"tank", "Tank"}, {"hovercraft", "Hovercraft"}
    };
    private static final String[] UPGRADES = {"engine", "suspension", "wheels", "nitro", "armor"};
    private static final int[] UPGRADE_COSTS = {200, 300, 450, 650, 900, 1200, 1600, 2100, 2700, 3500};

    private final RealGame game;
    private Stage stage;
    private Skin skin;
    private int selectedVehicleIndex = 0;
    private float d;

    public GarageScreen(RealGame game) { this.game = game; }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = SkinFactory.create();
        d = Math.max(1f, Gdx.graphics.getDensity());
        buildUI();
    }

    private void buildUI() {
        stage.clear();
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("GARAGE", skin, "title")).colspan(3).padBottom(20 * d).row();
        root.add(new Label("Coins: " + game.getSaveManager().getCoins(), skin)).colspan(3).padBottom(16 * d).row();

        String vehicleId = VEHICLES[selectedVehicleIndex][0];
        String vehicleName = VEHICLES[selectedVehicleIndex][1];
        boolean unlocked = game.getSaveManager().isVehicleUnlocked(vehicleId);

        TextButton prevBtn = new TextButton("<", skin);
        prevBtn.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            selectedVehicleIndex = (selectedVehicleIndex - 1 + VEHICLES.length) % VEHICLES.length; buildUI(); }});
        TextButton nextBtn = new TextButton(">", skin);
        nextBtn.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            selectedVehicleIndex = (selectedVehicleIndex + 1) % VEHICLES.length; buildUI(); }});

        root.add(prevBtn).width(80 * d).height(80 * d);
        root.add(new Label(vehicleName + (unlocked ? "" : " [LOCKED]"), skin)).padLeft(16 * d).padRight(16 * d);
        root.add(nextBtn).width(80 * d).height(80 * d);
        root.row().padTop(16 * d);

        if (unlocked) {
            for (String upgrade : UPGRADES) {
                int level = game.getSaveManager().getUpgradeLevel(vehicleId, upgrade);
                String label = capitalize(upgrade) + " Lv." + level;
                if (level < 10) label += " -> " + UPGRADE_COSTS[level];
                else label += " MAX";
                TextButton btn = new TextButton(label, skin);
                final String ut = upgrade; final int ul = level;
                if (level < 10) btn.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
                    if (game.getSaveManager().spendCoins(UPGRADE_COSTS[ul])) {
                        game.getSaveManager().setUpgradeLevel(vehicleId, ut, ul + 1); buildUI(); }}});
                else btn.setDisabled(true);
                root.add(btn).colspan(3).width(460 * d).height(65 * d).padBottom(8 * d).row();
            }
        }

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) {
            game.setScreen(new MenuScreen(game)); }});
        root.add(backBtn).colspan(3).width(260 * d).height(70 * d).padTop(24 * d);
    }

    private String capitalize(String s) { return s.substring(0, 1).toUpperCase() + s.substring(1); }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }

    @Override
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override
    public void dispose() { if (stage != null) stage.dispose(); }
}
