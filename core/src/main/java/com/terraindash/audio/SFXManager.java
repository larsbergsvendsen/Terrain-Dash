package com.terraindash.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages sound effects with volume control and pitch variation.
 */
public class SFXManager implements Disposable {

    private final Map<String, Sound> sounds = new HashMap<>();
    private float volume = 1.0f;

    public void load(String id, String path) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(id, sound);
    }

    public long play(String id) {
        return play(id, 1f);
    }

    public long play(String id, float pitch) {
        Sound sound = sounds.get(id);
        if (sound == null) return -1;
        return sound.play(volume, pitch, 0f);
    }

    public long playWithVariation(String id) {
        float pitch = 0.9f + (float) Math.random() * 0.2f;
        return play(id, pitch);
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
}
