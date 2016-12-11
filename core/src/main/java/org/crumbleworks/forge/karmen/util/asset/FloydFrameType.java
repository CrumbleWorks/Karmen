package org.crumbleworks.forge.karmen.util.asset;

public enum FloydFrameType implements FrameType {
    
    STILL_FRONT_A(0, 0),
    STILL_FRONT_B(1, 0),
    STILL_SIDE_A(2, 0),
    STILL_SIDE_B(3, 0),
    JUMP_FRONT_UP(4, 0),
    JUMP_FRONT_DOWN(5, 0),
    HIT_SIDE_A(5, 0),
    BLOCK_FRONT_A(0, 1),
    BLOCK_FRONT_B(1, 1),
    MOVE_SIDE_A(2, 1),
    MOVE_SIDE_B(3, 1),
    KICK_SIDE(4, 1),
    FIST_SIDE(5, 1),
    HIT_SIDE_B(6, 1),
    JUMP_SIDE_UP(0, 2),
    JUMP_SIDE_DOWN(1, 2),
    MOVE_SIDE_A_AIR(2, 2),
    MOVE_SIDE_B_AIR(3, 2),
    KICK_SIDE_AIR(4, 2),
    BLOCK_SIDE_A(5, 2),
    BLOCK_SIDE_B(6, 2);
    
    private int x;
    private int y;
    
    private FloydFrameType(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
