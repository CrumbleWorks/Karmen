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
    private Texture danceFloorTexture;
    
    /* OPTIONS */
    public final Texture OPT_NOTE;
    public final Texture OPT_NOTE_STRIKE;
    public final Texture OPT_DOOR;
    public final Texture OPT_DOOR_OPEN;
    public final Texture OPT_RET;
    public final Texture OPT_RET_INV;
    
    public TextureLibrary() {
        crumbleWorksLogo = loadTexture("gfx/GrpLogo3x32x32.png");
        libGdxLogo = loadTexture("gfx/libGdxLogo.png");
        lampTexture = loadTexture("gfx/Lamp.png");
        floydTexture = loadTexture("gfx/Hero_9x4_16x32_CHARAKTER.png");
        explosionTexture = loadTexture("gfx/effects/explosion.png");
        danceFloorTexture = loadTexture("gfx/dance_floor_animation.png");
        
        OPT_NOTE = loadTexture("gfx/options/note.png");
        OPT_NOTE_STRIKE = loadTexture("gfx/options/note_striked.png");
        OPT_DOOR = loadTexture("gfx/options/door.png");
        OPT_DOOR_OPEN = loadTexture("gfx/options/door_open.png");
        OPT_RET = loadTexture("gfx/options/ret.png");
        OPT_RET_INV = loadTexture("gfx/options/ret_inv.png");
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
    
    public Texture getDanceFloorTexture() {
        return danceFloorTexture;
    }
    
    /* REGIONS */
    
    public TextureRegion[] getFloydRegions() {
        int FRAME_WIDTH = 16;
        int FRAME_HEIGHT = 32;
        
        TextureRegion[] regions = new TextureRegion[36];
        
        for(FloydFrameType type : FloydFrameType.values()) {
            regions[type.ordinal()] = new TextureRegion(getFloydTexture(), type.getX() * FRAME_WIDTH, type.getY() * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
        }
        
        return regions;
    }
    
    public TextureRegion[] getDanceFloorRegions() {
        int FRAME_WIDTH = 174;
        int FRAME_HEIGHT = 36;
        
        TextureRegion[][] tempTextureRegions = TextureRegion.split(getDanceFloorTexture(), FRAME_WIDTH, FRAME_HEIGHT);
        
        int rows = tempTextureRegions.length;
        int cols = tempTextureRegions[0].length;
        
        TextureRegion[] textureRegions = new TextureRegion[rows * cols];
        
        int frameIndex = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                textureRegions[frameIndex++] = tempTextureRegions[i][j];
            }
        }
        
        return textureRegions;
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
        danceFloorTexture.dispose();
    }
    
}
