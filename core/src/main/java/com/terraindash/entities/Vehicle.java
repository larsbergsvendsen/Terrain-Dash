package com.terraindash.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public void render(SpriteBatch batch) {
        // TODO: Draw chassis and wheel sprites at body positions/angles
        // For now, rendering is handled by debug renderer
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
