package org.crumbleworks.forge.karmen.util;

import com.badlogic.gdx.graphics.Color;

public class NeonColors {
    
    private static final float DEFAULT_ALPHA = 1;
    private static final float MAX_COLOR_VALUE = 255;
    
    public static final Color Yellow = convert(170, 255, 0);
    public static final Color Orange = convert(255, 170, 0);
    public static final Color Pink = convert(255, 0, 170);
    public static final Color Purple = convert(170, 0, 255);
    public static final Color Blue = convert(0, 170, 255);

    private static Color convert(int r, int g, int b) {
        return new Color(r / MAX_COLOR_VALUE, g / MAX_COLOR_VALUE, b / MAX_COLOR_VALUE, DEFAULT_ALPHA);
    }

}
