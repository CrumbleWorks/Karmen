package org.crumbleworks.forge.karmen;

import org.crumbleworks.forge.karmen.screen.AboutScreen;
import org.crumbleworks.forge.karmen.screen.IntroScreen;
import org.crumbleworks.forge.karmen.screen.MenuScreen;
import org.crumbleworks.forge.karmen.screen.PlayScreen;
import org.crumbleworks.forge.karmen.util.asset.SoundLibrary;
import org.crumbleworks.forge.karmen.util.asset.TextureLibrary;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Karmen extends Game {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private BitmapFont fontMedium;
    private BitmapFont fontLarge;

    private TextureLibrary texureLibrary;
    private SoundLibrary soundLibrary;

    public Screen introScreen;
    public Screen menuScreen;
    public Screen playScreen;
    public Screen aboutScreen;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG); // TODO REMOVE FOR RELAIS

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Karmen.SCREEN_WIDTH, Karmen.SCREEN_HEIGHT);

        batch = new SpriteBatch();

        fontMedium = new BitmapFont(Gdx.files.internal("fnt/NNPH_30.fnt"));
        fontLarge = new BitmapFont(Gdx.files.internal("fnt/NNPH_40.fnt"));

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
