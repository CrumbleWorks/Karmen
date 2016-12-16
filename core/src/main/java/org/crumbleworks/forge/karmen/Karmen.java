package org.crumbleworks.forge.karmen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.crumbleworks.forge.karmen.scenes.AboutScreen;
import org.crumbleworks.forge.karmen.scenes.IntroScreen;
import org.crumbleworks.forge.karmen.scenes.MenuScreen;
import org.crumbleworks.forge.karmen.scenes.PlayScreen;
import org.crumbleworks.forge.karmen.scenes.SceneManager;
import org.crumbleworks.forge.karmen.scenes.SceneManager.Scenes;
import org.crumbleworks.forge.karmen.util.Calc;
import org.crumbleworks.forge.karmen.util.asset.FontLibrary;
import org.crumbleworks.forge.karmen.util.asset.TextureLibrary;
import org.crumbleworks.forge.karmen.util.asset.music.MusicService;
import org.crumbleworks.forge.karmen.util.asset.music.MusicType;
import org.crumbleworks.forge.karmen.util.asset.sound.SoundService;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Karmen extends Game {

    /*
     * !WARNING: SCREEN WIDTH/HEIGHT MUST NOT BE CHANGED,
     * THESE VALUES ARE USED FOR THE CAMERA AND SUBSEQUENTLY FOR THE PHYSICS
     * 
     * IF YOU NEED A BIGGER SCREEN, CHANGE WINDOW MEASUREMENTS SEPARATELY
     */
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private SceneManager sceneManager;
    
    private TextureLibrary texureLibrary;
    private FontLibrary fontLibrary;
    private SoundService soundService;
    private MusicService musicService;

    //DEBUG FLAG
    public static final boolean isDebug = true; //TODO turn off

    @Override
    public void create() {
        if(isDebug) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Karmen.SCREEN_WIDTH, Karmen.SCREEN_HEIGHT);

        batch = new SpriteBatch();

        texureLibrary = new TextureLibrary();
        fontLibrary = new FontLibrary();
        soundService = new SoundService();
        musicService = new MusicService();
        
        sceneManager = new SceneManager(this);
        
        //BEGIN
        sceneManager.changeScene(Scenes.INTRO);
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        super.render();
        musicService.update();
        
        /* DEBUG INFOS */
        if(isDebug) {
            if(debugShaper == null) {
                debugShaper = new ShapeRenderer();
            }
            
            renderDebug();
            debugMusicSwitcherCheck();
        }
    }
    
    @Override
    public void dispose() {
        sceneManager.dispose();

        texureLibrary.dispose();
        fontLibrary.dispose();
        soundService.dispose();
        musicService.dispose();

        batch.dispose();
    }
    
    /* ***********************************************************************
     * FANCY GETTERS
     */
    
    public Camera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
    
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public TextureLibrary getTextureLibrary() {
        return texureLibrary;
    }
    
    public FontLibrary getFontLibrary() {
        return fontLibrary;
    }

    public SoundService getSoundService() {
        return soundService;
    }
    
    public MusicService getMusicService() {
        return musicService;
    }

    /* ***********************************************************************
     * DEBUGGERY
     */
    
    private final List<MusicType> mt = new ArrayList<MusicType>(Arrays.asList(MusicType.values()));
    
    private void debugMusicSwitcherCheck() {
        if(Gdx.input.isKeyJustPressed(Keys.UP)) {
            int nextIndex = mt.indexOf(this.getMusicService().playing()) + 1;
            if(nextIndex < mt.size()) {
                this.getMusicService().play(mt.get(nextIndex));
            }
        } else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
            int nextIndex = mt.indexOf(this.getMusicService().playing()) - 1;
            if(nextIndex >= 0) {
                this.getMusicService().play(mt.get(nextIndex));
            }
        }
    }
    
    private ShapeRenderer debugShaper;
    
    private long deltaAccumulator = 0;
    private int frameCount = 0;
    private int fps = 0;
    
    private void renderDebug() {
        debugShaper.begin(ShapeType.Filled);
        debugShaper.setColor(Color.WHITE);
        debugShaper.box(0, 0, 0, 60, 30, 0);
        debugShaper.end();
        
        //fps calc
        deltaAccumulator += Calc.gdxDeltaToMillis(Gdx.graphics.getDeltaTime());
        if(deltaAccumulator >= Calc.ONE_SECOND_MS) {
            deltaAccumulator -= Calc.ONE_SECOND_MS;
            fps = frameCount;
            frameCount = 0;
        }
        frameCount++;
        
        batch.begin();
        fontLibrary.ARIAL.setColor(Color.RED);
        fontLibrary.ARIAL.draw(batch, "FPS: " + fps, 10, 20);
        batch.end();
    }
}