package org.crumbleworks.forge.karmen.character;

public enum Facing {
    LEFT(90), RIGHT(-90), FRONT(0);
    
    private int rotation;
    
    private Facing(int rotation) {
        this.rotation = rotation;
    }
    
    public int getRotation() {
        return rotation;
    }
}
