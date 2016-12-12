package org.crumbleworks.forge.karmen.util;


public class Calc {
    
    public static final long ONE_SECOND_MS = 1000;
    
    private Calc() {}
    
    public static final long gdxDeltaToMillis(float delta) {
        return (long)(delta * ONE_SECOND_MS);
    }
}
