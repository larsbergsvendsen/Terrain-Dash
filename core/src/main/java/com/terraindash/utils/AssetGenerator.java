package com.terraindash.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AssetGenerator implements Disposable {

    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, TextureRegion> regions = new HashMap<>();

    public void generateAll() {
        try {
            generateBuggyChassis();
            generateWheelTexture();
            generateCoinTexture();
            generateBoostPadTexture();
            generateParticleTexture();
            generateNitroFlameTexture();
            generateBackgroundTextures();
            generateControlButtons();
            Gdx.app.log("Assets", "Generated " + textures.size() + " textures");
        } catch (Exception e) {
            Gdx.app.error("Assets", "Error: " + e.getMessage(), e);
        }
    }

    private void generateBuggyChassis() {
        int w = 192, h = 72;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // Underbody / skid plate
        setColor(pm, 0.12f, 0.35f, 0.6f);
        fillRoundRect(pm, 10, 42, 172, 22, 6);

        // Main body
        setColor(pm, 0.2f, 0.55f, 0.9f);
        fillRoundRect(pm, 6, 20, 180, 30, 8);

        // Body highlight (top edge)
        setColor(pm, 0.35f, 0.7f, 1f);
        fillRoundRect(pm, 8, 20, 176, 6, 3);

        // Cabin
        setColor(pm, 0.15f, 0.45f, 0.78f);
        fillRoundRect(pm, 30, 4, 80, 20, 6);

        // Windshield
        setColor(pm, 0.6f, 0.82f, 0.95f);
        fillRoundRect(pm, 36, 6, 32, 14, 4);

        // Rear window
        setColor(pm, 0.5f, 0.75f, 0.9f);
        fillRoundRect(pm, 74, 6, 28, 14, 4);

        // Headlight
        setColor(pm, 1f, 0.95f, 0.6f);
        pm.fillCircle(174, 32, 6);
        setColor(pm, 1f, 1f, 0.85f);
        pm.fillCircle(174, 32, 3);

        // Tail light
        setColor(pm, 1f, 0.15f, 0.1f);
        pm.fillCircle(12, 32, 5);
        setColor(pm, 1f, 0.3f, 0.2f);
        pm.fillCircle(12, 32, 3);

        // Bumpers
        setColor(pm, 0.3f, 0.3f, 0.35f);
        fillRoundRect(pm, 2, 36, 8, 14, 3);
        fillRoundRect(pm, 182, 36, 8, 14, 3);

        // Side stripe
        setColor(pm, 1f, 0.7f, 0.1f);
        pm.fillRectangle(20, 35, 150, 3);

        store("chassis_buggy", pm);
    }

    private void generateWheelTexture() {
        int size = 48;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2, r = size / 2 - 2;

        // Outer tire with tread
        setColor(pm, 0.18f, 0.18f, 0.2f);
        pm.fillCircle(cx, cy, r);

        // Tread pattern
        setColor(pm, 0.25f, 0.25f, 0.27f);
        for (int i = 0; i < 16; i++) {
            float angle = (float) (i * Math.PI * 2 / 16);
            int x1 = cx + (int) (Math.cos(angle) * (r - 3));
            int y1 = cy + (int) (Math.sin(angle) * (r - 3));
            int x2 = cx + (int) (Math.cos(angle) * r);
            int y2 = cy + (int) (Math.sin(angle) * r);
            pm.drawLine(x1, y1, x2, y2);
        }

        // Inner tire wall
        setColor(pm, 0.22f, 0.22f, 0.24f);
        pm.fillCircle(cx, cy, r - 5);

        // Rim
        setColor(pm, 0.7f, 0.72f, 0.75f);
        pm.fillCircle(cx, cy, r - 8);

        // Rim detail
        setColor(pm, 0.55f, 0.57f, 0.6f);
        pm.fillCircle(cx, cy, r - 11);

        // Spokes
        setColor(pm, 0.75f, 0.77f, 0.8f);
        for (int i = 0; i < 5; i++) {
            float angle = (float) (i * Math.PI * 2 / 5);
            int ex = cx + (int) (Math.cos(angle) * (r - 9));
            int ey = cy + (int) (Math.sin(angle) * (r - 9));
            pm.drawLine(cx, cy, ex, ey);
            pm.drawLine(cx + 1, cy, ex + 1, ey);
        }

        // Hub cap
        setColor(pm, 0.8f, 0.82f, 0.85f);
        pm.fillCircle(cx, cy, 4);
        setColor(pm, 0.6f, 0.62f, 0.65f);
        pm.fillCircle(cx, cy, 2);

        store("wheel", pm);
    }

    private void generateCoinTexture() {
        int size = 36;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2;

        // Shadow
        setColor(pm, 0.6f, 0.5f, 0.1f);
        pm.fillCircle(cx + 1, cy + 1, size / 2 - 2);

        // Gold outer
        setColor(pm, 1f, 0.82f, 0.15f);
        pm.fillCircle(cx, cy, size / 2 - 2);

        // Gold inner
        setColor(pm, 0.95f, 0.75f, 0.1f);
        pm.fillCircle(cx, cy, size / 2 - 5);

        // Highlight
        setColor(pm, 1f, 0.95f, 0.5f);
        pm.fillCircle(cx - 3, cy - 3, size / 2 - 9);

        // $ symbol lines
        setColor(pm, 0.75f, 0.6f, 0.05f);
        pm.fillRectangle(cx - 1, cy - 6, 3, 13);
        pm.fillRectangle(cx - 5, cy - 4, 11, 3);
        pm.fillRectangle(cx - 5, cy + 2, 11, 3);

        store("coin", pm);
    }

    private void generateBoostPadTexture() {
        int w = 64, h = 24;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // Base
        setColor(pm, 1f, 0.45f, 0f);
        fillRoundRect(pm, 0, 0, w, h, 6);

        // Inner glow
        setColor(pm, 1f, 0.65f, 0.1f);
        fillRoundRect(pm, 3, 3, w - 6, h - 6, 4);

        // Arrow shapes (using rectangles)
        setColor(pm, 1f, 0.9f, 0.3f);
        for (int i = 0; i < 3; i++) {
            int ax = 10 + i * 18;
            pm.fillRectangle(ax, h / 2 - 4, 3, 8);
            pm.fillRectangle(ax + 3, h / 2 - 2, 3, 4);
        }

        store("boost_pad", pm);
    }

    private void generateParticleTexture() {
        int size = 16;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int cx = size / 2, cy = size / 2;
        float r = size / 2f;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float dist = (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (dist < r) {
                    float alpha = (1f - dist / r);
                    alpha = alpha * alpha;
                    pm.setColor(1f, 1f, 1f, alpha);
                    pm.drawPixel(x, y);
                }
            }
        }

        store("particle", pm);
    }

    private void generateNitroFlameTexture() {
        int w = 48, h = 24;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        int cy = h / 2;

        for (int x = 0; x < w; x++) {
            float t = (float) x / w;
            float radius = (1f - t) * cy;
            for (int y = 0; y < h; y++) {
                float dy = Math.abs(y - cy);
                if (dy < radius) {
                    float intensity = 1f - dy / radius;
                    float r = 1f;
                    float g = 0.4f + 0.6f * intensity * (1f - t);
                    float b = 0.1f * intensity * (1f - t);
                    float a = intensity * (1f - t * 0.7f);
                    pm.setColor(r, g, b, a);
                    pm.drawPixel(x, y);
                }
            }
        }

        store("nitro_flame", pm);
    }

    private void generateBackgroundTextures() {
        generateSkyGradient("bg_countryside", 0.45f, 0.72f, 0.92f, 0.82f, 0.92f, 1f);
        generateSkyGradient("bg_desert", 0.9f, 0.75f, 0.5f, 1f, 0.9f, 0.65f);
        generateSkyGradient("bg_arctic", 0.65f, 0.78f, 0.88f, 0.88f, 0.93f, 1f);
        generateSkyGradient("bg_neon", 0.04f, 0.01f, 0.12f, 0.08f, 0.04f, 0.22f);
        generateSkyGradient("bg_volcano", 0.25f, 0.08f, 0.04f, 0.45f, 0.18f, 0.08f);
        generateSkyGradient("bg_sky", 0.35f, 0.55f, 0.92f, 0.65f, 0.82f, 1f);

        generateMountainLayer("mountains_countryside", 0.35f, 0.5f, 0.38f, 0.28f, 0.42f, 0.28f);
        generateMountainLayer("mountains_desert", 0.65f, 0.5f, 0.32f, 0.55f, 0.42f, 0.22f);
        generateMountainLayer("mountains_arctic", 0.82f, 0.88f, 0.92f, 0.68f, 0.72f, 0.8f);
        generateMountainLayer("mountains_neon", 0.08f, 0.04f, 0.18f, 0.12f, 0.06f, 0.28f);
        generateMountainLayer("mountains_volcano", 0.22f, 0.1f, 0.06f, 0.32f, 0.13f, 0.04f);
        generateMountainLayer("mountains_sky", 0.75f, 0.85f, 0.98f, 0.55f, 0.68f, 0.88f);

        generateTreeLayer();
        generateCloudTexture();
    }

    private void generateSkyGradient(String name, float r1, float g1, float b1,
                                      float r2, float g2, float b2) {
        int w = 4, h = 128;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        for (int y = 0; y < h; y++) {
            float t = (float) y / h;
            pm.setColor(r1 + (r2 - r1) * t, g1 + (g2 - g1) * t, b1 + (b2 - b1) * t, 1f);
            pm.drawLine(0, y, w - 1, y);
        }
        store(name, pm);
    }

    private void generateMountainLayer(String name, float r1, float g1, float b1,
                                        float r2, float g2, float b2) {
        int w = 256, h = 96;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        for (int x = 0; x < w; x++) {
            float n = (float) (Math.sin(x * 0.02) * 28 + Math.sin(x * 0.055 + 1.7) * 14
                + Math.sin(x * 0.12 + 3.2) * 7);
            int peak = Math.max(8, Math.min(h - 4, (int) (h / 2 + n)));
            for (int y = peak; y < h; y++) {
                float t = (float) (y - peak) / (h - peak);
                pm.setColor(r1 + (r2 - r1) * t, g1 + (g2 - g1) * t, b1 + (b2 - b1) * t, 1f);
                pm.drawPixel(x, y);
            }
        }
        store(name, pm);
    }

    private void generateTreeLayer() {
        int w = 256, h = 64;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        for (int i = 0; i < 10; i++) {
            int tx = Math.min(w - 20, (int) (i * 25 + Math.sin(i * 2.3) * 6));
            int tw = 7 + (int) (Math.sin(i * 2.1) * 2);

            setColor(pm, 0.3f, 0.22f, 0.12f);
            pm.fillRectangle(tx + tw / 2 - 1, h - 12, 3, 12);

            float g = 0.45f + (float) Math.sin(i * 1.3) * 0.1f;
            setColor(pm, 0.18f, g, 0.12f);
            pm.fillCircle(tx + tw / 2, h - 14 - tw, tw);
            pm.fillCircle(tx + tw / 2 - 3, h - 12 - tw / 2, tw - 1);
            pm.fillCircle(tx + tw / 2 + 3, h - 12 - tw / 2, tw - 1);
        }
        store("trees_layer", pm);
    }

    private void generateCloudTexture() {
        int w = 80, h = 32;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        setColor(pm, 1f, 1f, 1f, 0.55f);
        pm.fillCircle(24, 18, 12);
        pm.fillCircle(40, 13, 14);
        pm.fillCircle(56, 18, 11);
        setColor(pm, 1f, 1f, 1f, 0.35f);
        pm.fillCircle(32, 11, 10);
        pm.fillCircle(48, 15, 12);
        store("cloud", pm);
    }

    private void generateControlButtons() {
        int s = 160;
        int cx = s / 2, cy = s / 2, r = s / 2 - 4;

        // Gas pedal - smooth gradient circle
        Pixmap pm = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        drawSoftCircle(pm, cx, cy, r, 0.1f, 0.7f, 0.2f, 0.75f);
        drawSoftCircle(pm, cx, cy, r - 12, 0.15f, 0.85f, 0.25f, 0.85f);
        // Up arrow
        setColor(pm, 1f, 1f, 1f, 0.95f);
        pm.fillRectangle(cx - 4, cy - 22, 8, 44);
        pm.fillRectangle(cx - 16, cy - 14, 32, 8);
        store("btn_gas", pm);

        // Tilt left
        pm = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        drawSoftCircle(pm, cx, cy, r, 0.7f, 0.45f, 0.1f, 0.65f);
        drawSoftCircle(pm, cx, cy, r - 12, 0.85f, 0.55f, 0.15f, 0.8f);
        setColor(pm, 1f, 1f, 1f, 0.95f);
        pm.fillRectangle(cx - 22, cy - 4, 44, 8);
        pm.fillRectangle(cx - 14, cy - 16, 8, 32);
        store("btn_tilt_left", pm);

        // Tilt right
        pm = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        drawSoftCircle(pm, cx, cy, r, 0.7f, 0.45f, 0.1f, 0.65f);
        drawSoftCircle(pm, cx, cy, r - 12, 0.85f, 0.55f, 0.15f, 0.8f);
        setColor(pm, 1f, 1f, 1f, 0.95f);
        pm.fillRectangle(cx - 22, cy - 4, 44, 8);
        pm.fillRectangle(cx + 6, cy - 16, 8, 32);
        store("btn_tilt_right", pm);
    }

    private void drawSoftCircle(Pixmap pm, int cx, int cy, int radius,
                                 float r, float g, float b, float maxAlpha) {
        for (int y = cy - radius; y <= cy + radius; y++) {
            for (int x = cx - radius; x <= cx + radius; x++) {
                if (x < 0 || y < 0 || x >= pm.getWidth() || y >= pm.getHeight()) continue;
                float dist = (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (dist <= radius) {
                    float edge = Math.max(0, 1f - (dist / radius));
                    float alpha = edge * maxAlpha;
                    pm.setColor(r, g, b, alpha);
                    Pixmap.Blending old = pm.getBlending();
                    pm.setBlending(Pixmap.Blending.SourceOver);
                    pm.drawPixel(x, y);
                    pm.setBlending(old);
                }
            }
        }
    }

    // ---- Helpers ----

    private void setColor(Pixmap pm, float r, float g, float b) {
        pm.setColor(r, g, b, 1f);
    }

    private void setColor(Pixmap pm, float r, float g, float b, float a) {
        pm.setColor(r, g, b, a);
    }

    private void fillRoundRect(Pixmap pm, int x, int y, int w, int h, int radius) {
        pm.fillRectangle(x + radius, y, w - radius * 2, h);
        pm.fillRectangle(x, y + radius, w, h - radius * 2);
        pm.fillCircle(x + radius, y + radius, radius);
        pm.fillCircle(x + w - radius - 1, y + radius, radius);
        pm.fillCircle(x + radius, y + h - radius - 1, radius);
        pm.fillCircle(x + w - radius - 1, y + h - radius - 1, radius);
    }

    private void store(String name, Pixmap pm) {
        Texture tex = new Texture(pm);
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        textures.put(name, tex);
        regions.put(name, new TextureRegion(tex));
        pm.dispose();
    }

    public Texture getTexture(String name) { return textures.get(name); }
    public TextureRegion getRegion(String name) { return regions.get(name); }

    @Override
    public void dispose() {
        for (Texture tex : textures.values()) tex.dispose();
        textures.clear();
        regions.clear();
    }
}
