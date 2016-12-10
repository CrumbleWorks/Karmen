package org.crumbleworks.forge.karmen;

import org.crumbleworks.forge.karmen.screen.IntroScreen;
import org.crumbleworks.forge.karmen.screen.MenuScreen;
import org.crumbleworks.forge.karmen.screen.PlayScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javafx.print.PageLayout;

public class Karmen extends Game {
	
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
	
	private SpriteBatch batch;
	
	private BitmapFont fontMedium;
	private BitmapFont fontLarge;
	
	private Screen introScreen;
	private Screen menuScreen;
	private Screen playScreen;
	
	private boolean playing;
	
	private final long introEnd = System.currentTimeMillis() + 0;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		
		fontMedium = new BitmapFont(Gdx.files.internal("fnt/NNPH_30.fnt"));
		fontLarge = new BitmapFont(Gdx.files.internal("fnt/NNPH_40.fnt"));

		introScreen = new IntroScreen(this);
		menuScreen = new MenuScreen(this);
		playScreen = new PlayScreen(this);
		
		setScreen(introScreen);
	}

	@Override
	public void render() {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		
		super.render();
		
		if(!playing && (System.currentTimeMillis() > introEnd)) {
		    setScreen(menuScreen);
		}
		
	}
	
	@Override
	public void dispose() {
	    introScreen.dispose();
	    menuScreen.dispose();
	    
		batch.dispose();
		fontMedium.dispose();
		fontLarge.dispose();
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

    public void startGame() {
        playing = true;
        setScreen(playScreen);
    }
	
}
