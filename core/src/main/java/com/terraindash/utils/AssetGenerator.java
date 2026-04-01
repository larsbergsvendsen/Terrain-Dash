package com.terraindash.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates all game textures procedurally at runtime using Pixmaps.
 * No external image files are needed.
 */
public class AssetGenerator implements Disposable {

    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, TextureRegion> regions = new HashMap<>();

    public void generateAll() {
        try {
            generateVehicleTextures();
            generateWheelTexture();
            generateTerrainTextures();
            generateCoinTexture();
            generateBoostPadTexture();
            generateBackgroundTextures();
            generateNitroFlameTexture();
            generateParticleTexture();
            generateStarTexture();
            Gdx.app.log("AssetGenerator", "All textures generated: " + textures.size());
        } catch (Exception e) {
            Gdx.app.error("AssetGenerator", "Error generating assets: " + e.getMessage(), e);
        }
    }

    // ---- Vehicle textures ----

    private void generateVehicleTextures() {
        generateBuggyChassis();
        generateMonsterTruckChassis();
        generateSportsCarChassis();
        generateRocketBikeChassis();
        generateTankChassis();
        generateHovercraftChassis();
    }

    private void generateBuggyChassis() {
        int w = 128, h = 48;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.2f, 0.6f, 0.9f, 1f);
        pm.fillRectangle(8, 12, 112, 28);
        pm.setColor(0.15f, 0.5f, 0.8f, 1f);
        pm.fillRectangle(16, 4, 60, 12);
        pm.setColor(0.7f, 0.85f, 1f, 0.7f);
        pm.fillRectangle(20, 5, 24, 9);
        pm.fillRectangle(48, 5, 20, 9);
        pm.setColor(0.1f, 0.4f, 0.7f, 1f);
        pm.fillRectangle(8, 38, 112, 4);
        pm.setColor(1f, 0.8f, 0f, 1f);
        pm.fillRectangle(108, 18, 12, 8);
        pm.setColor(1f, 0f, 0f, 1f);
        pm.fillRectangle(0, 18, 8, 8);
        store("chassis_buggy", pm);
    }

    private void generateMonsterTruckChassis() {
        int w = 160, h = 64;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.8f, 0.2f, 0.1f, 1f);
        pm.fillRectangle(10, 16, 140, 36);
        pm.setColor(0.6f, 0.15f, 0.08f, 1f);
        pm.fillRectangle(20, 4, 80, 16);
        pm.setColor(0.7f, 0.85f, 1f, 0.6f);
        pm.fillRectangle(24, 6, 30, 12);
        pm.fillRectangle(60, 6, 28, 12);
        pm.setColor(0.9f, 0.9f, 0f, 1f);
        pm.fillRectangle(144, 22, 14, 10);
        pm.setColor(1f, 0f, 0f, 1f);
        pm.fillRectangle(0, 24, 10, 10);
        pm.setColor(0.3f, 0.3f, 0.3f, 1f);
        pm.fillRectangle(10, 48, 140, 6);
        store("chassis_monstertruck", pm);
    }

    private void generateSportsCarChassis() {
        int w = 140, h = 36;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.9f, 0.1f, 0.2f, 1f);
        pm.fillRectangle(4, 10, 132, 20);
        pm.setColor(0.75f, 0.08f, 0.15f, 1f);
        pm.fillRectangle(30, 2, 50, 10);
        pm.setColor(0.7f, 0.85f, 1f, 0.7f);
        pm.fillRectangle(34, 3, 42, 8);
        pm.setColor(1f, 0.9f, 0.2f, 1f);
        pm.fillRectangle(130, 14, 10, 8);
        pm.setColor(1f, 0f, 0f, 0.8f);
        pm.fillRectangle(0, 14, 6, 8);
        pm.setColor(0.7f, 0.08f, 0.12f, 1f);
        for (int x = 4; x < 136; x += 2) {
            pm.drawPixel(x, 28);
        }
        store("chassis_sportscar", pm);
    }

    private void generateRocketBikeChassis() {
        int w = 100, h = 40;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.1f, 0.1f, 0.1f, 1f);
        pm.fillRectangle(10, 14, 80, 16);
        pm.setColor(0.9f, 0.5f, 0f, 1f);
        pm.fillRectangle(60, 8, 30, 8);
        pm.setColor(0.3f, 0.3f, 0.3f, 1f);
        pm.fillRectangle(20, 4, 20, 12);
        pm.setColor(0.7f, 0.85f, 1f, 0.6f);
        pm.fillRectangle(24, 5, 14, 8);
        pm.setColor(1f, 0.3f, 0f, 1f);
        pm.fillRectangle(86, 16, 14, 8);
        pm.setColor(0.2f, 0.2f, 0.2f, 1f);
        pm.fillRectangle(10, 28, 80, 6);
        store("chassis_rocketbike", pm);
    }

    private void generateTankChassis() {
        int w = 180, h = 72;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.35f, 0.4f, 0.3f, 1f);
        pm.fillRectangle(6, 20, 168, 40);
        pm.setColor(0.3f, 0.35f, 0.25f, 1f);
        pm.fillRectangle(20, 6, 60, 18);
        pm.setColor(0.4f, 0.45f, 0.35f, 1f);
        pm.fillRectangle(70, 12, 80, 10);
        pm.setColor(0.25f, 0.3f, 0.2f, 1f);
        pm.fillRectangle(6, 56, 168, 8);
        pm.setColor(0.7f, 0.85f, 1f, 0.4f);
        pm.fillRectangle(26, 8, 18, 12);
        pm.fillRectangle(50, 8, 16, 12);
        store("chassis_tank", pm);
    }

    private void generateHovercraftChassis() {
        int w = 140, h = 44;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.2f, 0.7f, 0.4f, 1f);
        pm.fillRectangle(6, 8, 128, 24);
        pm.setColor(0.15f, 0.6f, 0.35f, 1f);
        pm.fillRectangle(20, 2, 60, 8);
        pm.setColor(0.7f, 0.85f, 1f, 0.6f);
        pm.fillRectangle(24, 3, 52, 6);
        pm.setColor(0.5f, 0.9f, 0.6f, 0.5f);
        pm.fillRectangle(6, 30, 128, 10);
        pm.setColor(0f, 0.8f, 1f, 0.6f);
        pm.fillRectangle(10, 32, 120, 6);
        pm.setColor(1f, 0.9f, 0.2f, 1f);
        pm.fillRectangle(128, 14, 10, 8);
        store("chassis_hovercraft", pm);
    }

    private void generateWheelTexture() {
        int size = 32;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2, r = size / 2 - 1;

        // Tire (dark)
        pm.setColor(0.2f, 0.2f, 0.2f, 1f);
        pm.fillCircle(cx, cy, r);
        // Rim (silver)
        pm.setColor(0.6f, 0.6f, 0.65f, 1f);
        pm.fillCircle(cx, cy, r - 4);
        // Hub
        pm.setColor(0.4f, 0.4f, 0.45f, 1f);
        pm.fillCircle(cx, cy, 3);
        // Spoke lines
        pm.setColor(0.5f, 0.5f, 0.55f, 1f);
        for (int i = 0; i < 5; i++) {
            float angle = (float) (i * Math.PI * 2.0 / 5.0);
            int ex = cx + (int) (Math.cos(angle) * (r - 5));
            int ey = cy + (int) (Math.sin(angle) * (r - 5));
            pm.drawLine(cx, cy, ex, ey);
        }
        // Tread marks
        pm.setColor(0.25f, 0.25f, 0.25f, 1f);
        for (int i = 0; i < 12; i++) {
            float angle = (float) (i * Math.PI * 2.0 / 12.0);
            int ex = cx + (int) (Math.cos(angle) * r);
            int ey = cy + (int) (Math.sin(angle) * r);
            pm.drawPixel(ex, ey);
        }

        store("wheel", pm);
    }

    // ---- Terrain textures ----

    private void generateTerrainTextures() {
        generateTerrainSurface("terrain_grass", 0.3f, 0.65f, 0.2f, 0.25f, 0.5f, 0.15f);
        generateTerrainSurface("terrain_sand", 0.85f, 0.75f, 0.5f, 0.75f, 0.65f, 0.4f);
        generateTerrainSurface("terrain_ice", 0.7f, 0.85f, 0.95f, 0.6f, 0.75f, 0.85f);
        generateTerrainSurface("terrain_asphalt", 0.35f, 0.35f, 0.38f, 0.3f, 0.3f, 0.32f);
        generateTerrainSurface("terrain_dirt", 0.5f, 0.35f, 0.2f, 0.4f, 0.28f, 0.15f);
        generateTerrainSurface("terrain_lava", 0.45f, 0.3f, 0.25f, 0.35f, 0.22f, 0.18f);
    }

    private void generateTerrainSurface(String name, float r1, float g1, float b1,
                                         float r2, float g2, float b2) {
        int w = 64, h = 64;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        for (int y = 0; y < h; y++) {
            float t = (float) y / h;
            float r = r1 + (r2 - r1) * t;
            float g = g1 + (g2 - g1) * t;
            float b = b1 + (b2 - b1) * t;

            for (int x = 0; x < w; x++) {
                float noise = (float) (Math.sin(x * 0.5 + y * 0.3) * 0.03
                    + Math.sin(x * 1.2 + y * 0.7) * 0.02);
                pm.setColor(
                    Math.max(0, Math.min(1, r + noise)),
                    Math.max(0, Math.min(1, g + noise)),
                    Math.max(0, Math.min(1, b + noise)),
                    1f
                );
                pm.drawPixel(x, y);
            }
        }

        store(name, pm);
    }

    // ---- Game entity textures ----

    private void generateCoinTexture() {
        int size = 24;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2;

        pm.setColor(1f, 0.85f, 0.2f, 1f);
        pm.fillCircle(cx, cy, size / 2 - 1);
        pm.setColor(0.9f, 0.75f, 0.1f, 1f);
        pm.fillCircle(cx, cy, size / 2 - 3);
        pm.setColor(1f, 0.9f, 0.3f, 1f);
        pm.fillCircle(cx - 1, cy - 1, size / 2 - 5);

        store("coin", pm);
    }

    private void generateBoostPadTexture() {
        int w = 48, h = 16;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        pm.setColor(1f, 0.5f, 0f, 0.9f);
        pm.fillRectangle(0, 0, w, h);

        pm.setColor(1f, 0.8f, 0.2f, 1f);
        for (int i = 0; i < 3; i++) {
            int ax = 8 + i * 14;
            pm.fillRectangle(ax + 2, 3, 4, h - 6);
        }

        store("boost_pad", pm);
    }

    // ---- Background textures ----

    private void generateBackgroundTextures() {
        generateSkyGradient("bg_countryside", 0.53f, 0.81f, 0.92f, 0.88f, 0.94f, 1f);
        generateSkyGradient("bg_desert", 0.95f, 0.8f, 0.55f, 1f, 0.92f, 0.7f);
        generateSkyGradient("bg_arctic", 0.7f, 0.82f, 0.9f, 0.9f, 0.95f, 1f);
        generateSkyGradient("bg_neon", 0.05f, 0.02f, 0.15f, 0.1f, 0.05f, 0.25f);
        generateSkyGradient("bg_volcano", 0.3f, 0.1f, 0.05f, 0.5f, 0.2f, 0.1f);
        generateSkyGradient("bg_sky", 0.4f, 0.6f, 0.95f, 0.7f, 0.85f, 1f);

        generateMountainLayer("mountains_countryside", 0.4f, 0.55f, 0.4f, 0.3f, 0.45f, 0.3f);
        generateMountainLayer("mountains_desert", 0.7f, 0.55f, 0.35f, 0.6f, 0.45f, 0.25f);
        generateMountainLayer("mountains_arctic", 0.85f, 0.9f, 0.95f, 0.7f, 0.75f, 0.82f);
        generateMountainLayer("mountains_neon", 0.1f, 0.05f, 0.2f, 0.15f, 0.08f, 0.3f);
        generateMountainLayer("mountains_volcano", 0.25f, 0.12f, 0.08f, 0.35f, 0.15f, 0.05f);
        generateMountainLayer("mountains_sky", 0.8f, 0.88f, 1f, 0.6f, 0.7f, 0.9f);

        generateTreeLayer();
        generateCloudTexture();
    }

    private void generateSkyGradient(String name, float r1, float g1, float b1,
                                      float r2, float g2, float b2) {
        int w = 4, h = 128;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        for (int y = 0; y < h; y++) {
            float t = (float) y / h;
            pm.setColor(
                r1 + (r2 - r1) * t,
                g1 + (g2 - g1) * t,
                b1 + (b2 - b1) * t,
                1f
            );
            pm.drawLine(0, y, w - 1, y);
        }

        store(name, pm);
    }

    private void generateMountainLayer(String name, float r1, float g1, float b1,
                                        float r2, float g2, float b2) {
        int w = 256, h = 96;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        int[] peaks = new int[w];
        for (int x = 0; x < w; x++) {
            float n = (float) (
                Math.sin(x * 0.02) * 30 +
                Math.sin(x * 0.05 + 1.7) * 15 +
                Math.sin(x * 0.11 + 3.2) * 8
            );
            peaks[x] = (int) (h / 2 + n);
            peaks[x] = Math.max(8, Math.min(h - 4, peaks[x]));
        }

        for (int x = 0; x < w; x++) {
            for (int y = peaks[x]; y < h; y++) {
                float t = (float) (y - peaks[x]) / (h - peaks[x]);
                pm.setColor(
                    r1 + (r2 - r1) * t,
                    g1 + (g2 - g1) * t,
                    b1 + (b2 - b1) * t,
                    1f
                );
                pm.drawPixel(x, y);
            }
        }

        store(name, pm);
    }

    private void generateTreeLayer() {
        int w = 256, h = 64;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 12; i++) {
            int tx = (int) (i * 22 + Math.sin(i * 2.3) * 8);
            int th = 20 + (int) (Math.sin(i * 1.7) * 10);
            int tw = 8 + (int) (Math.sin(i * 2.1) * 3);

            pm.setColor(0.35f, 0.25f, 0.15f, 1f);
            pm.fillRectangle(tx + tw / 2 - 1, h - th / 3, 3, th / 3);

            pm.setColor(0.2f + (float) Math.sin(i) * 0.1f,
                0.5f + (float) Math.sin(i * 1.3) * 0.1f,
                0.15f, 1f);
            pm.fillCircle(tx + tw / 2, h - th / 3 - tw, tw);
            pm.fillCircle(tx + tw / 2 - tw / 2, h - th / 3 - tw / 2, tw - 2);
            pm.fillCircle(tx + tw / 2 + tw / 2, h - th / 3 - tw / 2, tw - 2);
        }

        store("trees_layer", pm);
    }

    private void generateCloudTexture() {
        int w = 64, h = 24;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 0.7f);
        pm.fillCircle(20, 14, 10);
        pm.fillCircle(32, 10, 12);
        pm.fillCircle(44, 14, 9);
        pm.setColor(1f, 1f, 1f, 0.5f);
        pm.fillCircle(26, 8, 8);
        pm.fillCircle(38, 12, 10);

        store("cloud", pm);
    }

    // ---- Effect textures ----

    private void generateNitroFlameTexture() {
        int w = 32, h = 16;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        int cy = h / 2;

        for (int x = 0; x < w; x++) {
            float t = (float) x / w;
            float radius = (1f - t) * cy;
            for (int y = 0; y < h; y++) {
                float dy = Math.abs(y - cy);
                if (dy < radius) {
                    float intensity = 1f - dy / radius;
                    pm.setColor(
                        1f,
                        0.5f + 0.5f * intensity * (1f - t),
                        0.1f * intensity,
                        intensity * (1f - t * 0.5f)
                    );
                    pm.drawPixel(x, y);
                }
            }
        }

        store("nitro_flame", pm);
    }

    private void generateParticleTexture() {
        int size = 8;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2;
        float r = size / 2f;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float dist = (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (dist < r) {
                    float alpha = 1f - dist / r;
                    pm.setColor(1f, 1f, 1f, alpha * alpha);
                    pm.drawPixel(x, y);
                }
            }
        }

        store("particle", pm);
    }

    private void generateStarTexture() {
        int size = 20;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2;

        pm.setColor(1f, 0.9f, 0.2f, 1f);
        for (int i = 0; i < 5; i++) {
            float outerAngle = (float) (i * Math.PI * 2 / 5 - Math.PI / 2);
            float innerAngle = (float) ((i + 0.5) * Math.PI * 2 / 5 - Math.PI / 2);

            int ox = cx + (int) (Math.cos(outerAngle) * 9);
            int oy = cy + (int) (Math.sin(outerAngle) * 9);
            int ix = cx + (int) (Math.cos(innerAngle) * 4);
            int iy = cy + (int) (Math.sin(innerAngle) * 4);

            pm.drawLine(cx, cy, ox, oy);
            pm.drawLine(ox, oy, ix, iy);
        }
        pm.fillCircle(cx, cy, 3);

        store("star", pm);
    }

    // ---- Utility ----

    private void store(String name, Pixmap pm) {
        Texture tex = new Texture(pm);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        textures.put(name, tex);
        regions.put(name, new TextureRegion(tex));
        pm.dispose();
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }

    public TextureRegion getRegion(String name) {
        return regions.get(name);
    }

    @Override
    public void dispose() {
        for (Texture tex : textures.values()) {
            tex.dispose();
        }
        textures.clear();
        regions.clear();
    }
}
