package com.terraindash.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Lightweight custom particle system using a single soft-circle texture.
 * Handles dust trails, nitro flames, landing sparks, and coin bursts.
 */
public class ParticleManager implements Disposable {

    public enum EffectType {
        DUST_TRAIL,
        NITRO_FLAME,
        LANDING_SPARK,
        COIN_BURST,
        EXPLOSION
    }

    private static class Particle {
        float x, y;
        float vx, vy;
        float life, maxLife;
        float size, startSize, endSize;
        Color color;
        float alpha;
        float gravity;
        float drag;
    }

    private final List<Particle> particles = new ArrayList<>();
    private TextureRegion particleTexture;
    private TextureRegion flameTexture;

    private static final int MAX_PARTICLES = 300;

    public void setTextures(TextureRegion particleTex, TextureRegion flameTex) {
        this.particleTexture = particleTex;
        this.flameTexture = flameTex;
    }

    public void spawn(EffectType type, Vector2 position) {
        spawn(type, position, 0f, 0f);
    }

    public void spawn(EffectType type, Vector2 position, float angle) {
        spawn(type, position, angle, 0f);
    }

    public void spawn(EffectType type, Vector2 position, float angle, float speed) {
        switch (type) {
            case DUST_TRAIL:
                spawnDust(position, speed);
                break;
            case NITRO_FLAME:
                spawnNitroFlame(position, angle);
                break;
            case LANDING_SPARK:
                spawnSparks(position, speed);
                break;
            case COIN_BURST:
                spawnCoinBurst(position);
                break;
            case EXPLOSION:
                spawnExplosion(position);
                break;
        }
    }

    private void spawnDust(Vector2 pos, float speed) {
        int count = Math.min(2, MAX_PARTICLES - particles.size());
        float intensity = MathUtils.clamp(speed / 20f, 0.2f, 1f);

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = pos.x + MathUtils.random(-0.3f, 0.3f);
            p.y = pos.y + MathUtils.random(-0.1f, 0.1f);
            p.vx = MathUtils.random(-1f, -0.2f) - speed * 0.05f;
            p.vy = MathUtils.random(0.3f, 1.2f);
            p.maxLife = MathUtils.random(0.4f, 0.8f);
            p.life = p.maxLife;
            p.startSize = 0.15f + intensity * 0.15f;
            p.endSize = 0.4f + intensity * 0.3f;
            p.size = p.startSize;
            p.color = new Color(0.7f, 0.65f, 0.55f, 0.6f * intensity);
            p.alpha = p.color.a;
            p.gravity = -0.3f;
            p.drag = 0.98f;
            particles.add(p);
        }
    }

    private void spawnNitroFlame(Vector2 pos, float angle) {
        int count = Math.min(3, MAX_PARTICLES - particles.size());

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = pos.x;
            p.y = pos.y + MathUtils.random(-0.15f, 0.15f);
            float spread = MathUtils.random(-0.3f, 0.3f);
            p.vx = -(float) Math.cos(angle) * MathUtils.random(3f, 6f) + spread;
            p.vy = -(float) Math.sin(angle) * MathUtils.random(3f, 6f) + spread;
            p.maxLife = MathUtils.random(0.15f, 0.3f);
            p.life = p.maxLife;
            p.startSize = MathUtils.random(0.2f, 0.4f);
            p.endSize = 0.05f;
            p.size = p.startSize;
            float rnd = MathUtils.random();
            if (rnd < 0.3f) {
                p.color = new Color(1f, 0.95f, 0.7f, 0.9f);
            } else if (rnd < 0.7f) {
                p.color = new Color(1f, 0.6f, 0.1f, 0.8f);
            } else {
                p.color = new Color(1f, 0.2f, 0.05f, 0.7f);
            }
            p.alpha = p.color.a;
            p.gravity = 0f;
            p.drag = 0.95f;
            particles.add(p);
        }
    }

    private void spawnSparks(Vector2 pos, float speed) {
        int count = Math.min(8 + (int) (speed * 0.5f), MAX_PARTICLES - particles.size());

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = pos.x + MathUtils.random(-0.5f, 0.5f);
            p.y = pos.y;
            p.vx = MathUtils.random(-3f, 3f);
            p.vy = MathUtils.random(1f, 5f);
            p.maxLife = MathUtils.random(0.2f, 0.5f);
            p.life = p.maxLife;
            p.startSize = MathUtils.random(0.05f, 0.12f);
            p.endSize = 0.02f;
            p.size = p.startSize;
            p.color = new Color(1f, 0.9f, 0.3f, 1f);
            p.alpha = 1f;
            p.gravity = -8f;
            p.drag = 0.99f;
            particles.add(p);
        }
    }

    private void spawnCoinBurst(Vector2 pos) {
        int count = Math.min(12, MAX_PARTICLES - particles.size());

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = pos.x;
            p.y = pos.y;
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float spd = MathUtils.random(1.5f, 4f);
            p.vx = (float) Math.cos(angle) * spd;
            p.vy = (float) Math.sin(angle) * spd;
            p.maxLife = MathUtils.random(0.3f, 0.6f);
            p.life = p.maxLife;
            p.startSize = MathUtils.random(0.1f, 0.2f);
            p.endSize = 0.02f;
            p.size = p.startSize;
            p.color = new Color(1f, 0.85f, 0.2f, 1f);
            p.alpha = 1f;
            p.gravity = -3f;
            p.drag = 0.96f;
            particles.add(p);
        }
    }

    private void spawnExplosion(Vector2 pos) {
        int count = Math.min(20, MAX_PARTICLES - particles.size());

        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = pos.x + MathUtils.random(-0.3f, 0.3f);
            p.y = pos.y + MathUtils.random(-0.3f, 0.3f);
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float spd = MathUtils.random(2f, 7f);
            p.vx = (float) Math.cos(angle) * spd;
            p.vy = (float) Math.sin(angle) * spd;
            p.maxLife = MathUtils.random(0.3f, 0.8f);
            p.life = p.maxLife;
            p.startSize = MathUtils.random(0.15f, 0.5f);
            p.endSize = 0.6f;
            p.size = p.startSize;
            float rnd = MathUtils.random();
            if (rnd < 0.4f) {
                p.color = new Color(1f, 0.6f, 0.1f, 0.9f);
            } else if (rnd < 0.7f) {
                p.color = new Color(0.4f, 0.4f, 0.4f, 0.7f);
            } else {
                p.color = new Color(1f, 0.2f, 0.05f, 0.8f);
            }
            p.alpha = p.color.a;
            p.gravity = -2f;
            p.drag = 0.94f;
            particles.add(p);
        }
    }

    public void update(float delta) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.life -= delta;
            if (p.life <= 0) {
                it.remove();
                continue;
            }

            float t = 1f - p.life / p.maxLife;
            p.vx *= p.drag;
            p.vy *= p.drag;
            p.vy += p.gravity * delta;
            p.x += p.vx * delta;
            p.y += p.vy * delta;
            p.size = MathUtils.lerp(p.startSize, p.endSize, t);
            p.color.a = p.alpha * (1f - t * t);
        }
    }

    public void render(SpriteBatch batch) {
        if (particleTexture == null) return;

        for (Particle p : particles) {
            batch.setColor(p.color);
            batch.draw(particleTexture,
                p.x - p.size / 2f, p.y - p.size / 2f,
                p.size, p.size);
        }
        batch.setColor(Color.WHITE);
    }

    public int getParticleCount() {
        return particles.size();
    }

    public void clear() {
        particles.clear();
    }

    @Override
    public void dispose() {
        particles.clear();
    }
}
