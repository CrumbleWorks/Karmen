package org.crumbleworks.forge.karmen;

import org.crumbleworks.forge.karmen.screen.AboutScreen;
import org.crumbleworks.forge.karmen.screen.IntroScreen;
import org.crumbleworks.forge.karmen.screen.MenuScreen;
import org.crumbleworks.forge.karmen.screen.PlayScreen;
import org.crumbleworks.forge.karmen.util.Calc;
import org.crumbleworks.forge.karmen.util.asset.SoundLibrary;
import org.crumbleworks.forge.karmen.util.asset.TextureLibrary;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Karmen extends Game {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private BitmapFont fontMedium;
    private BitmapFont fontLarge;
    private BitmapFont fontArial;

    private TextureLibrary texureLibrary;
    private SoundLibrary soundLibrary;

    public Screen introScreen;
    public Screen menuScreen;
    public Screen playScreen;
    public Screen aboutScreen;

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

        fontMedium = new BitmapFont(Gdx.files.internal("fnt/NNPH_30.fnt"));
        fontLarge = new BitmapFont(Gdx.files.internal("fnt/NNPH_40.fnt"));
        fontArial = new BitmapFont();

        texureLibrary = new TextureLibrary();
        soundLibrary = new SoundLibrary();

        introScreen = new IntroScreen(this);
        menuScreen = new MenuScreen(this);
        playScreen = new PlayScreen(this);
        aboutScreen = new AboutScreen(this);

        setScreen(introScreen);
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        super.render();
        
        /* DEBUG INFOS */
        if(isDebug) {
            if(debugShaper == null) {
                debugShaper = new ShapeRenderer();
            }
            renderDebug();
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
        fontArial.setColor(Color.RED);
        fontArial.draw(batch, "FPS: " + fps, 10, 20);
        batch.end();
    }

    @Override
    public void dispose() {
        introScreen.dispose();
        menuScreen.dispose();
        playScreen.dispose();
        aboutScreen.dispose();

        texureLibrary.dispose();
        soundLibrary.dispose();

        batch.dispose();
        fontMedium.dispose();
        fontLarge.dispose();
    }

    public Camera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFontMedium() {
        return fontMedium;
    }

    public BitmapFont getFontLarge() {
        return fontLarge;
    }
    
    public BitmapFont getArial() {
        return fontArial;
    }

    public TextureLibrary getTextureLibrary() {
        return texureLibrary;
    }

    public SoundLibrary getSoundLibrary() {
        return soundLibrary;
    }

    public void changeScreen(Screen nextScreen) {
        setScreen(nextScreen);
    }

}
