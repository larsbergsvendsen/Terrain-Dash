package com.terraindash.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * All UI uses a virtual 800x480 coordinate system (ExtendViewport).
 * Sizes here are tuned for that resolution.
 */
public class SkinFactory {

    private static Skin cachedSkin;
    private static Texture skinTexture;

    public static Skin create() {
        if (cachedSkin != null) return cachedSkin;

        Skin skin = new Skin();

        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(1.6f);
        skin.add("default-font", defaultFont);

        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(3.2f);
        skin.add("title-font", titleFont);

        BitmapFont smallFont = new BitmapFont();
        smallFont.getData().setScale(1.2f);
        skin.add("small-font", smallFont);

        int w = 128, h = 64;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        // Button up: blue gradient
        for (int y = 0; y < 32; y++) {
            float t = (float) y / 32;
            pm.setColor(0.12f + t * 0.06f, 0.22f + t * 0.08f, 0.48f + t * 0.08f, 0.92f);
            pm.drawLine(0, y, 31, y);
        }
        // Button down: pressed
        for (int y = 0; y < 32; y++) {
            float t = (float) y / 32;
            pm.setColor(0.08f + t * 0.04f, 0.14f + t * 0.05f, 0.32f + t * 0.05f, 0.95f);
            pm.drawLine(32, y, 63, y);
        }
        // Button disabled
        for (int y = 0; y < 32; y++) {
            pm.setColor(0.18f, 0.18f, 0.22f, 0.7f);
            pm.drawLine(64, y, 95, y);
        }
        // Slider bg
        pm.setColor(0.15f, 0.15f, 0.2f, 0.8f);
        pm.fillRectangle(0, 32, 32, 32);
        // Slider knob
        pm.setColor(0.35f, 0.55f, 0.95f, 1f);
        pm.fillRectangle(32, 32, 32, 32);
        // Slider before
        pm.setColor(0.22f, 0.4f, 0.82f, 1f);
        pm.fillRectangle(64, 32, 32, 32);

        skinTexture = new Texture(pm);
        skinTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pm.dispose();

        TextureRegionDrawable btnUp = d(skinTexture, 0, 0, 32, 32);
        TextureRegionDrawable btnDown = d(skinTexture, 32, 0, 32, 32);
        TextureRegionDrawable btnDisabled = d(skinTexture, 64, 0, 32, 32);

        TextButton.TextButtonStyle btn = new TextButton.TextButtonStyle();
        btn.up = btnUp; btn.over = btnUp; btn.down = btnDown; btn.disabled = btnDisabled;
        btn.font = defaultFont;
        btn.fontColor = Color.WHITE;
        btn.downFontColor = new Color(0.7f, 0.8f, 1f, 1f);
        btn.disabledFontColor = new Color(0.45f, 0.45f, 0.5f, 1f);
        skin.add("default", btn);

        skin.add("default", new Label.LabelStyle(defaultFont, Color.WHITE));
        skin.add("title", new Label.LabelStyle(titleFont, Color.WHITE));
        skin.add("small", new Label.LabelStyle(smallFont, new Color(0.7f, 0.7f, 0.78f, 1f)));

        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = d(skinTexture, 0, 32, 32, 32); ss.background.setMinHeight(10);
        ss.knob = d(skinTexture, 32, 32, 32, 32); ss.knob.setMinWidth(24); ss.knob.setMinHeight(24);
        ss.knobBefore = d(skinTexture, 64, 32, 32, 32); ss.knobBefore.setMinHeight(10);
        skin.add("default-horizontal", ss);

        cachedSkin = skin;
        return skin;
    }

    private static TextureRegionDrawable d(Texture t, int x, int y, int w, int h) {
        return new TextureRegionDrawable(new TextureRegion(t, x, y, w, h));
    }

    public static void dispose() {
        if (skinTexture != null) { skinTexture.dispose(); skinTexture = null; }
        cachedSkin = null;
    }
}
