package org.crumbleworks.forge.karmen.util.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontLibrary {

    public final BitmapFont NNPH_MEDIUM;
    public final BitmapFont NNPH_LARGE;
    public final BitmapFont ARIAL;
    
    public FontLibrary() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fnt/NNPH.ttf"));
        
        NNPH_MEDIUM = generateFont(generator, 30);
        NNPH_LARGE = generateFont(generator, 40);
        ARIAL = new BitmapFont();
        
        generator.dispose();
    }
    
    private BitmapFont generateFont(FreeTypeFontGenerator generator, int size) {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (int) Math.ceil(size);
        generator.scaleForPixelHeight((int) Math.ceil(size));
        return generator.generateFont(parameter);
    }
    
    public void dispose() {
        NNPH_MEDIUM.dispose();
        NNPH_LARGE.dispose();
        ARIAL.dispose();
    }
}
