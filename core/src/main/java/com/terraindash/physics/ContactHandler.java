package com.terraindash.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.terraindash.world.GameWorld;

/**
 * Handles Box2D collision events between game entities.
 * Delegates effects (coin collection, boost activation, etc.) to GameWorld.
 */
public class ContactHandler implements ContactListener {

    private final GameWorld gameWorld;

    public ContactHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object dataA = fixtureA.getBody().getUserData();
        Object dataB = fixtureB.getBody().getUserData();

        if (dataA == null || dataB == null) return;

        String typeA = getEntityType(dataA);
        String typeB = getEntityType(dataB);

        handleCollision(typeA, typeB, dataA, dataB);
    }

    private void handleCollision(String typeA, String typeB, Object dataA, Object dataB) {
        if (isVehicleVs(typeA, typeB, "coin")) {
            gameWorld.collectCoin();
        } else if (isVehicleVs(typeA, typeB, "boost")) {
            gameWorld.addNitro(3f);
        } else if (isVehicleVs(typeA, typeB, "finish")) {
            gameWorld.setLevelComplete(true);
        }
    }

    private boolean isVehicleVs(String typeA, String typeB, String other) {
        return ("vehicle".equals(typeA) && other.equals(typeB))
            || ("vehicle".equals(typeB) && other.equals(typeA));
    }

    private String getEntityType(Object userData) {
        if (userData instanceof String) return (String) userData;
        return "unknown";
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
