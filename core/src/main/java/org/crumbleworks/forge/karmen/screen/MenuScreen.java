package org.crumbleworks.forge.karmen.screen;

import java.util.ArrayList;
import java.util.List;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.util.NeonColors;
import org.crumbleworks.forge.karmen.util.asset.TextureLibrary;
import org.crumbleworks.forge.karmen.util.asset.music.MusicType;
import org.crumbleworks.forge.karmen.util.asset.sound.SoundType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MenuScreen extends KarmenScreen {
    
    private final Karmen game;
    
    private ShapeRenderer shapeRenderer;
    
    private List<MenuButton> buttons;
    private int selectedButton;
    
    private Animation lampAnimation;
    private float lampAnimationStateTime;
    private float lampFrameDuration;
    
    private Animation floydAnimation;
    private float floydAnimationStateTime;
    private float floydFrameDuration;
    
    public MenuScreen(Karmen game) {
        super(game,
                new int[]{Keys.M},
                new String[]{"M"},
                new Runnable[]{()->{game.getMusicService().toggleMute();
                                    game.getSoundService().toggleMute();}},
                new Texture[]{game.getTextureLibrary().OPT_NOTE},
                new Texture[]{game.getTextureLibrary().OPT_NOTE_STRIKE}
             );
        
        this.game = game;
        
        shapeRenderer = new ShapeRenderer();
        
        buttons = new ArrayList<MenuButton>() {{
            add(new MenuButton("PLAY", 'P', NeonColors.Green, true, ()->{game.setScreen(game.playScreen);}));
            add(new MenuButton("ABOUT", 'A', NeonColors.Pink, false, ()->{game.setScreen(game.aboutScreen);}));
            add(new MenuButton("QUIT", 'Q', NeonColors.Blue, false, ()->{Gdx.app.exit();}));
        }};
        selectedButton = 0;
        
        lampFrameDuration = 0.25f;
        lampAnimationStateTime = 0f;
        initLampAnimation();
        
        floydFrameDuration = 0.25f;
        floydAnimationStateTime = 0f;
        initFloydAnimation();
    }
    
    private void initLampAnimation() {
        int width = 32;
        int height = 64;
        
        TextureRegion[][] tempTextureRegions = TextureRegion.split(game.getTextureLibrary().getLampTexture(), width, height);
        
        int rows = tempTextureRegions.length;
        int cols = tempTextureRegions[0].length;
        
        TextureRegion[] textureRegions = new TextureRegion[rows * cols];
        int index = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                textureRegions[index++] = tempTextureRegions[i][j];
            }
        }

        lampAnimation = new Animation(lampFrameDuration, Array.with(textureRegions), PlayMode.LOOP_RANDOM);
    }
    
    private void initFloydAnimation() {
        TextureRegion[] textureRegions;

        int width = 16;
        int height = 32;
        
        textureRegions = new TextureRegion[] {
            new TextureRegion(game.getTextureLibrary().getFloydTexture(), 32, 0, width, height),
            new TextureRegion(game.getTextureLibrary().getFloydTexture(), 48, 0, width, height),
        };
        
        floydAnimation = new Animation(floydFrameDuration, Array.with(textureRegions), PlayMode.LOOP);
    }
    
    /* ******************************************************************** */

    @Override
    void krender(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
//        drawGrid();
        
        drawTitle();
        drawButtons();
        drawLamp(delta);
        drawFloyd(delta);
        
        checkInput();
    }

    private void checkInput() {
        if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            buttons.get(selectedButton).selected = false;
            if(selectedButton == 0) {
                selectedButton = buttons.size() - 1;
            }
            else {
                selectedButton--;
            }
            buttons.get(selectedButton).selected = true;
            buttons.get(selectedButton).on = true;
            buttons.get(selectedButton).nextDraw = TimeUtils.millis() - MenuButton.DRAW_PAUSE;
        }
        else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            buttons.get(selectedButton).selected = false;
            if(selectedButton == (buttons.size() - 1)) {
                selectedButton = 0;
            }
            else {
                selectedButton++;
            }
            buttons.get(selectedButton).selected = true;
            buttons.get(selectedButton).on = true;
            buttons.get(selectedButton).nextDraw = TimeUtils.millis() - MenuButton.DRAW_PAUSE;
        }
        
        //chk for input
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            buttons.get(selectedButton).callback.run();
        }
    }
    
    private void drawTitle() {
        game.getBatch().begin();
        game.getFontLarge().setColor(NeonColors.Orange);
        game.getFontLarge().draw(game.getBatch(), "NEWCOMER NINJA PARTY HATER", 50, Karmen.SCREEN_HEIGHT - 100);
        game.getBatch().end();
    }
    
    private void drawButtons() {
        int paddingX = 100, paddingY = 200;
        int pixhighlight = 2;
        
        /**
         * 1. get screen width/height
         * 2. decide on padding/spacing
         * 3. divide restliche platz durch buttonanzahl
         * 4. draw buttons
         */
        int restwidth = Karmen.SCREEN_WIDTH - 2 * paddingX;
        int boxWidth = restwidth / buttons.size();
        int boxHeight = Karmen.SCREEN_HEIGHT - 2 * paddingY;
        
        //draw white stuff (AKA HIGHLIGHTS)
        drawRectangle(new Vector2(paddingX - pixhighlight, paddingY - pixhighlight), restwidth + 2 * pixhighlight, boxHeight+2 * pixhighlight, Color.WHITE, 0);
        
        //these them bars
        int barWidth = 45;
        int barHeight = boxHeight + barWidth;
        float rotDeg = -12.0f; 
        
        int numbars = buttons.size() - 1;
        
        int wbarWidth = barWidth + 2 * pixhighlight;
        int wbarHeight = barHeight - 2 * pixhighlight;
        int wbarCursor = paddingX + boxWidth;
        
        for (int i = 0; i < numbars; i++) {
            drawRectangle(new Vector2(wbarCursor - (wbarWidth / 2) - pixhighlight, paddingY - ((wbarHeight - boxHeight) / 2)), wbarWidth + 2 * pixhighlight, wbarHeight - 2 * pixhighlight, Color.WHITE, rotDeg);
            wbarCursor += boxWidth;
        }
        
        //draw buttons
        
        int drawCursor = paddingX; //abstand von linkem rand
        
        for (MenuButton menuButton : buttons) {
            Vector2 buttonOrigin = new Vector2(drawCursor, paddingY);
            drawRectangle(buttonOrigin, boxWidth, boxHeight, menuButton.color, 0);
            drawButtonLabel(menuButton, buttonOrigin);
            
            drawCursor += boxWidth;
        }
        
        //draw more black stuff
        int barCursor = paddingX + boxWidth;
        
        for (int i = 0; i < numbars; i++) {
            drawRectangle(new Vector2(barCursor - (barWidth / 2), paddingY - ((barHeight - boxHeight) / 2)), barWidth, barHeight, Color.BLACK, rotDeg);
            barCursor += boxWidth;
        }
    }

    private void drawButtonLabel(MenuButton button, Vector2 buttonOrigin) {
        float labelOffsetX = 50f;
        float labelOffsetY = 50f;
        
        float buttonXPos = buttonOrigin.x + labelOffsetX;
        float buttonYPos = buttonOrigin.y + labelOffsetY;
        
        float underlineOffsetY = 15f;
        float underlinePosY = buttonOrigin.y + underlineOffsetY;
        
        int labelWidth = 20;
        int labelHeight = 4;
        
        game.getBatch().begin();
        
        game.getFontMedium().setColor(NeonColors.Orange);
        game.getFontMedium().draw(game.getBatch(), button.text, buttonXPos, buttonYPos);
        
        game.getBatch().end();
        
        if(button.selected) {
            if(TimeUtils.millis() > button.nextDraw) {
                if(button.on) {
                    button.on = false;
                    button.nextDraw += MenuButton.DRAW_PAUSE;
                }
                else {
                    button.on = true;
                    button.nextDraw += MenuButton.DRAW_PAUSE;
                }
            }
            else {
                if(button.on) {
                    drawRectangle(new Vector2(buttonXPos, underlinePosY), labelWidth, labelHeight, NeonColors.Orange, 0f);
                }
            }
        }
    }
    
    private void drawRectangle(Vector2 origin, int width, int height, Color color, float rotation) {
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color);
        
        shapeRenderer.identity();
        shapeRenderer.translate(origin.x, origin.y, 0);
        shapeRenderer.rotate(0, 0, 1, rotation);
        shapeRenderer.rect(0, 0, width, height);
    
        shapeRenderer.end();
    }
    
    private void drawLamp(float delta) {
        lampAnimationStateTime += delta;
        TextureRegion animationFrame = lampAnimation.getKeyFrame(lampAnimationStateTime, true);
        
        game.getBatch().begin();
        game.getBatch().draw(animationFrame, Karmen.SCREEN_WIDTH - 200, Karmen.SCREEN_HEIGHT / 16, animationFrame.getRegionWidth() * 5, animationFrame.getRegionHeight() * 5);
        game.getBatch().end();
    }
    
    private void drawFloyd(float delta) {
        floydAnimationStateTime += delta;
        
        TextureRegion animationFrame = floydAnimation.getKeyFrame(floydAnimationStateTime, true);
        if(!animationFrame.isFlipX()) {
            animationFrame.flip(true, false);
        }
        
        game.getBatch().begin();
        game.getBatch().draw(animationFrame, Karmen.SCREEN_WIDTH - 125, Karmen.SCREEN_HEIGHT / 15, animationFrame.getRegionWidth() * 5, animationFrame.getRegionHeight() * 5);
        game.getBatch().end();
    }

    @Override
    public void show() {
        game.getSoundService().play(SoundType.LIGHT);
    }
    
    @Override
    public void hide() {}
    
    /*
     * INNER SHTUFF
     */
    
    static class MenuButton {
        private static final long DRAW_PAUSE = 750;
        
        final String text;
        final char key; //key to call this button
        final Runnable callback;
        final Color color;
        
        /* render information */
        boolean selected;
        boolean on;
        long nextDraw = TimeUtils.millis() + DRAW_PAUSE;

        public MenuButton(String text, char key, Color color, boolean selected, Runnable callback) {
            this.text = text;
            this.key = key;
            this.color = color;
            this.on = true;
            this.callback = callback;
            this.selected = selected;
        }
    }
}
