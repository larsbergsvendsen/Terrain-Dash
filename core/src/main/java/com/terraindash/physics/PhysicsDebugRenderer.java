package com.terraindash.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

/**
 * Wrapper around Box2DDebugRenderer for toggling physics visualization
 * during development. Shows collision shapes, joints, and velocities.
 */
public class PhysicsDebugRenderer implements Disposable {

    private Box2DDebugRenderer renderer;
    private boolean enabled = false;

    public PhysicsDebugRenderer() {
        renderer = new Box2DDebugRenderer(
            true,   // shapes
            true,   // joints
            false,  // aabbs
            false,  // inactive
            false,  // velocities
            true    // contacts
        );
    }

    public void render(World world, OrthographicCamera camera) {
        if (!enabled || world == null) return;
        renderer.render(world, camera.combined);
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void dispose() {
        if (renderer != null) renderer.dispose();
    }
}
