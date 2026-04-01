package com.terraindash.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.terraindash.utils.AssetGenerator;
import com.terraindash.utils.Constants;

import java.util.List;

/**
 * Renders the game world: parallax background layers, terrain as filled
 * polygons with surface textures, and collectible entities.
 */
public class WorldRenderer implements Disposable {

    private final TerrainGenerator terrainGenerator;
    private final String worldId;
    private final AssetGenerator assets;
    private final ShapeRenderer shapeRenderer;

    private static final float[] PARALLAX_SPEEDS = {0.05f, 0.15f, 0.3f};

    public WorldRenderer(TerrainGenerator terrainGenerator, String worldId, AssetGenerator assets) {
        this.terrainGenerator = terrainGenerator;
        this.worldId = worldId;
        this.assets = assets;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void renderBackground(SpriteBatch batch, OrthographicCamera camera) {
        float camX = camera.position.x;
        float camY = camera.position.y;
        float viewW = camera.viewportWidth * camera.zoom;
        float viewH = camera.viewportHeight * camera.zoom;
        float left = camX - viewW / 2f;
        float bottom = camY - viewH / 2f;

        String worldKey = getWorldKey();

        TextureRegion skyRegion = assets.getRegion("bg_" + worldKey);
        if (skyRegion != null) {
            batch.draw(skyRegion, left, bottom, viewW, viewH);
        }

        TextureRegion mountains = assets.getRegion("mountains_" + worldKey);
        if (mountains != null) {
            float parallaxX = camX * PARALLAX_SPEEDS[0];
            float mw = viewW * 1.5f;
            float startX = left - (parallaxX % mw);
            for (float x = startX; x < left + viewW + mw; x += mw) {
                batch.draw(mountains, x, bottom + viewH * 0.2f, mw, viewH * 0.4f);
            }
        }

        TextureRegion trees = assets.getRegion("trees_layer");
        if (trees != null) {
            float parallaxX = camX * PARALLAX_SPEEDS[1];
            float tw = viewW * 1.2f;
            float startX = left - (parallaxX % tw);
            for (float x = startX; x < left + viewW + tw; x += tw) {
                batch.draw(trees, x, bottom + viewH * 0.1f, tw, viewH * 0.25f);
            }
        }

        TextureRegion cloud = assets.getRegion("cloud");
        if (cloud != null) {
            float cw = 4f, ch = 1.5f;
            for (int i = 0; i < 6; i++) {
                float cx = (float) (i * 12 + Math.sin(i * 2.3) * 5) - camX * 0.03f;
                float cy = bottom + viewH * (0.7f + (float) Math.sin(i * 1.7) * 0.12f);
                float wrappedX = cx - (float) Math.floor((cx - left) / (viewW * 2)) * viewW * 2;
                batch.draw(cloud, wrappedX, cy, cw * (1f + i * 0.2f), ch);
            }
        }
    }

    public void renderTerrain(SpriteBatch batch, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        String worldKey = getWorldKey();
        Color topColor = getTerrainTopColor(worldKey);
        Color bottomColor = getTerrainBottomColor(worldKey);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float camLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2f;
        float camRight = camera.position.x + camera.viewportWidth * camera.zoom / 2f;

        for (TerrainChunk chunk : terrainGenerator.getActiveChunks()) {
            if (chunk.getEndX() < camLeft || chunk.getStartX() > camRight) continue;
            renderChunkFill(chunk, topColor, bottomColor);
        }

        shapeRenderer.end();

        renderTerrainSurface(camera, worldKey);
    }

    private void renderChunkFill(TerrainChunk chunk, Color topColor, Color bottomColor) {
        float[] heights = chunk.getHeights();
        float dx = Constants.TERRAIN_CHUNK_WIDTH / Constants.TERRAIN_POINTS_PER_CHUNK;
        float bottomY = -Constants.TERRAIN_DEPTH;

        for (int i = 0; i < heights.length - 1; i++) {
            float x1 = chunk.getStartX() + i * dx;
            float x2 = chunk.getStartX() + (i + 1) * dx;
            float y1 = heights[i];
            float y2 = heights[i + 1];

            shapeRenderer.triangle(
                x1, y1, x2, y2, x1, bottomY,
                topColor, topColor, bottomColor
            );
            shapeRenderer.triangle(
                x2, y2, x2, bottomY, x1, bottomY,
                topColor, bottomColor, bottomColor
            );
        }
    }

    private void renderTerrainSurface(OrthographicCamera camera, String worldKey) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        Color edgeColor = getTerrainEdgeColor(worldKey);
        Color edgeColorDark = new Color(edgeColor.r * 0.7f, edgeColor.g * 0.7f, edgeColor.b * 0.7f, 1f);

        float camLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2f;
        float camRight = camera.position.x + camera.viewportWidth * camera.zoom / 2f;
        float edgeThickness = 0.15f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (TerrainChunk chunk : terrainGenerator.getActiveChunks()) {
            if (chunk.getEndX() < camLeft || chunk.getStartX() > camRight) continue;

            float[] heights = chunk.getHeights();
            float dx = Constants.TERRAIN_CHUNK_WIDTH / Constants.TERRAIN_POINTS_PER_CHUNK;

            for (int i = 0; i < heights.length - 1; i++) {
                float x1 = chunk.getStartX() + i * dx;
                float x2 = chunk.getStartX() + (i + 1) * dx;
                float y1 = heights[i];
                float y2 = heights[i + 1];

                shapeRenderer.triangle(
                    x1, y1, x2, y2, x1, y1 - edgeThickness,
                    edgeColor, edgeColor, edgeColorDark
                );
                shapeRenderer.triangle(
                    x2, y2, x2, y2 - edgeThickness, x1, y1 - edgeThickness,
                    edgeColor, edgeColorDark, edgeColorDark
                );
            }
        }

        shapeRenderer.end();
    }

    public void renderEntities(SpriteBatch batch, OrthographicCamera camera,
                                List<Vector2> coinPositions, List<Vector2> boostPositions) {
        TextureRegion coinRegion = assets.getRegion("coin");
        TextureRegion boostRegion = assets.getRegion("boost_pad");

        float camLeft = camera.position.x - camera.viewportWidth * camera.zoom / 2f;
        float camRight = camera.position.x + camera.viewportWidth * camera.zoom / 2f;

        if (coinRegion != null && coinPositions != null) {
            for (Vector2 pos : coinPositions) {
                if (pos.x < camLeft - 1 || pos.x > camRight + 1) continue;
                float coinSize = Constants.COIN_RADIUS * 2;
                float bobOffset = (float) Math.sin(pos.x * 0.5 + Gdx.graphics.getFrameId() * 0.05) * 0.15f;
                batch.draw(coinRegion,
                    pos.x - coinSize / 2, pos.y + bobOffset - coinSize / 2,
                    coinSize, coinSize);
            }
        }

        if (boostRegion != null && boostPositions != null) {
            for (Vector2 pos : boostPositions) {
                if (pos.x < camLeft - 1 || pos.x > camRight + 1) continue;
                batch.draw(boostRegion, pos.x - 0.75f, pos.y - 0.2f, 1.5f, 0.5f);
            }
        }
    }

    private String getWorldKey() {
        switch (worldId) {
            case "countryside": return "countryside";
            case "desert":      return "desert";
            case "arctic":      return "arctic";
            case "neon":        return "neon";
            case "volcano":     return "volcano";
            case "sky":         return "sky";
            default:            return "countryside";
        }
    }

    private Color getTerrainTopColor(String worldKey) {
        switch (worldKey) {
            case "countryside": return new Color(0.35f, 0.65f, 0.2f, 1f);
            case "desert":      return new Color(0.85f, 0.75f, 0.5f, 1f);
            case "arctic":      return new Color(0.85f, 0.92f, 0.98f, 1f);
            case "neon":        return new Color(0.15f, 0.1f, 0.25f, 1f);
            case "volcano":     return new Color(0.45f, 0.25f, 0.15f, 1f);
            case "sky":         return new Color(0.75f, 0.85f, 0.95f, 1f);
            default:            return new Color(0.35f, 0.65f, 0.2f, 1f);
        }
    }

    private Color getTerrainBottomColor(String worldKey) {
        switch (worldKey) {
            case "countryside": return new Color(0.25f, 0.4f, 0.15f, 1f);
            case "desert":      return new Color(0.65f, 0.5f, 0.3f, 1f);
            case "arctic":      return new Color(0.5f, 0.6f, 0.7f, 1f);
            case "neon":        return new Color(0.05f, 0.02f, 0.1f, 1f);
            case "volcano":     return new Color(0.2f, 0.1f, 0.05f, 1f);
            case "sky":         return new Color(0.5f, 0.6f, 0.75f, 1f);
            default:            return new Color(0.25f, 0.4f, 0.15f, 1f);
        }
    }

    private Color getTerrainEdgeColor(String worldKey) {
        switch (worldKey) {
            case "countryside": return new Color(0.45f, 0.75f, 0.25f, 1f);
            case "desert":      return new Color(0.9f, 0.8f, 0.55f, 1f);
            case "arctic":      return new Color(0.95f, 0.97f, 1f, 1f);
            case "neon":        return new Color(0f, 1f, 0.8f, 1f);
            case "volcano":     return new Color(1f, 0.4f, 0.1f, 1f);
            case "sky":         return new Color(0.9f, 0.95f, 1f, 1f);
            default:            return new Color(0.45f, 0.75f, 0.25f, 1f);
        }
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
