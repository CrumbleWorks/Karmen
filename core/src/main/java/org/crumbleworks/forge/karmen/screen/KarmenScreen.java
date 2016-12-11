package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A SUPER SCREEN FOR KARMEN
 */
public abstract class KarmenScreen implements Screen {
    
    private final int TEXTURE_WH = 32;
    
    private final Karmen game;
    
    //screen commands
    private final int[] keys;
    private final String[] lbl;
    private final Runnable[] func;
    private final Texture[] texDef;
    private final Texture[] texAct;
    private boolean[] act;
    
    /**
     * Allows setting various quick keys for <code>muting</code>, etc.; The logic will take the index of <code>keys[]</code> as reference
     * <p>Textures should be 8*8px
     *
     * @param game
     * @param keys
     * @param desc
     * @param func
     * @param texDef
     * @param texAct
     */
    public KarmenScreen(Karmen game, int[] keys, String[] lbl, Runnable[] func, Texture[] texDef, Texture[] texAct) {
        this.game = game;
        
        this.keys = keys;
        this.lbl = lbl;
        this.func = func;
        this.texDef = texDef;
        this.texAct = texAct;
        
        act = new boolean[keys.length];
        for(int i = 0 ; i < act.length ; i++) {
            act[i] = false;
        }
    }

    @Override
    public void render(float delta) {
        krender(delta);
        
        drawStuff();
        checkInput();
    }
    
    abstract void krender(float delta);
    
    private void drawStuff() {
        SpriteBatch batch = game.getBatch();
        BitmapFont font = game.getArial();
        
        final int ELEMENT_BOX_LENGTH = 64;
        final int ELEMENT_BOX_HEIGHT = 32;
        final int ELEMENT_BOX_OFFSET = 33;
        final int SCREEN_BORDER_PADDING = 4;
        
        final int SCREEN_WIDTH = Karmen.SCREEN_WIDTH;
        int elementCursor = keys.length * ELEMENT_BOX_LENGTH + SCREEN_BORDER_PADDING;
        
        batch.begin();
        
        for(int i = 0 ; i < keys.length ; i++) {
            //symbol
            Texture texTmp = null;
            if(act[i]) {
                texTmp = texAct[i];
            } else {
                texTmp = texDef[i];
            }
            batch.draw(texTmp, SCREEN_WIDTH - elementCursor , SCREEN_BORDER_PADDING, TEXTURE_WH, TEXTURE_WH);

            //name
            font.setColor(Color.WHITE);
            font.draw(batch, "" + lbl[i], SCREEN_WIDTH - elementCursor + ELEMENT_BOX_OFFSET, ELEMENT_BOX_HEIGHT - SCREEN_BORDER_PADDING);
            
            elementCursor -= ELEMENT_BOX_LENGTH;
        }
        batch.end();
    }

    private void checkInput() {
        for(int i = 0; keys.length > i; i++) {
            if(Gdx.input.isKeyJustPressed(keys[i])) {
                act[i] = !act[i];
                func[i].run();
            }
        }
    }
    
    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}