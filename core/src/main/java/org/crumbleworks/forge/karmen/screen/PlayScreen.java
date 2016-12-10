package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.character.Floyd;
import org.crumbleworks.forge.karmen.character.FloydInputAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class PlayScreen implements Screen {

    private final Karmen game;
    
    private Floyd floyd;
    
    private InputMultiplexer inputMultiplexer;
    
    public PlayScreen(final Karmen game) {
        this.game = game;
        this.floyd = new Floyd(Karmen.SCREEN_WIDTH / 2, 50, 128, 256, "gfx/Hero_7x4_16x32_CHARAKTER.png", game);
        
        inputMultiplexer = new InputMultiplexer(new FloydInputAdapter(floyd));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        
        floyd.update();
        
        Gdx.gl.glClearColor(1, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        
        floyd.draw();
        
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
