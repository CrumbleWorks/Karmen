package org.crumbleworks.forge.karmen.util;


public class Calc {
    
    private Calc() {}
    
    public static final long gdxDeltaToMillis(float delta) {
        return (long)(delta * 1000);
    }
}
