package com.terraindash.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.terraindash.entities.Vehicle;
import com.terraindash.entities.VehicleConfig;
import com.terraindash.utils.Constants;

/**
 * Factory and utility methods for creating and controlling
 * the vehicle's Box2D bodies and joints.
 */
public class VehiclePhysics {

    public static Vehicle createVehicle(World world, Vector2 position) {
        return createVehicle(world, position, VehicleConfig.buggy());
    }

    public static Vehicle createVehicle(World world, Vector2 position, VehicleConfig config) {
        Body chassis = createChassis(world, position, config);
        Body wheelRear = createWheel(world,
            new Vector2(position.x - config.wheelBase / 2f, position.y - config.chassisHeight),
            config);
        Body wheelFront = createWheel(world,
            new Vector2(position.x + config.wheelBase / 2f, position.y - config.chassisHeight),
            config);

        WheelJoint jointRear = createWheelJoint(world, chassis, wheelRear,
            new Vector2(-config.wheelBase / 2f, -config.chassisHeight), config);
        WheelJoint jointFront = createWheelJoint(world, chassis, wheelFront,
            new Vector2(config.wheelBase / 2f, -config.chassisHeight), config);

        return new Vehicle(chassis, wheelFront, wheelRear, jointFront, jointRear, config);
    }

    private static Body createChassis(World world, Vector2 position, VehicleConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(config.chassisWidth / 2f, config.chassisHeight / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.chassisDensity;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_VEHICLE;
        fixtureDef.filter.maskBits = Constants.CATEGORY_TERRAIN | Constants.CATEGORY_OBSTACLE;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private static Body createWheel(World world, Vector2 position, VehicleConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(config.wheelRadius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.wheelDensity;
        fixtureDef.friction = config.getWheelFriction();
        fixtureDef.restitution = 0.05f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_VEHICLE;
        fixtureDef.filter.maskBits = Constants.CATEGORY_TERRAIN;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private static WheelJoint createWheelJoint(World world, Body chassis, Body wheel,
                                                Vector2 localAnchor, VehicleConfig config) {
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.initialize(chassis, wheel, wheel.getWorldCenter(), new Vector2(0, 1));
        jointDef.localAnchorA.set(localAnchor);
        jointDef.frequencyHz = config.getSuspensionFreq();
        jointDef.dampingRatio = config.getSuspensionDamp();
        jointDef.enableMotor = true;
        jointDef.motorSpeed = 0f;
        jointDef.maxMotorTorque = config.getMotorTorque();

        return (WheelJoint) world.createJoint(jointDef);
    }

    /** Apply constant forward motor torque to rear wheel */
    public static void applyMotorForce(Vehicle vehicle) {
        float targetSpeed = -vehicle.getConfig().getMaxSpeed();
        vehicle.getJointRear().setMotorSpeed(targetSpeed);
        vehicle.getJointRear().setMaxMotorTorque(vehicle.getConfig().getMotorTorque());
    }

    /** Apply rotational torque to chassis based on player tilt input [-1, 1] */
    public static void applyTiltTorque(Vehicle vehicle, float tiltInput) {
        if (tiltInput == 0f) return;
        float torque = tiltInput * vehicle.getConfig().getTiltStrength();
        vehicle.getChassis().applyTorque(torque, true);
    }

    /** Apply nitro boost as an impulse in the forward direction */
    public static void applyNitroBoost(Vehicle vehicle) {
        float boostForce = vehicle.getConfig().getNitroMultiplier() * 5f;
        float angle = vehicle.getChassis().getAngle();
        Vector2 force = new Vector2(
            (float) Math.cos(angle) * boostForce,
            (float) Math.sin(angle) * boostForce
        );
        vehicle.getChassis().applyForceToCenter(force, true);
    }
}
