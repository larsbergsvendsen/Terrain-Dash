package com.terraindash.effects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.terraindash.utils.Constants;

/**
 * Controls the game camera with smooth following, look-ahead,
 * dynamic zoom based on speed, and screen shake effects.
 */
public class CameraController {

    private final OrthographicCamera camera;

    private float shakeAmplitude = 0f;
    private float shakeDuration = 0f;
    private float shakeTimer = 0f;

    public CameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update(float delta, Vector2 vehiclePosition, float vehicleSpeed) {
        float targetX = vehiclePosition.x + Constants.CAMERA_LOOK_AHEAD;
        float targetY = vehiclePosition.y + 2f;

        camera.position.x += (targetX - camera.position.x) * Constants.CAMERA_LERP;
        camera.position.y += (targetY - camera.position.y) * Constants.CAMERA_LERP;

        float speedZoom = 1f + vehicleSpeed * Constants.CAMERA_SPEED_ZOOM_FACTOR;
        float targetZoom = MathUtils.clamp(speedZoom, Constants.CAMERA_MIN_ZOOM, Constants.CAMERA_MAX_ZOOM);
        camera.zoom += (targetZoom - camera.zoom) * 0.05f;

        if (shakeTimer > 0) {
            shakeTimer -= delta;
            float intensity = shakeAmplitude * (shakeTimer / shakeDuration);
            camera.position.x += MathUtils.random(-intensity, intensity);
            camera.position.y += MathUtils.random(-intensity, intensity);

            if (shakeTimer <= 0) {
                shakeTimer = 0;
                shakeAmplitude = 0;
            }
        }

        camera.update();
    }

    public void shake(float amplitude, float duration) {
        this.shakeAmplitude = amplitude;
        this.shakeDuration = duration;
        this.shakeTimer = duration;
    }
}
