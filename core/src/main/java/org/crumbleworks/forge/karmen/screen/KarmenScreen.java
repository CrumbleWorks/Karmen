package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A SUPER SCREEN FOR KARMEN
 */
public abstract class KarmenScreen implements Screen {
    
    private final int TEXTURE_WH = 8;
    
    private final Karmen game;
    
    //screen commands
    private final int[] keys;
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
    public KarmenScreen(Karmen game, int[] keys, Runnable[] func, Texture[] texDef, Texture[] texAct) {
        this.game = game;
        
        this.keys = keys;
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
        
        final int ELEMENT_BOX_LENGTH = 16;
        final int ELEMENT_BOX_OFFSET = 7;
        final int SCREEN_BORDER_PADDING = 2;
        
        final int SCREEN_WIDTH = Gdx.graphics.getWidth();
        int elementCursor = keys.length * ELEMENT_BOX_LENGTH + SCREEN_BORDER_PADDING;
        
        batch.begin();
        for(int i = 0 ; i < keys[i] ; i++) {
            //name
            font.draw(batch, "" + Keys.toString(keys[i]), SCREEN_WIDTH - elementCursor, SCREEN_BORDER_PADDING);
            //symbol
            Texture texTmp = null;
            if(act[i]) {
                texTmp = texAct[i];
            } else {
                texTmp = texDef[i];
            }
            batch.draw(texTmp, SCREEN_WIDTH - elementCursor + ELEMENT_BOX_OFFSET , SCREEN_BORDER_PADDING, TEXTURE_WH, TEXTURE_WH);
            
            elementCursor -= ELEMENT_BOX_LENGTH;
        }
        batch.end();
    }

    private void checkInput() {
        for(int i = 0; keys.length > i; i++) {
            if(Gdx.input.isKeyJustPressed(keys[i])) {
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