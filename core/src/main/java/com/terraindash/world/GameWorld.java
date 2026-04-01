package com.terraindash.world;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.terraindash.effects.ParticleManager;
import com.terraindash.entities.Vehicle;
import com.terraindash.entities.VehicleConfig;
import com.terraindash.physics.ContactHandler;
import com.terraindash.physics.VehiclePhysics;
import com.terraindash.utils.AssetGenerator;
import com.terraindash.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Central game world class that owns the Box2D physics world,
 * terrain, vehicle, collectibles, particles, and all game entities.
 */
public class GameWorld implements Disposable {

    private final String worldId;
    private final int levelIndex;

    private World physicsWorld;
    private Vehicle vehicle;
    private TerrainGenerator terrainGenerator;
    private ContactHandler contactHandler;
    private WorldRenderer renderer;
    private ParticleManager particles;
    private AssetGenerator assets;

    private final List<CoinEntity> coins = new ArrayList<>();
    private final List<BoostEntity> boosts = new ArrayList<>();
    private final List<Vector2> activeCoinPositions = new ArrayList<>();
    private final List<Vector2> activeBoostPositions = new ArrayList<>();

    private int coinsCollected = 0;
    private float nitroFuel = 0f;
    private float elapsedTime = 0f;
    private float distanceTraveled = 0f;
    private float startX;
    private boolean levelComplete = false;
    private boolean vehicleFlipped = false;
    private float flipTimer = 0f;

    private float tiltInput = 0f;
    private float dustTimer = 0f;
    private boolean wasInAir = false;
    private float previousVy = 0f;

    public GameWorld(String worldId, int levelIndex, AssetGenerator assets) {
        this.worldId = worldId;
        this.levelIndex = levelIndex;
        this.assets = assets;

        physicsWorld = new World(new Vector2(0, Constants.GRAVITY), true);
        contactHandler = new ContactHandler(this);
        physicsWorld.setContactListener(contactHandler);

        terrainGenerator = new TerrainGenerator(physicsWorld, worldId, levelIndex);
        terrainGenerator.generateTerrain();

        VehicleConfig config = VehicleConfig.buggy();
        vehicle = VehiclePhysics.createVehicle(physicsWorld, new Vector2(2f, 8f), config);
        vehicle.setTextures(
            assets.getRegion("chassis_" + config.id),
            assets.getRegion("wheel")
        );
        startX = vehicle.getPosition().x;

        renderer = new WorldRenderer(terrainGenerator, worldId, assets);

        particles = new ParticleManager();
        particles.setTextures(assets.getRegion("particle"), assets.getRegion("nitro_flame"));

        spawnEntities();
    }

    public void setVehicleConfig(VehicleConfig config) {
        // For garage integration: rebuild vehicle with new config
    }

    private void spawnEntities() {
        float levelLength = 500f;

        // Generate coins along the terrain
        for (float x = 10f; x < levelLength; x += 8f + (float) Math.sin(x * 0.1) * 5f) {
            float terrainY = terrainGenerator.getHeightAt(x);
            if (terrainY > 0) {
                float coinY = terrainY + 1.5f + (float) Math.sin(x * 0.3) * 0.8f;
                CoinEntity coin = new CoinEntity(x, coinY);
                coin.body = createCoinSensor(x, coinY);
                coins.add(coin);
            }
        }

        // Generate boost pads
        for (float x = 60f; x < levelLength; x += 80f + (float) Math.sin(x * 0.07) * 30f) {
            float terrainY = terrainGenerator.getHeightAt(x);
            if (terrainY > 0) {
                BoostEntity boost = new BoostEntity(x, terrainY + 0.3f);
                boost.body = createBoostSensor(x, terrainY + 0.3f);
                boosts.add(boost);
            }
        }

        // Finish line sensor
        createFinishSensor(levelLength);
    }

    private Body createCoinSensor(float x, float y) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(x, y);
        Body body = physicsWorld.createBody(bd);
        body.setUserData("coin");

        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.COIN_RADIUS);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.CATEGORY_COIN;
        fd.filter.maskBits = Constants.CATEGORY_VEHICLE;

        body.createFixture(fd);
        shape.dispose();
        return body;
    }

    private Body createBoostSensor(float x, float y) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(x, y);
        Body body = physicsWorld.createBody(bd);
        body.setUserData("boost");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.75f, 0.2f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.CATEGORY_BOOST;
        fd.filter.maskBits = Constants.CATEGORY_VEHICLE;

        body.createFixture(fd);
        shape.dispose();
        return body;
    }

    private void createFinishSensor(float x) {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(x, 10f);
        Body body = physicsWorld.createBody(bd);
        body.setUserData("finish");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 15f);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.isSensor = true;
        fd.filter.categoryBits = Constants.CATEGORY_SENSOR;
        fd.filter.maskBits = Constants.CATEGORY_VEHICLE;

        body.createFixture(fd);
        shape.dispose();
    }

    public void handleInput(boolean touching, float touchXNormalized) {
        if (!touching) {
            tiltInput = 0f;
            return;
        }

        if (touchXNormalized < 0.5f) {
            tiltInput = -1f;
        } else {
            tiltInput = 1f;
        }
    }

    public void stepPhysics(float timeStep) {
        VehiclePhysics.applyMotorForce(vehicle);
        VehiclePhysics.applyTiltTorque(vehicle, tiltInput);
        VehiclePhysics.applyGroundedStabilization(vehicle);

        if (nitroFuel > 0) {
            VehiclePhysics.applyNitroBoost(vehicle);
        }

        physicsWorld.step(timeStep, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
    }

    public void update(float delta) {
        elapsedTime += delta;
        distanceTraveled = Math.max(distanceTraveled, vehicle.getPosition().x - startX);

        // Nitro countdown
        if (nitroFuel > 0) {
            nitroFuel -= delta;
            if (nitroFuel < 0) nitroFuel = 0;

            particles.spawn(ParticleManager.EffectType.NITRO_FLAME,
                getRearPosition(), vehicle.getChassisAngle());
        }

        // Dust particles when grounded
        boolean inAir = VehiclePhysics.isInAir(vehicle);
        if (!inAir && vehicle.getHorizontalSpeed() > 2f) {
            dustTimer += delta;
            if (dustTimer > 0.05f) {
                dustTimer = 0f;
                particles.spawn(ParticleManager.EffectType.DUST_TRAIL,
                    getRearWheelPosition(), 0f, vehicle.getHorizontalSpeed());
            }
        }

        // Landing detection
        if (wasInAir && !inAir) {
            float impact = Math.abs(previousVy);
            if (impact > 3f) {
                particles.spawn(ParticleManager.EffectType.LANDING_SPARK,
                    vehicle.getPosition(), 0f, impact);
            }
        }
        wasInAir = inAir;
        previousVy = vehicle.getLinearVelocity().y;

        // Flip detection
        float angle = Math.abs(vehicle.getChassisAngle());
        float normalizedAngle = angle % (float) (2 * Math.PI);
        if (normalizedAngle > Math.PI) normalizedAngle = (float) (2 * Math.PI) - normalizedAngle;

        if (normalizedAngle > Constants.FLIP_ANGLE_THRESHOLD) {
            flipTimer += delta;
            if (flipTimer > 0.8f) {
                vehicleFlipped = true;
                particles.spawn(ParticleManager.EffectType.EXPLOSION, vehicle.getPosition());
            }
        } else {
            flipTimer = 0f;
        }

        // Remove collected coins
        Iterator<CoinEntity> coinIt = coins.iterator();
        while (coinIt.hasNext()) {
            CoinEntity coin = coinIt.next();
            if (coin.collected) {
                particles.spawn(ParticleManager.EffectType.COIN_BURST,
                    new Vector2(coin.x, coin.y));
                if (coin.body != null) {
                    physicsWorld.destroyBody(coin.body);
                }
                coinIt.remove();
            }
        }

        // Remove used boosts
        Iterator<BoostEntity> boostIt = boosts.iterator();
        while (boostIt.hasNext()) {
            BoostEntity boost = boostIt.next();
            if (boost.used) {
                if (boost.body != null) {
                    physicsWorld.destroyBody(boost.body);
                }
                boostIt.remove();
            }
        }

        // Rebuild visible position lists for renderer
        activeCoinPositions.clear();
        for (CoinEntity coin : coins) {
            activeCoinPositions.add(new Vector2(coin.x, coin.y));
        }
        activeBoostPositions.clear();
        for (BoostEntity boost : boosts) {
            activeBoostPositions.add(new Vector2(boost.x, boost.y));
        }

        // Vehicle fell off world
        if (vehicle.getPosition().y < -5f) {
            vehicleFlipped = true;
        }

        terrainGenerator.updateChunks(vehicle.getPosition().x);
        particles.update(delta);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        // Background: batch open
        batch.begin();
        renderer.renderBackground(batch, camera);
        batch.end();

        // Terrain: uses ShapeRenderer (no batch)
        renderer.renderTerrain(batch, camera);

        // Entities, vehicle, particles: batch open
        batch.begin();
        renderer.renderEntities(batch, camera, activeCoinPositions, activeBoostPositions);
        vehicle.render(batch);
        particles.render(batch);
        batch.end();
    }

    private Vector2 getRearPosition() {
        Vector2 pos = vehicle.getPosition();
        float angle = vehicle.getChassisAngle();
        return new Vector2(
            pos.x - (float) Math.cos(angle) * vehicle.getConfig().chassisWidth * 0.5f,
            pos.y - (float) Math.sin(angle) * vehicle.getConfig().chassisWidth * 0.5f
        );
    }

    private Vector2 getRearWheelPosition() {
        return vehicle.getWheelRear().getPosition().cpy();
    }

    // Called by ContactHandler
    public void collectCoin() {
        float vx = vehicle.getPosition().x;
        float vy = vehicle.getPosition().y;
        for (CoinEntity coin : coins) {
            if (!coin.collected) {
                float dx = coin.x - vx;
                float dy = coin.y - vy;
                if (dx * dx + dy * dy < 9f) {
                    coin.collected = true;
                    coinsCollected++;
                    return;
                }
            }
        }
    }

    public void addNitro(float amount) {
        float vx = vehicle.getPosition().x;
        float vy = vehicle.getPosition().y;
        for (BoostEntity boost : boosts) {
            if (!boost.used) {
                float dx = boost.x - vx;
                float dy = boost.y - vy;
                if (dx * dx + dy * dy < 9f) {
                    boost.used = true;
                    nitroFuel = Math.min(nitroFuel + amount, Constants.NITRO_DURATION);
                    return;
                }
            }
        }
    }

    public boolean isVehicleFlipped() {
        return vehicleFlipped;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean complete) {
        this.levelComplete = complete;
    }

    private final Vector2 tempPosition = new Vector2();

    public Vector2 getVehiclePosition() {
        return tempPosition.set(vehicle.getPosition());
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

    public float getFlipTimer() {
        return flipTimer;
    }

    public boolean isNitroActive() {
        return nitroFuel > 0;
    }

    public InputProcessor getInputProcessor() {
        return null;
    }

    @Override
    public void dispose() {
        if (physicsWorld != null) physicsWorld.dispose();
        if (renderer != null) renderer.dispose();
        if (particles != null) particles.dispose();
    }

    // Inner entity classes
    static class CoinEntity {
        float x, y;
        boolean collected = false;
        Body body;
        CoinEntity(float x, float y) { this.x = x; this.y = y; }
    }

    static class BoostEntity {
        float x, y;
        boolean used = false;
        Body body;
        BoostEntity(float x, float y) { this.x = x; this.y = y; }
    }
}
