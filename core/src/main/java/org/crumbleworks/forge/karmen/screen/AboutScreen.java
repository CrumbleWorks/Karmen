package org.crumbleworks.forge.karmen.screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.screen.MenuScreen.MenuButton;
import org.crumbleworks.forge.karmen.util.NeonColors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class AboutScreen implements Screen {

    private final Karmen game;
    
    public AboutScreen(final Karmen game) {
        this.game = game;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.getBatch().begin();
        game.getFontMedium().setColor(NeonColors.Orange);
        game.getFontMedium().draw(game.getBatch(), Gdx.files.internal("about/NNPH.about").readString(), 50, Karmen.SCREEN_HEIGHT - 100);
        game.getBatch().end();
        
        checkInput();
    }
    
    private void checkInput() {
        //chk for input
        if(Gdx.input.isKeyJustPressed(Keys.Q)) {
            game.setScreen(game.menuScreen);
        }
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
