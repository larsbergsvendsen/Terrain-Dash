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

/**
 * Creates a complete Scene2D Skin programmatically with no external files.
 * Generates a modern flat UI style with custom colors and button states.
 */
public class SkinFactory {

    private static Skin cachedSkin;
    private static Texture skinTexture;

    public static Skin create() {
        if (cachedSkin != null) return cachedSkin;

        Skin skin = new Skin();

        BitmapFont defaultFont = new BitmapFont();
        defaultFont.getData().setScale(1.5f);
        skin.add("default-font", defaultFont);

        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(3f);
        skin.add("title-font", titleFont);

        BitmapFont smallFont = new BitmapFont();
        smallFont.getData().setScale(1.2f);
        skin.add("small-font", smallFont);

        int atlasSize = 128;
        Pixmap pm = new Pixmap(atlasSize, atlasSize, Pixmap.Format.RGBA8888);

        // White block (0,0,16,16)
        pm.setColor(Color.WHITE);
        pm.fillRectangle(0, 0, 16, 16);

        // Button normal (16,0,16,16) - dark blue
        pm.setColor(0.2f, 0.3f, 0.55f, 1f);
        pm.fillRectangle(16, 0, 16, 16);

        // Button hover (32,0,16,16) - lighter blue
        pm.setColor(0.3f, 0.4f, 0.65f, 1f);
        pm.fillRectangle(32, 0, 16, 16);

        // Button pressed (48,0,16,16) - darker
        pm.setColor(0.15f, 0.2f, 0.4f, 1f);
        pm.fillRectangle(48, 0, 16, 16);

        // Button disabled (64,0,16,16)
        pm.setColor(0.3f, 0.3f, 0.35f, 1f);
        pm.fillRectangle(64, 0, 16, 16);

        // Slider bg (0,16,16,16)
        pm.setColor(0.25f, 0.25f, 0.3f, 1f);
        pm.fillRectangle(0, 16, 16, 16);

        // Slider knob (16,16,16,16)
        pm.setColor(0.5f, 0.7f, 1f, 1f);
        pm.fillRectangle(16, 16, 16, 16);

        // Slider knob before (32,16,16,16)
        pm.setColor(0.3f, 0.5f, 0.9f, 1f);
        pm.fillRectangle(32, 16, 16, 16);

        skinTexture = new Texture(pm);
        pm.dispose();

        TextureRegion white = new TextureRegion(skinTexture, 0, 0, 16, 16);
        TextureRegion btnNormal = new TextureRegion(skinTexture, 16, 0, 16, 16);
        TextureRegion btnHover = new TextureRegion(skinTexture, 32, 0, 16, 16);
        TextureRegion btnPressed = new TextureRegion(skinTexture, 48, 0, 16, 16);
        TextureRegion btnDisabled = new TextureRegion(skinTexture, 64, 0, 16, 16);
        TextureRegion sliderBg = new TextureRegion(skinTexture, 0, 16, 16, 16);
        TextureRegion sliderKnob = new TextureRegion(skinTexture, 16, 16, 16, 16);
        TextureRegion sliderBefore = new TextureRegion(skinTexture, 32, 16, 16, 16);

        // TextButton style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = new TextureRegionDrawable(btnNormal);
        btnStyle.over = new TextureRegionDrawable(btnHover);
        btnStyle.down = new TextureRegionDrawable(btnPressed);
        btnStyle.disabled = new TextureRegionDrawable(btnDisabled);
        btnStyle.font = defaultFont;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);
        skin.add("default", btnStyle);

        // Label styles
        Label.LabelStyle defaultLabel = new Label.LabelStyle(defaultFont, Color.WHITE);
        skin.add("default", defaultLabel);

        Label.LabelStyle titleLabel = new Label.LabelStyle(titleFont, Color.WHITE);
        skin.add("title", titleLabel);

        Label.LabelStyle smallLabel = new Label.LabelStyle(smallFont, new Color(0.8f, 0.8f, 0.8f, 1f));
        skin.add("small", smallLabel);

        // Slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(sliderBg);
        sliderStyle.background.setMinHeight(8);
        sliderStyle.knob = new TextureRegionDrawable(sliderKnob);
        sliderStyle.knob.setMinWidth(20);
        sliderStyle.knob.setMinHeight(20);
        sliderStyle.knobBefore = new TextureRegionDrawable(sliderBefore);
        sliderStyle.knobBefore.setMinHeight(8);
        skin.add("default-horizontal", sliderStyle);

        cachedSkin = skin;
        return skin;
    }

    public static void dispose() {
        if (skinTexture != null) {
            skinTexture.dispose();
            skinTexture = null;
        }
        cachedSkin = null;
    }
}
