package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class IntroScreen implements Screen {
    
    private static final float CRUMBLEWORKS_LOGO_SCALE = 8f;
    private static final float LIBGDX_LOGO_SCALE = 1.25f;
    
    private final Karmen game;
    
    private OrthographicCamera camera;
    
    private Texture crumbleWorksLogo;
    private Texture libGdxLogo;
    
    public IntroScreen(final Karmen game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Karmen.SCREEN_WIDTH, Karmen.SCREEN_HEIGHT);
        
        crumbleWorksLogo = new Texture("gfx/GrpLogo32x32.png");
        libGdxLogo = new Texture("gfx/libGdxLogo.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

//        drawGrid();
        
        Vector2 middleOfLeftLogoBox = getMiddleOfLeftLogoBox();
        Vector2 middleOfRightLogoBox = getMiddleOfRightLogoBox();
        
        game.getBatch().begin();
        game.getBatch().draw(crumbleWorksLogo, middleOfLeftLogoBox.x - ((crumbleWorksLogo.getWidth() * CRUMBLEWORKS_LOGO_SCALE) / 2), middleOfLeftLogoBox.y - ((crumbleWorksLogo.getHeight() * CRUMBLEWORKS_LOGO_SCALE) / 2), crumbleWorksLogo.getWidth() * CRUMBLEWORKS_LOGO_SCALE, crumbleWorksLogo.getHeight() * CRUMBLEWORKS_LOGO_SCALE);
        
        game.getFontMedium().setColor(Color.BLACK);
        game.getFontMedium().draw(game.getBatch(), "POWERED BY", middleOfRightLogoBox.x - ((libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE) / 2), middleOfRightLogoBox.y - ((libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE) / 2) + 100);
        game.getBatch().draw(libGdxLogo, middleOfRightLogoBox.x - ((libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE) / 2), middleOfRightLogoBox.y - ((libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE) / 2), libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE, libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE);
        
        game.getBatch().end();
        
    }
    
    private Vector2 getMiddleOfLeftLogoBox() {
        int verticalMiddleSectionHeight = Karmen.SCREEN_HEIGHT / 2;
        int topAndBottomSectionHeight = (Karmen.SCREEN_HEIGHT - verticalMiddleSectionHeight) / 2;
        
        int verticalMiddleSectionBottom = topAndBottomSectionHeight;
        int verticalMiddleSectionTop = verticalMiddleSectionBottom + verticalMiddleSectionHeight;
        
        int horizontalSectionWidth = Karmen.SCREEN_WIDTH / 2;
        
        return new Vector2(horizontalSectionWidth / 2, verticalMiddleSectionTop - (verticalMiddleSectionHeight / 2));
    }
    
    private Vector2 getMiddleOfRightLogoBox() {
        int verticalMiddleSectionHeight = Karmen.SCREEN_HEIGHT / 2;
        int topAndBottomSectionHeight = (Karmen.SCREEN_HEIGHT - verticalMiddleSectionHeight) / 2;
        
        int verticalMiddleSectionBottom = topAndBottomSectionHeight;
        int verticalMiddleSectionTop = verticalMiddleSectionBottom + verticalMiddleSectionHeight;
        
        int horizontalSectionWidth = Karmen.SCREEN_WIDTH / 2;
        
        return new Vector2((horizontalSectionWidth / 2) + horizontalSectionWidth, verticalMiddleSectionTop - (verticalMiddleSectionHeight / 2));
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
        crumbleWorksLogo.dispose();
        libGdxLogo.dispose();
    }

}
