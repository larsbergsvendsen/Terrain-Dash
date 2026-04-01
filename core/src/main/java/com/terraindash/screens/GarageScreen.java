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
import com.terraindash.TerrainDashGame;
import com.terraindash.entities.VehicleConfig;

public class GarageScreen extends ScreenAdapter {

    private static final String[][] VEHICLES = {
        {"buggy", "Buggy"},
        {"monstertruck", "Monster Truck"},
        {"sportscar", "Sports Car"},
        {"rocketbike", "Rocket Bike"},
        {"tank", "Tank"},
        {"hovercraft", "Hovercraft"}
    };

    private static final String[] UPGRADES = {
        "engine", "suspension", "wheels", "nitro", "armor"
    };

    private static final int[] UPGRADE_COSTS = {
        200, 300, 450, 650, 900, 1200, 1600, 2100, 2700, 3500
    };

    private final TerrainDashGame game;
    private Stage stage;
    private Skin skin;
    private int selectedVehicleIndex = 0;

    public GarageScreen(TerrainDashGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/default-skin.json"));
        buildUI();
    }

    private void buildUI() {
        stage.clear();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("GARAGE", skin, "title")).colspan(3).padBottom(30).row();
        root.add(new Label("Coins: " + game.getSaveManager().getCoins(), skin))
            .colspan(3).padBottom(20).row();

        String vehicleId = VEHICLES[selectedVehicleIndex][0];
        String vehicleName = VEHICLES[selectedVehicleIndex][1];
        boolean unlocked = game.getSaveManager().isVehicleUnlocked(vehicleId);

        // Vehicle navigation
        TextButton prevBtn = new TextButton("<", skin);
        prevBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedVehicleIndex = (selectedVehicleIndex - 1 + VEHICLES.length) % VEHICLES.length;
                buildUI();
            }
        });

        TextButton nextBtn = new TextButton(">", skin);
        nextBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedVehicleIndex = (selectedVehicleIndex + 1) % VEHICLES.length;
                buildUI();
            }
        });

        root.add(prevBtn).width(60).height(60);
        root.add(new Label(vehicleName + (unlocked ? "" : " [LOCKED]"), skin)).padLeft(20).padRight(20);
        root.add(nextBtn).width(60).height(60);
        root.row().padTop(20);

        if (unlocked) {
            // Upgrade buttons
            for (String upgrade : UPGRADES) {
                int level = game.getSaveManager().getUpgradeLevel(vehicleId, upgrade);
                String label = capitalize(upgrade) + " Lv." + level;

                if (level < 10) {
                    int cost = UPGRADE_COSTS[level];
                    label += " → " + cost + " coins";
                } else {
                    label += " MAX";
                }

                TextButton upgradeBtn = new TextButton(label, skin);
                final String upType = upgrade;
                final int upLevel = level;

                if (level < 10) {
                    upgradeBtn.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            int cost = UPGRADE_COSTS[upLevel];
                            if (game.getSaveManager().spendCoins(cost)) {
                                game.getSaveManager().setUpgradeLevel(vehicleId, upType, upLevel + 1);
                                buildUI();
                            }
                        }
                    });
                } else {
                    upgradeBtn.setDisabled(true);
                }

                root.add(upgradeBtn).colspan(3).width(400).height(55).padBottom(8).row();
            }

            String selected = game.getSaveManager().getSelectedVehicle();
            if (!vehicleId.equals(selected)) {
                TextButton selectBtn = new TextButton("SELECT", skin);
                selectBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.getSaveManager().setSelectedVehicle(vehicleId);
                        buildUI();
                    }
                });
                root.add(selectBtn).colspan(3).width(200).height(60).padTop(15).row();
            } else {
                root.add(new Label("SELECTED", skin)).colspan(3).padTop(15).row();
            }
        }

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        root.add(backBtn).colspan(3).width(200).height(60).padTop(30);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
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
