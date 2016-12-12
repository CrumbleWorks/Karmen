package org.crumbleworks.forge.karmen.util;


public class PhysicsConstants {

    /* EVERYTHING IN approximate CM */
    
    public static final float VERTICAL_GRAVITY = -981.0f; //leftyrighty
    public static final float HORIZONTAL_GRAVITY = 0.0f; //uppedydownedy
    
    /* DOLL BEHAVIOURS */
    public static final float RUNNING_SPEED_MAX = 1200.0f; //everything in relation to Karmen.SCREEN_WIDTH
    public static final float RUNNING_SPEED_ACCELERATION = 80 * 1000f;
    
    public static final float STANDING_DRAG = RUNNING_SPEED_ACCELERATION / 2;
    
    /* BOX2D Settings */
    public static final float WORLD_STEP = 1/ (10 * 1000.0f); //DO NOT GO HIGHER, SHTUFF BREAKS IF HIGHER
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    
    private PhysicsConstants() {}
}