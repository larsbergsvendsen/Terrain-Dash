package com.terraindash.utils;

public final class Constants {

    private Constants() {}

    // Display
    public static final float VIEWPORT_WIDTH = 20f;   // meters
    public static final float VIEWPORT_HEIGHT = 12f;   // meters
    public static final float PIXELS_PER_METER = 64f;

    // Physics
    public static final float GRAVITY = -15f;
    public static final float TIME_STEP = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;

    // Vehicle defaults
    public static final float DEFAULT_MAX_SPEED = 25f;       // m/s
    public static final float DEFAULT_MOTOR_TORQUE = 80f;
    public static final float DEFAULT_TILT_STRENGTH = 40f;
    public static final float FLIP_ANGLE_THRESHOLD = 2.8f;    // ~160 degrees in radians

    // Camera
    public static final float CAMERA_LERP = 0.1f;
    public static final float CAMERA_LOOK_AHEAD = 3f;         // meters ahead of vehicle
    public static final float CAMERA_MIN_ZOOM = 0.8f;
    public static final float CAMERA_MAX_ZOOM = 1.5f;
    public static final float CAMERA_SPEED_ZOOM_FACTOR = 0.02f;

    // Terrain
    public static final float TERRAIN_CHUNK_WIDTH = 40f;      // meters
    public static final int TERRAIN_POINTS_PER_CHUNK = 40;
    public static final float TERRAIN_DEPTH = 10f;             // visual depth below surface

    // Gameplay
    public static final float NITRO_DURATION = 3f;             // seconds
    public static final float NITRO_MULTIPLIER = 1.8f;
    public static final float COIN_RADIUS = 0.3f;
    public static final float COIN_VALUE = 1;

    // Surface friction values
    public static final float FRICTION_ASPHALT = 0.9f;
    public static final float FRICTION_GRASS = 0.7f;
    public static final float FRICTION_SAND = 0.4f;
    public static final float FRICTION_ICE = 0.15f;
    public static final float FRICTION_DIRT = 0.6f;

    // Collision categories (bitmask)
    public static final short CATEGORY_TERRAIN = 0x0001;
    public static final short CATEGORY_VEHICLE = 0x0002;
    public static final short CATEGORY_COIN = 0x0004;
    public static final short CATEGORY_OBSTACLE = 0x0008;
    public static final short CATEGORY_BOOST = 0x0010;
    public static final short CATEGORY_SENSOR = 0x0020;
}
