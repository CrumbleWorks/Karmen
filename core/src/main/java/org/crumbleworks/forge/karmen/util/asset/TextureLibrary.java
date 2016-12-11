package org.crumbleworks.forge.karmen.util.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureLibrary {

    private Texture crumbleWorksLogo;
    private Texture libGdxLogo;
    private Texture lampTexture;
    private Texture floydTexture;
    
    public TextureLibrary() {
        crumbleWorksLogo = loadTexture("gfx/GrpLogo3x32x32.png");
        libGdxLogo = loadTexture("gfx/libGdxLogo.png");
        lampTexture = loadTexture("gfx/Lamp.png");
        floydTexture = loadTexture("gfx/Hero_7x4_16x32_CHARAKTER.png");
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
    
    public void dispose() {
        crumbleWorksLogo.dispose();
        libGdxLogo.dispose();
        lampTexture.dispose();
        floydTexture.dispose();
    }
    
}
