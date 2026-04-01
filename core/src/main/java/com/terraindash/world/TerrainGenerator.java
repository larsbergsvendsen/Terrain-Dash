package com.terraindash.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.terraindash.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates terrain for both pre-designed levels and endless mode.
 * Terrain is divided into chunks that are loaded/unloaded as the player moves.
 */
public class TerrainGenerator {

    private final World physicsWorld;
    private final String worldId;
    private final int levelIndex;

    private final List<TerrainChunk> activeChunks = new ArrayList<>();
    private int currentChunkIndex = 0;

    public TerrainGenerator(World physicsWorld, String worldId, int levelIndex) {
        this.physicsWorld = physicsWorld;
        this.worldId = worldId;
        this.levelIndex = levelIndex;
    }

    public void generateTerrain() {
        for (int i = -1; i <= 3; i++) {
            loadChunk(i);
        }
    }

    public void updateChunks(float vehicleX) {
        int vehicleChunk = (int) (vehicleX / Constants.TERRAIN_CHUNK_WIDTH);

        if (vehicleChunk > currentChunkIndex) {
            currentChunkIndex = vehicleChunk;

            // Unload chunks that are far behind
            activeChunks.removeIf(chunk -> {
                if (chunk.getIndex() < currentChunkIndex - 2) {
                    chunk.dispose(physicsWorld);
                    return true;
                }
                return false;
            });

            // Load new chunks ahead
            for (int i = currentChunkIndex + 1; i <= currentChunkIndex + 3; i++) {
                if (!isChunkLoaded(i)) {
                    loadChunk(i);
                }
            }
        }
    }

    private boolean isChunkLoaded(int index) {
        for (TerrainChunk chunk : activeChunks) {
            if (chunk.getIndex() == index) return true;
        }
        return false;
    }

    private void loadChunk(int chunkIndex) {
        float startX = chunkIndex * Constants.TERRAIN_CHUNK_WIDTH;
        float[] heights = generateHeights(chunkIndex, startX);

        TerrainChunk chunk = new TerrainChunk(chunkIndex, startX, heights);
        chunk.createPhysicsBody(physicsWorld, getSurfaceFriction());
        activeChunks.add(chunk);
    }

    /**
     * Generate height values for a terrain chunk.
     * Uses layered sine waves for natural-looking hills.
     */
    private float[] generateHeights(int chunkIndex, float startX) {
        int numPoints = Constants.TERRAIN_POINTS_PER_CHUNK + 1;
        float[] heights = new float[numPoints];
        float dx = Constants.TERRAIN_CHUNK_WIDTH / Constants.TERRAIN_POINTS_PER_CHUNK;

        for (int i = 0; i < numPoints; i++) {
            float x = startX + i * dx;
            heights[i] = computeTerrainHeight(x);
        }

        return heights;
    }

    private float computeTerrainHeight(float x) {
        float baseHeight = 5f;

        // Layer 1: Broad hills
        float h = baseHeight + (float) Math.sin(x * 0.05) * 3f;
        // Layer 2: Medium bumps
        h += (float) Math.sin(x * 0.15 + 1.3) * 1.5f;
        // Layer 3: Small details
        h += (float) Math.sin(x * 0.4 + 2.7) * 0.5f;

        // World-specific terrain modifiers
        switch (worldId) {
            case "countryside":
                // Gentle, smooth hills
                break;
            case "desert":
                h += (float) Math.sin(x * 0.08 + 5.1) * 2f;
                break;
            case "arctic":
                h += (float) Math.sin(x * 0.3 + 0.7) * 1f;
                h += (float) Math.sin(x * 0.6) * 0.3f;
                break;
            case "neon":
                // Sharper, more angular terrain
                h += Math.signum((float) Math.sin(x * 0.2)) * 1.5f;
                break;
            case "volcano":
                h += (float) Math.sin(x * 0.1) * 4f;
                h += Math.abs((float) Math.sin(x * 0.25)) * 2f;
                break;
            case "sky":
                h += 3f;
                // Gaps handled separately as missing terrain sections
                break;
        }

        return Math.max(h, 1f);
    }

    private float getSurfaceFriction() {
        switch (worldId) {
            case "countryside": return Constants.FRICTION_GRASS;
            case "desert":      return Constants.FRICTION_SAND;
            case "arctic":      return Constants.FRICTION_ICE;
            case "neon":        return Constants.FRICTION_ASPHALT;
            case "volcano":     return Constants.FRICTION_DIRT;
            case "sky":         return Constants.FRICTION_ASPHALT;
            default:            return Constants.FRICTION_GRASS;
        }
    }

    public List<TerrainChunk> getActiveChunks() {
        return activeChunks;
    }
}
