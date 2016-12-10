package org.crumbleworks.forge.karmen.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.util.NeonColors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

public class AboutScreen implements Screen {

    private final Karmen game;
    
    private BitmapFont arialFont;
    
    private List<String> creditLines;
    private int currentTopCreditLineIndex;
    private int creditLinesOffset;
    private float relatvieCreditPosition;
    private float creditSpeed;
    
    public AboutScreen(final Karmen game) {
        this.game = game;
        
        arialFont = new BitmapFont();
        
        currentTopCreditLineIndex = 0;
        creditLinesOffset = 30;
        relatvieCreditPosition = 0f;
        creditSpeed = 0.25f;
        
        readAboutFile("about/NNPH.about");
    }
    
    private void readAboutFile(String aboutFileName) {
        creditLines = new ArrayList<>(Arrays.asList(Gdx.files.internal(aboutFileName).readString().split("\\r?\\n")));
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.getBatch().begin();
        
        arialFont.setColor(NeonColors.Orange);
        
        if(currentTopCreditLineIndex < creditLines.size()) {
            currentTopCreditLineIndex = (int) (relatvieCreditPosition / creditLinesOffset);
            if(currentTopCreditLineIndex >= creditLines.size()) {
                currentTopCreditLineIndex = creditLines.size() - 1;
            }
        }
        else {
            currentTopCreditLineIndex = creditLines.size() - 1;
        }
        
        relatvieCreditPosition += creditSpeed;
        
        for(int i = 0; i <= currentTopCreditLineIndex; i++) {
            float fontX = Karmen.SCREEN_WIDTH / 4;
            float fontY = relatvieCreditPosition - (i * creditLinesOffset);
            
            arialFont.draw(game.getBatch(), creditLines.get(i), fontX, fontY, Karmen.SCREEN_WIDTH / 2, Align.center, true);
            
            if((i == (creditLines.size() - 1)) && fontY > Karmen.SCREEN_HEIGHT + creditLinesOffset) {
                game.setScreen(game.menuScreen);
            }
        }
        
        arialFont.draw(game.getBatch(), "PRESS Q TO SKIP", Karmen.SCREEN_WIDTH - 150, 25);
        
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
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        arialFont.dispose();
    }

}
