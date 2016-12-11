package org.crumbleworks.forge.karmen.util.asset;

public enum FloydFrameType implements FrameType {
    
    STILL_FRONT_A(0, 0),   STILL_FRONT_B(1, 0), STILL_SIDE_RA(2, 0),   STILL_SIDE_RB(3, 0), STILL_SIDE_LA(4, 0),   STILL_SIDE_LB(5, 0), MOVE_SIDE_RA_AIR(6, 0), MOVE_SIDE_RB_AIR(7, 0), KICK_SIDE_R_AIR(8, 0),
    BLOCK_FRONT_A(0, 1),   BLOCK_FRONT_B(1, 1), BLOCK_SIDE_RA(2, 1),   BLOCK_SIDE_RB(3, 1), BLOCK_SIDE_LA(4, 1),   BLOCK_SIDE_LB(5, 1), MOVE_SIDE_LA_AIR(6, 1), MOVE_SIDE_LB_AIR(7, 1), KICK_SIDE_L_AIR(8, 1),
      KICK_SIDE_R(0, 2),     KICK_SIDE_L(1, 2),  MOVE_SIDE_RA(2, 2),    MOVE_SIDE_RB(3, 2),  MOVE_SIDE_LA(4, 2),    MOVE_SIDE_LB(5, 2),      HIT_SIDE_RA(6, 2),      HIT_SIDE_RB(7, 2),    PUNCH_SIDE_R(8, 2),
    JUMP_FRONT_UP(0, 3), JUMP_FRONT_DOWN(1, 3), JUMP_SIDE_RUP(2, 3), JUMP_SIDE_RDOWN(3, 3), JUMP_SIDE_LUP(4, 3), JUMP_SIDE_LDOWN(5, 3),      HIT_SIDE_LA(6, 3),      HIT_SIDE_LB(7, 3),    PUNCH_SIDE_L(8, 3);
    
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
