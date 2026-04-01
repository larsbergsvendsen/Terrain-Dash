package com.terraindash.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Renders the game world: parallax background, terrain surface,
 * and game entities (coins, obstacles, decorations).
 */
public class WorldRenderer implements Disposable {

    private final TerrainGenerator terrainGenerator;

    public WorldRenderer(TerrainGenerator terrainGenerator) {
        this.terrainGenerator = terrainGenerator;
    }

    public void renderBackground(SpriteBatch batch) {
        // TODO: Render 3-4 parallax background layers
        // Each layer scrolls at a different speed relative to camera
        // Layer 0: Sky gradient (slowest)
        // Layer 1: Far mountains / clouds
        // Layer 2: Mid-ground trees / buildings
        // Layer 3: Near-ground bushes / rocks
    }

    public void renderTerrain(SpriteBatch batch) {
        // TODO: For each active chunk, render filled polygon below terrain line
        // 1. Build triangle strip from terrain points down to bottom
        // 2. Apply surface texture based on world type
        // 3. Draw terrain edge/grass on top
        for (TerrainChunk chunk : terrainGenerator.getActiveChunks()) {
            renderChunk(batch, chunk);
        }
    }

    private void renderChunk(SpriteBatch batch, TerrainChunk chunk) {
        // TODO: Implement terrain mesh rendering
        // Will use ShapeRenderer or custom mesh for filled terrain
    }

    public void renderEntities(SpriteBatch batch) {
        // TODO: Render coins, boost pads, obstacles, and decorations
    }

    @Override
    public void dispose() {
        // Dispose textures, meshes, etc.
    }
}
