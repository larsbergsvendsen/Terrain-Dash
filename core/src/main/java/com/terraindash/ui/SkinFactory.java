package com.terraindash.ui;

import com.badlogic.gdx.Gdx;
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

public class SkinFactory {

    private static Skin cachedSkin;
    private static Texture skinTexture;

    public static Skin create() {
        if (cachedSkin != null) return cachedSkin;

        float density = Math.max(1f, Gdx.graphics.getDensity());
        float fontScale = density * 1.2f;

        Skin skin = new Skin();

        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(fontScale);
        skin.add("default-font", defaultFont);

        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(fontScale * 2.2f);
        skin.add("title-font", titleFont);

        BitmapFont smallFont = new BitmapFont();
        smallFont.getData().setScale(fontScale * 0.85f);
        skin.add("small-font", smallFont);

        Pixmap pm = new Pixmap(128, 64, Pixmap.Format.RGBA8888);

        // Rounded button normal - gradient blue
        for (int y = 0; y < 32; y++) {
            float t = (float) y / 32;
            float r = 0.15f + t * 0.08f;
            float g = 0.28f + t * 0.06f;
            float b = 0.55f - t * 0.1f;
            pm.setColor(r, g, b, 1f);
            pm.drawLine(0, y, 31, y);
        }

        // Button pressed - darker
        for (int y = 0; y < 32; y++) {
            float t = (float) y / 32;
            pm.setColor(0.08f + t * 0.05f, 0.15f + t * 0.04f, 0.35f - t * 0.06f, 1f);
            pm.drawLine(32, y, 63, y);
        }

        // Button disabled
        for (int y = 0; y < 32; y++) {
            pm.setColor(0.22f, 0.22f, 0.26f, 1f);
            pm.drawLine(64, y, 95, y);
        }

        // Slider bg
        pm.setColor(0.18f, 0.18f, 0.24f, 1f);
        pm.fillRectangle(0, 32, 32, 32);

        // Slider knob
        pm.setColor(0.4f, 0.6f, 1f, 1f);
        pm.fillRectangle(32, 32, 32, 32);

        // Slider filled
        pm.setColor(0.25f, 0.45f, 0.85f, 1f);
        pm.fillRectangle(64, 32, 32, 32);

        skinTexture = new Texture(pm);
        skinTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pm.dispose();

        TextureRegionDrawable btnUp = new TextureRegionDrawable(new TextureRegion(skinTexture, 0, 0, 32, 32));
        TextureRegionDrawable btnDown = new TextureRegionDrawable(new TextureRegion(skinTexture, 32, 0, 32, 32));
        TextureRegionDrawable btnDisabled = new TextureRegionDrawable(new TextureRegion(skinTexture, 64, 0, 32, 32));
        TextureRegionDrawable sliderBg = new TextureRegionDrawable(new TextureRegion(skinTexture, 0, 32, 32, 32));
        TextureRegionDrawable sliderKnob = new TextureRegionDrawable(new TextureRegion(skinTexture, 32, 32, 32, 32));
        TextureRegionDrawable sliderFilled = new TextureRegionDrawable(new TextureRegion(skinTexture, 64, 32, 32, 32));

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = btnUp;
        btnStyle.over = btnUp;
        btnStyle.down = btnDown;
        btnStyle.disabled = btnDisabled;
        btnStyle.font = defaultFont;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.downFontColor = new Color(0.8f, 0.85f, 1f, 1f);
        btnStyle.disabledFontColor = new Color(0.5f, 0.5f, 0.5f, 1f);
        skin.add("default", btnStyle);

        skin.add("default", new Label.LabelStyle(defaultFont, Color.WHITE));
        skin.add("title", new Label.LabelStyle(titleFont, Color.WHITE));
        skin.add("small", new Label.LabelStyle(smallFont, new Color(0.75f, 0.75f, 0.82f, 1f)));

        float knobSize = 28 * density;
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBg;
        sliderStyle.background.setMinHeight(8 * density);
        sliderStyle.knob = sliderKnob;
        sliderStyle.knob.setMinWidth(knobSize);
        sliderStyle.knob.setMinHeight(knobSize);
        sliderStyle.knobBefore = sliderFilled;
        sliderStyle.knobBefore.setMinHeight(8 * density);
        skin.add("default-horizontal", sliderStyle);

        cachedSkin = skin;
        return skin;
    }

    public static void dispose() {
        if (skinTexture != null) { skinTexture.dispose(); skinTexture = null; }
        cachedSkin = null;
    }
}
