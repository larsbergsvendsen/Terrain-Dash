package com.terraindash.world;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.terraindash.entities.Vehicle;
import com.terraindash.physics.ContactHandler;
import com.terraindash.physics.VehiclePhysics;
import com.terraindash.utils.Constants;

/**
 * Central game world class that owns the Box2D physics world,
 * terrain, vehicle, and all game entities.
 */
public class GameWorld implements Disposable {

    private final String worldId;
    private final int levelIndex;

    private World physicsWorld;
    private Vehicle vehicle;
    private TerrainGenerator terrainGenerator;
    private ContactHandler contactHandler;
    private WorldRenderer renderer;

    private int coinsCollected = 0;
    private float nitroFuel = 0f;
    private float elapsedTime = 0f;
    private float distanceTraveled = 0f;
    private float startX;
    private boolean levelComplete = false;

    private float tiltInput = 0f;

    public GameWorld(String worldId, int levelIndex) {
        this.worldId = worldId;
        this.levelIndex = levelIndex;

        physicsWorld = new World(new Vector2(0, Constants.GRAVITY), true);
        contactHandler = new ContactHandler(this);
        physicsWorld.setContactListener(contactHandler);

        terrainGenerator = new TerrainGenerator(physicsWorld, worldId, levelIndex);
        terrainGenerator.generateTerrain();

        vehicle = VehiclePhysics.createVehicle(physicsWorld, new Vector2(2f, 8f));
        startX = vehicle.getPosition().x;

        renderer = new WorldRenderer(terrainGenerator);
    }

    public void handleInput(boolean touching, float touchXNormalized) {
        if (!touching) {
            tiltInput = 0f;
            return;
        }

        if (touchXNormalized < 0.5f) {
            tiltInput = -1f; // tilt back (nose up)
        } else {
            tiltInput = 1f;  // tilt forward (nose down)
        }
    }

    public void stepPhysics(float timeStep) {
        VehiclePhysics.applyMotorForce(vehicle);
        VehiclePhysics.applyTiltTorque(vehicle, tiltInput);

        if (nitroFuel > 0) {
            VehiclePhysics.applyNitroBoost(vehicle);
        }

        physicsWorld.step(timeStep, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    }

    public void update(float delta) {
        elapsedTime += delta;
        distanceTraveled = vehicle.getPosition().x - startX;

        if (nitroFuel > 0) {
            nitroFuel -= delta;
            if (nitroFuel < 0) nitroFuel = 0;
        }

        terrainGenerator.updateChunks(vehicle.getPosition().x);
    }

    public void render(SpriteBatch batch) {
        renderer.renderBackground(batch);
        renderer.renderTerrain(batch);
        vehicle.render(batch);
        renderer.renderEntities(batch);
    }

    public boolean isVehicleFlipped() {
        float angle = Math.abs(vehicle.getChassisAngle());
        return angle > Constants.FLIP_ANGLE_THRESHOLD;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean complete) {
        this.levelComplete = complete;
    }

    public void collectCoin() {
        coinsCollected++;
    }

    public void addNitro(float amount) {
        nitroFuel = Math.min(nitroFuel + amount, Constants.NITRO_DURATION);
    }

    // Getters for HUD and camera
    public Vector2 getVehiclePosition() {
        return vehicle.getPosition();
    }

    public float getVehicleSpeed() {
        return vehicle.getSpeed();
    }

    public float getSpeed() {
        return vehicle.getSpeed();
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public float getNitroFuel() {
        return nitroFuel;
    }

    public float getDistanceTraveled() {
        return distanceTraveled;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public InputProcessor getInputProcessor() {
        return null; // Touch handled directly in GameScreen
    }

    @Override
    public void dispose() {
        if (physicsWorld != null) physicsWorld.dispose();
        if (renderer != null) renderer.dispose();
    }
}
