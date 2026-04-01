package com.terraindash.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Standalone screen shake utility that can be used by the CameraController
 * or UI elements for juicy impact feedback.
 */
public class ScreenShake {

    private float trauma = 0f;
    private float maxOffset = 0.5f;
    private float maxAngle = 3f;

    private final Vector2 offset = new Vector2();
    private float angleOffset = 0f;

    public void addTrauma(float amount) {
        trauma = Math.min(trauma + amount, 1f);
    }

    public void update(float delta) {
        if (trauma <= 0) {
            offset.set(0, 0);
            angleOffset = 0;
            return;
        }

        float shake = trauma * trauma;

        offset.x = maxOffset * shake * MathUtils.random(-1f, 1f);
        offset.y = maxOffset * shake * MathUtils.random(-1f, 1f);
        angleOffset = maxAngle * shake * MathUtils.random(-1f, 1f);

        // Decay trauma over time
        trauma = Math.max(trauma - delta * 1.5f, 0f);
    }

    public Vector2 getOffset() {
        return offset;
    }

    public float getAngleOffset() {
        return angleOffset;
    }

    public float getTrauma() {
        return trauma;
    }
}
