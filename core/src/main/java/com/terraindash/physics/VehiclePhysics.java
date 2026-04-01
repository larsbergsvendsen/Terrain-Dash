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
 * the vehicle's Box2D bodies and joints. Includes speed limiting,
 * angular damping, and air-control tuning for good game feel.
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
            new Vector2(-config.wheelBase / 2f, -config.chassisHeight), config, true);
        WheelJoint jointFront = createWheelJoint(world, chassis, wheelFront,
            new Vector2(config.wheelBase / 2f, -config.chassisHeight), config, false);

        chassis.setUserData("vehicle");
        wheelFront.setUserData("vehicle");
        wheelRear.setUserData("vehicle");

        return new Vehicle(chassis, wheelFront, wheelRear, jointFront, jointRear, config);
    }

    private static Body createChassis(World world, Vector2 position, VehicleConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angularDamping = 1.5f;

        Body body = world.createBody(bodyDef);

        float hw = config.chassisWidth / 2f;
        float hh = config.chassisHeight / 2f;

        PolygonShape shape = new PolygonShape();
        shape.set(new float[]{
            -hw, -hh,
             hw * 0.9f, -hh,
             hw, -hh * 0.3f,
             hw * 0.7f, hh,
            -hw * 0.4f, hh,
            -hw, hh * 0.5f
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.chassisDensity;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_VEHICLE;
        fixtureDef.filter.maskBits = (short) (Constants.CATEGORY_TERRAIN | Constants.CATEGORY_OBSTACLE
            | Constants.CATEGORY_COIN | Constants.CATEGORY_BOOST | Constants.CATEGORY_SENSOR);

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private static Body createWheel(World world, Vector2 position, VehicleConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.angularDamping = 0.3f;

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(config.wheelRadius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.wheelDensity;
        fixtureDef.friction = config.getWheelFriction();
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_VEHICLE;
        fixtureDef.filter.maskBits = Constants.CATEGORY_TERRAIN;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private static WheelJoint createWheelJoint(World world, Body chassis, Body wheel,
                                                Vector2 localAnchor, VehicleConfig config,
                                                boolean isDriveWheel) {
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.initialize(chassis, wheel, wheel.getWorldCenter(), new Vector2(0, 1));
        jointDef.localAnchorA.set(localAnchor);
        jointDef.frequencyHz = config.getSuspensionFreq();
        jointDef.dampingRatio = config.getSuspensionDamp();
        jointDef.enableMotor = isDriveWheel;
        jointDef.motorSpeed = 0f;
        jointDef.maxMotorTorque = isDriveWheel ? config.getMotorTorque() : 0f;

        return (WheelJoint) world.createJoint(jointDef);
    }

    /** Apply motor force with smooth speed limiting */
    public static void applyMotorForce(Vehicle vehicle) {
        float currentSpeed = vehicle.getHorizontalSpeed();
        float maxSpeed = vehicle.getConfig().getMaxSpeed();

        float speedRatio = Math.abs(currentSpeed) / maxSpeed;
        float torqueScale = 1f;
        if (speedRatio > 0.8f) {
            torqueScale = Math.max(0f, 1f - (speedRatio - 0.8f) * 5f);
        }

        float targetSpeed = -maxSpeed;
        vehicle.getJointRear().setMotorSpeed(targetSpeed);
        vehicle.getJointRear().setMaxMotorTorque(vehicle.getConfig().getMotorTorque() * torqueScale);
    }

    /** Release motor (coast) */
    public static void releaseMotor(Vehicle vehicle) {
        vehicle.getJointRear().setMotorSpeed(0f);
        vehicle.getJointRear().setMaxMotorTorque(0f);
    }

    /** Apply rotational torque based on player tilt, with air-control bonus */
    public static void applyTiltTorque(Vehicle vehicle, float tiltInput) {
        if (tiltInput == 0f) return;

        float torque = tiltInput * vehicle.getConfig().getTiltStrength();

        boolean inAir = isInAir(vehicle);
        if (inAir) {
            torque *= 1.5f;
        }

        vehicle.getChassis().applyTorque(torque, true);
    }

    /** Apply nitro boost force in the forward direction */
    public static void applyNitroBoost(Vehicle vehicle) {
        float boostForce = vehicle.getConfig().getNitroMultiplier() * 8f;
        float angle = vehicle.getChassis().getAngle();
        Vector2 force = new Vector2(
            (float) Math.cos(angle) * boostForce,
            (float) Math.sin(angle) * boostForce
        );
        vehicle.getChassis().applyForceToCenter(force, true);
    }

    /** Check if the vehicle is airborne by testing vertical velocity stability */
    public static boolean isInAir(Vehicle vehicle) {
        float vy = vehicle.getLinearVelocity().y;
        float rearWheelVy = vehicle.getWheelRear().getLinearVelocity().y;
        float frontWheelVy = vehicle.getWheelFront().getLinearVelocity().y;

        return Math.abs(vy) > 1.5f && Math.abs(rearWheelVy) > 1.5f && Math.abs(frontWheelVy) > 1.5f;
    }

    /** Compute landing impact force from vertical velocity */
    public static float getLandingImpact(Vehicle vehicle) {
        float vy = vehicle.getLinearVelocity().y;
        return Math.max(0, -vy);
    }

    /** Apply angular velocity damping when grounded for stability */
    public static void applyGroundedStabilization(Vehicle vehicle) {
        if (!isInAir(vehicle)) {
            float angularVel = vehicle.getChassis().getAngularVelocity();
            vehicle.getChassis().setAngularVelocity(angularVel * 0.95f);
        }
    }
}
