package org.crumbleworks.forge.karmen.util.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontLibrary {

    public final BitmapFont NNPH_MEDIUM;
    public final BitmapFont NNPH_LARGE;
    public final BitmapFont ARIAL;
    
    public FontLibrary() {
        NNPH_MEDIUM = new BitmapFont(Gdx.files.internal("fnt/NNPH_30.fnt"));
        NNPH_LARGE = new BitmapFont(Gdx.files.internal("fnt/NNPH_40.fnt"));
        ARIAL = new BitmapFont();
    }
    
    public void dispose() {
        NNPH_MEDIUM.dispose();
        NNPH_LARGE.dispose();
        ARIAL.dispose();
    }
}
