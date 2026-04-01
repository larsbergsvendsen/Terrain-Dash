package com.terraindash.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Manages particle effects: dust trails, nitro flames, sparks on landing,
 * and coin collection bursts.
 */
public class ParticleManager implements Disposable {

    public enum EffectType {
        DUST_TRAIL,
        NITRO_FLAME,
        LANDING_SPARK,
        COIN_BURST,
        EXPLOSION
    }

    public void spawn(EffectType type, Vector2 position) {
        spawn(type, position, 0f);
    }

    public void spawn(EffectType type, Vector2 position, float angle) {
        // TODO: Create particle effect at position
        // Use LibGDX ParticleEffect or custom particle system
    }

    public void update(float delta) {
        // TODO: Update all active particle effects
    }

    public void render(SpriteBatch batch) {
        // TODO: Render all active particle effects
    }

    @Override
    public void dispose() {
        // Dispose particle textures and effects
    }
}
