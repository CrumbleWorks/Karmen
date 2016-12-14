package org.crumbleworks.forge.karmen.scenes;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.scenes.SceneManager.Scene;
import org.crumbleworks.forge.karmen.scenes.SceneManager.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A SUPER SCREEN FOR KARMEN
 */
public abstract class KarmenScreen implements Scene {
    
    private final int TEXTURE_WH = 32;
    
    private final Karmen game;
    
    private static boolean isInited = false;
    //screen commands
    static private int[] keys;
    static private String[] lbl;
    static private Runnable[] func;
    static private Texture[] texDef;
    static private Texture[] texAct;
    static private boolean[] act;
    
    private final boolean[] active;
    
    /**
     * the boolean 
     */
    public KarmenScreen(Karmen game, boolean[] active) {
        this.game = game;
        this.active = active;
        
        if(!isInited) {
            isInited = true;
            keys = new int[]{Keys.ESCAPE, Keys.M};
            lbl = new String[]{"ESC", "M"};
            func = new Runnable[]{()->{game.getSceneManager().changeScene(Scenes.MENU);},
                                  ()->{game.getMusicService().toggleMute();
                                       game.getSoundService().toggleMute();}};
            texDef = new Texture[]{game.getTextureLibrary().OPT_RET,
                                   game.getTextureLibrary().OPT_NOTE};
            texAct = new Texture[]{game.getTextureLibrary().OPT_RET,
                                   game.getTextureLibrary().OPT_NOTE_STRIKE};
            act = new boolean[]{false, false};
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
        
        final int ELEMENT_BOX_LENGTH = 64;
        final int ELEMENT_BOX_HEIGHT = 32;
        final int ELEMENT_BOX_OFFSET = 33;
        final int SCREEN_BORDER_PADDING = 4;
        
        final int SCREEN_WIDTH = Karmen.SCREEN_WIDTH;
        int elementCursor = keys.length * ELEMENT_BOX_LENGTH + SCREEN_BORDER_PADDING;
        
        batch.begin();
        
        for(int i = 0 ; i < keys.length ; i++) {
            if(active[i]) {
                //symbol
                Texture texTmp = null;
                if(act[i]) {
                    texTmp = texAct[i];
                } else {
                    texTmp = texDef[i];
                }
                batch.draw(texTmp, SCREEN_WIDTH - elementCursor , SCREEN_BORDER_PADDING, TEXTURE_WH, TEXTURE_WH);
                
                //name
                game.getFontLibrary().ARIAL.setColor(Color.WHITE);
                game.getFontLibrary().ARIAL.draw(batch, "" + lbl[i], SCREEN_WIDTH - elementCursor + ELEMENT_BOX_OFFSET, ELEMENT_BOX_HEIGHT - SCREEN_BORDER_PADDING);
                
                elementCursor -= ELEMENT_BOX_LENGTH;
            }
        }
        batch.end();
    }

    private void checkInput() {
        for(int i = 0; keys.length > i; i++) {
            if(active[i]) {
                if(Gdx.input.isKeyJustPressed(keys[i])) {
                    act[i] = !act[i];
                    func[i].run();
                }
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