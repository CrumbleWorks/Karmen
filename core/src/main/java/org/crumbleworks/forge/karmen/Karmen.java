package org.crumbleworks.forge.karmen;

import org.crumbleworks.forge.karmen.screen.IntroScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Karmen extends Game {
	
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
	
	private SpriteBatch batch;
	private BitmapFont font;
	
	private Screen introScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		introScreen = new IntroScreen(this);
		font = new BitmapFont();
		
		setScreen(introScreen);
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
		
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }
	
}
