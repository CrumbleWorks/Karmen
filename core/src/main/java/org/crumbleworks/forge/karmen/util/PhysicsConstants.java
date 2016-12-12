package org.crumbleworks.forge.karmen.util;


public class PhysicsConstants {

    public static final float VERTICAL_GRAVITY = 0.0f;
    public static final float HORIZONTAL_GRAVITY = -9.8f;
    
    /* DOLL BEHAVIOURS */
    public static final float RUNNING_SPEED_MAX = 3.0f;
    public static final float RUNNING_SPEED_ACCELERATION = 0.8f;
    
    /* BOX2D Settings */
    public static final float WORLD_STEP = 1/300.0f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    
    private PhysicsConstants() {}
}