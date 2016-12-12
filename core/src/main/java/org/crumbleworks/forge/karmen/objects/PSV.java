package org.crumbleworks.forge.karmen.objects;

import com.badlogic.gdx.math.Vector2;

/**
 * Position Size Velocity
 */
public class PSV {
    public Vector2 position;
    public Vector2 size;
    public Vector2 velocity;

    public PSV(Vector2 position, Vector2 size, Vector2 velocity) {
        this.position = position;
        this.size = size;
        this.velocity = velocity;
    }
}
