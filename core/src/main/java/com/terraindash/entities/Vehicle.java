package com.terraindash.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;

/**
 * Represents the player's vehicle composed of a chassis body
 * and two wheel bodies connected via WheelJoints.
 */
public class Vehicle {

    private Body chassis;
    private Body wheelFront;
    private Body wheelRear;
    private WheelJoint jointFront;
    private WheelJoint jointRear;
    private VehicleConfig config;

    private TextureRegion chassisTexture;
    private TextureRegion wheelTexture;

    public Vehicle(Body chassis, Body wheelFront, Body wheelRear,
                   WheelJoint jointFront, WheelJoint jointRear,
                   VehicleConfig config) {
        this.chassis = chassis;
        this.wheelFront = wheelFront;
        this.wheelRear = wheelRear;
        this.jointFront = jointFront;
        this.jointRear = jointRear;
        this.config = config;
    }

    public void setTextures(TextureRegion chassisTexture, TextureRegion wheelTexture) {
        this.chassisTexture = chassisTexture;
        this.wheelTexture = wheelTexture;
    }

    public void render(SpriteBatch batch) {
        renderWheel(batch, wheelRear);
        renderWheel(batch, wheelFront);
        renderChassis(batch);
    }

    private void renderChassis(SpriteBatch batch) {
        if (chassisTexture == null) return;

        Vector2 pos = chassis.getPosition();
        float angle = chassis.getAngle() * MathUtils.radiansToDegrees;
        float w = config.chassisWidth;
        float h = config.chassisHeight;

        batch.draw(chassisTexture,
            pos.x - w / 2f, pos.y - h / 2f,
            w / 2f, h / 2f,
            w, h,
            1f, 1f,
            angle);
    }

    private void renderWheel(SpriteBatch batch, Body wheel) {
        if (wheelTexture == null) return;

        Vector2 pos = wheel.getPosition();
        float angle = wheel.getAngle() * MathUtils.radiansToDegrees;
        float diameter = config.wheelRadius * 2f;

        batch.draw(wheelTexture,
            pos.x - diameter / 2f, pos.y - diameter / 2f,
            diameter / 2f, diameter / 2f,
            diameter, diameter,
            1f, 1f,
            angle);
    }

    public Vector2 getPosition() {
        return chassis.getPosition();
    }

    public float getSpeed() {
        return chassis.getLinearVelocity().len();
    }

    public float getHorizontalSpeed() {
        return chassis.getLinearVelocity().x;
    }

    public float getChassisAngle() {
        return chassis.getAngle();
    }

    public Vector2 getLinearVelocity() {
        return chassis.getLinearVelocity();
    }

    public Body getChassis() {
        return chassis;
    }

    public Body getWheelFront() {
        return wheelFront;
    }

    public Body getWheelRear() {
        return wheelRear;
    }

    public WheelJoint getJointFront() {
        return jointFront;
    }

    public WheelJoint getJointRear() {
        return jointRear;
    }

    public VehicleConfig getConfig() {
        return config;
    }
}
