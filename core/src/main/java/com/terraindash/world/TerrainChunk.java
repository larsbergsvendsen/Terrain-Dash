package com.terraindash.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.terraindash.utils.Constants;

/**
 * A segment of terrain represented as a chain of height points.
 * Each chunk creates a Box2D ChainShape for collision and stores
 * the visual mesh data for rendering.
 */
public class TerrainChunk {

    private final int index;
    private final float startX;
    private final float[] heights;
    private Body body;

    public TerrainChunk(int index, float startX, float[] heights) {
        this.index = index;
        this.startX = startX;
        this.heights = heights;
    }

    public void createPhysicsBody(World world, float friction) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        body = world.createBody(bodyDef);
        body.setUserData("terrain");

        float dx = Constants.TERRAIN_CHUNK_WIDTH / Constants.TERRAIN_POINTS_PER_CHUNK;
        Vector2[] vertices = new Vector2[heights.length];

        for (int i = 0; i < heights.length; i++) {
            vertices[i] = new Vector2(startX + i * dx, heights[i]);
        }

        ChainShape shape = new ChainShape();
        shape.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = friction;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_TERRAIN;
        fixtureDef.filter.maskBits = Constants.CATEGORY_VEHICLE;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void dispose(World world) {
        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    public int getIndex() {
        return index;
    }

    public float getStartX() {
        return startX;
    }

    public float[] getHeights() {
        return heights;
    }

    public float getEndX() {
        return startX + Constants.TERRAIN_CHUNK_WIDTH;
    }

    public float getHeightAt(float x) {
        if (x < startX || x > getEndX()) return 0f;

        float dx = Constants.TERRAIN_CHUNK_WIDTH / Constants.TERRAIN_POINTS_PER_CHUNK;
        int idx = (int) ((x - startX) / dx);
        idx = Math.max(0, Math.min(idx, heights.length - 2));

        float t = ((x - startX) - idx * dx) / dx;
        return heights[idx] * (1 - t) + heights[idx + 1] * t;
    }
}
