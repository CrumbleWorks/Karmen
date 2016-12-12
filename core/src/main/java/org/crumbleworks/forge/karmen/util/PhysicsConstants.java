package org.crumbleworks.forge.karmen.util;


public class PhysicsConstants {

    /* SPEEDS IN approximate CM/S */
    /* FORCES ARE IN probably NEWTON */
    
    public static final float VERTICAL_GRAVITY = -981.0f; //leftyrighty
    public static final float HORIZONTAL_GRAVITY = 0.0f; //uppedydownedy
    
    /* DOLL BEHAVIOURS */
    public static final float RUNNING_SPEED_MAX = 1200.0f; //everything in relation to Karmen.SCREEN_WIDTH
    public static final float RUNNING_SPEED_ACCELERATION = 100 * 1000f;
    
    public static final float STANDING_DRAG = RUNNING_SPEED_ACCELERATION * 2;
    public static final float BLOCKING_DRAG = RUNNING_SPEED_ACCELERATION * 8;
    public static final long BLOCK_DELAY_MS = 280;
    
    public static final float JUMP_FORCE = 600f;
    
    public static final float PUNCH_FORCE = 2000.0f;
    public static final float KICK_FORCE = 4000.0f;
    
    /* BOX2D Settings */
//    public static final float WORLD_STEP = 1/ (10 * 1000.0f); //DO NOT GO HIGHER, SHTUFF BREAKS IF HIGHER
    public static final float WORLD_STEP = 1 / 900.0f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    
    private PhysicsConstants() {}
}