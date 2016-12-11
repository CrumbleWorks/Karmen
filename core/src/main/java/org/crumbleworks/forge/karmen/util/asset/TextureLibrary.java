package org.crumbleworks.forge.karmen.util.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureLibrary {

    private Texture crumbleWorksLogo;
    private Texture libGdxLogo;
    private Texture lampTexture;
    private Texture floydTexture;
    private Texture explosionTexture;
    
    public TextureLibrary() {
        crumbleWorksLogo = loadTexture("gfx/GrpLogo3x32x32.png");
        libGdxLogo = loadTexture("gfx/libGdxLogo.png");
        lampTexture = loadTexture("gfx/Lamp.png");
        floydTexture = loadTexture("gfx/Hero_7x4_16x32_CHARAKTER.png");
        explosionTexture = loadTexture("gfx/effects/explosion.png");
    }
    
    private Texture loadTexture(String textureFileName) {
        return new Texture(Gdx.files.internal(textureFileName));
    }
    
    public Texture getCrumbleWorksLogo() {
        return crumbleWorksLogo;
    }
    
    public Texture getLibGdxLogo() {
        return libGdxLogo;
    }
    
    public Texture getLampTexture() {
        return lampTexture;
    }
    
    public Texture getFloydTexture() {
        return floydTexture;
    }
    
    /* REGIONS */
    
    public TextureRegion[] getFloydRegions() {
        int FRAME_WIDTH = 16;
        int FRAME_HEIGHT = 32;
        
        TextureRegion[] regions = new TextureRegion[28];
        
        for(FloydFrameType type : FloydFrameType.values()) {
            regions[type.ordinal()] = new TextureRegion(getFloydTexture(), type.getX() * FRAME_WIDTH, type.getY() * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
        }
        
        return regions;
    }
    
    public Texture getExplosionTexture() {
        return explosionTexture;
    }
    
    public void dispose() {
        crumbleWorksLogo.dispose();
        libGdxLogo.dispose();
        lampTexture.dispose();
        floydTexture.dispose();
        explosionTexture.dispose();
    }
    
}
