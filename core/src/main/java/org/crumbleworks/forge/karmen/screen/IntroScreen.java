package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class IntroScreen implements Screen {
    
    private static final float CRUMBLEWORKS_LOGO_SCALE = 8f;
    private static final float LIBGDX_LOGO_SCALE = 1.25f;
    
    private final Karmen game;
    
    private long currTime = System.currentTimeMillis();
    private boolean flashing = false;
    private int flashCount = 0;
    private final long flash = currTime + 800;
    private final long redEyes = currTime + 1800;
    private final long introEnd = currTime + 3800;
    
    private boolean displayGDX = false;
    
    private OrthographicCamera camera;
    
    private int regionIndex = 0;
    private TextureRegion[] crumbleWorksLogoRegions;
    private Texture libGdxLogo;
    
    public IntroScreen(final Karmen game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Karmen.SCREEN_WIDTH, Karmen.SCREEN_HEIGHT);
        
        Texture crumbleWorksLogos = new Texture("gfx/GrpLogo3x32x32.png");
        crumbleWorksLogoRegions = new TextureRegion[]{
                new TextureRegion(crumbleWorksLogos, 0, 0, 32, 32),
                new TextureRegion(crumbleWorksLogos, 32, 0, 32, 32),
                new TextureRegion(crumbleWorksLogos, 64, 0, 32, 32)
        }; 
        libGdxLogo = new Texture("gfx/libGdxLogo.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //RENDER
        Gdx.gl.glClearColor(1, 0.92f, 0.92f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

//        drawGrid();
        
        Vector2 middleOfLeftLogoBox = getMiddleOfLeftLogoBox();
        Vector2 middleOfRightLogoBox = getMiddleOfRightLogoBox();
        
        game.getBatch().begin();
        game.getBatch().draw(
                crumbleWorksLogoRegions[regionIndex],
                middleOfLeftLogoBox.x - ((crumbleWorksLogoRegions[regionIndex].getRegionWidth() * CRUMBLEWORKS_LOGO_SCALE) / 2),
                middleOfLeftLogoBox.y - ((crumbleWorksLogoRegions[regionIndex].getRegionHeight() * CRUMBLEWORKS_LOGO_SCALE) / 2),
                crumbleWorksLogoRegions[regionIndex].getRegionWidth() * CRUMBLEWORKS_LOGO_SCALE,
                crumbleWorksLogoRegions[regionIndex].getRegionHeight() * CRUMBLEWORKS_LOGO_SCALE
                );
        
        if(displayGDX) {
            game.getFontMedium().setColor(Color.BLACK);
            game.getFontMedium().draw(game.getBatch(), "POWERED BY", middleOfRightLogoBox.x - ((libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE) / 2), middleOfRightLogoBox.y - ((libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE) / 2) + 100);
            game.getBatch().draw(libGdxLogo, middleOfRightLogoBox.x - ((libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE) / 2), middleOfRightLogoBox.y - ((libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE) / 2), libGdxLogo.getWidth() * LIBGDX_LOGO_SCALE, libGdxLogo.getHeight() * LIBGDX_LOGO_SCALE);
        }
        
        game.getBatch().end();

        //LOGIC
        currTime = System.currentTimeMillis();
        if(currTime >= introEnd) { //zeit abarbeiten gross bis kleinstes
            game.setScreen(game.menuScreen);
        } else if(currTime >= redEyes) {
            regionIndex = 2;
            displayGDX = true;
        } else if(currTime >= flash) {
            if(!flashing) {
                regionIndex = 1;
                flashing = true;
            } else {
                regionIndex = 0;
                if(flashCount > 5) {
                    //hide for 5 frames
                    flashing = false;
                    flashCount = 0;
                } else {
                    flashCount++;
                }
            }
        }
            
        
        
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
        libGdxLogo.dispose();
    }

}
