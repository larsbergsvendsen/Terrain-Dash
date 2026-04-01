package com.terraindash.entities;

/**
 * Configuration data for a vehicle type, including base stats
 * and current upgrade levels. Upgrade levels affect the computed stats.
 */
public class VehicleConfig {

    public final String id;
    public final String name;

    // Base stats (before upgrades)
    public final float baseMaxSpeed;
    public final float baseMotorTorque;
    public final float baseTiltStrength;
    public final float chassisWidth;
    public final float chassisHeight;
    public final float wheelRadius;
    public final float wheelBase;          // distance between wheels
    public final float chassisDensity;
    public final float wheelDensity;
    public final float suspensionFrequency;
    public final float suspensionDamping;

    // Upgrade levels (0-10)
    public int engineLevel = 0;
    public int suspensionLevel = 0;
    public int wheelsLevel = 0;
    public int nitroLevel = 0;
    public int armorLevel = 0;

    public VehicleConfig(String id, String name,
                         float baseMaxSpeed, float baseMotorTorque, float baseTiltStrength,
                         float chassisWidth, float chassisHeight,
                         float wheelRadius, float wheelBase,
                         float chassisDensity, float wheelDensity,
                         float suspensionFrequency, float suspensionDamping) {
        this.id = id;
        this.name = name;
        this.baseMaxSpeed = baseMaxSpeed;
        this.baseMotorTorque = baseMotorTorque;
        this.baseTiltStrength = baseTiltStrength;
        this.chassisWidth = chassisWidth;
        this.chassisHeight = chassisHeight;
        this.wheelRadius = wheelRadius;
        this.wheelBase = wheelBase;
        this.chassisDensity = chassisDensity;
        this.wheelDensity = wheelDensity;
        this.suspensionFrequency = suspensionFrequency;
        this.suspensionDamping = suspensionDamping;
    }

    public float getMaxSpeed() {
        return baseMaxSpeed * (1f + engineLevel * 0.08f);
    }

    public float getMotorTorque() {
        return baseMotorTorque * (1f + engineLevel * 0.1f);
    }

    public float getTiltStrength() {
        return baseTiltStrength * (1f + suspensionLevel * 0.05f);
    }

    public float getSuspensionFreq() {
        return suspensionFrequency * (1f + suspensionLevel * 0.08f);
    }

    public float getSuspensionDamp() {
        return suspensionDamping * (1f + suspensionLevel * 0.05f);
    }

    public float getWheelFriction() {
        return 0.8f + wheelsLevel * 0.04f;
    }

    public float getNitroDuration() {
        return 2f + nitroLevel * 0.3f;
    }

    public float getNitroMultiplier() {
        return 1.5f + nitroLevel * 0.1f;
    }

    public int getMaxFlips() {
        return 1 + armorLevel / 3;
    }

    /** Predefined vehicle: Buggy (starter vehicle) */
    public static VehicleConfig buggy() {
        return new VehicleConfig(
            "buggy", "Buggy",
            20f, 60f, 35f,
            1.8f, 0.6f,
            0.35f, 1.4f,
            2f, 1f,
            4f, 0.7f
        );
    }

    /** Predefined vehicle: Monster Truck */
    public static VehicleConfig monsterTruck() {
        return new VehicleConfig(
            "monstertruck", "Monster Truck",
            18f, 90f, 30f,
            2.2f, 0.8f,
            0.5f, 1.8f,
            4f, 2f,
            3f, 0.8f
        );
    }

    /** Predefined vehicle: Sports Car */
    public static VehicleConfig sportsCar() {
        return new VehicleConfig(
            "sportscar", "Sports Car",
            30f, 50f, 25f,
            2.0f, 0.4f,
            0.3f, 1.6f,
            1.5f, 0.8f,
            6f, 0.5f
        );
    }

    /** Predefined vehicle: Rocket Bike */
    public static VehicleConfig rocketBike() {
        return new VehicleConfig(
            "rocketbike", "Rocket Bike",
            28f, 40f, 45f,
            1.4f, 0.5f,
            0.25f, 1.0f,
            1f, 0.6f,
            5f, 0.4f
        );
    }

    /** Predefined vehicle: Tank */
    public static VehicleConfig tank() {
        return new VehicleConfig(
            "tank", "Tank",
            12f, 120f, 20f,
            2.5f, 1.0f,
            0.45f, 2.0f,
            6f, 3f,
            2.5f, 0.9f
        );
    }

    /** Predefined vehicle: Hovercraft */
    public static VehicleConfig hovercraft() {
        return new VehicleConfig(
            "hovercraft", "Hovercraft",
            25f, 55f, 40f,
            2.0f, 0.5f,
            0.35f, 1.6f,
            1.5f, 0.5f,
            3f, 0.3f
        );
    }
}
