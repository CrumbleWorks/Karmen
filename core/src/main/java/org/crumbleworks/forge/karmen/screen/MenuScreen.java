package org.crumbleworks.forge.karmen.screen;

import java.util.ArrayList;
import java.util.List;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.util.NeonColors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.sun.glass.ui.Menu;

public class MenuScreen implements Screen {
    
    private final Karmen game;
    
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    
    private static ShapeRenderer debugRenderer;
    
    private List<MenuButton> buttons;

    public MenuScreen(Karmen game) {
        this.game = game;
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Karmen.SCREEN_WIDTH, Karmen.SCREEN_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        
        debugRenderer = new ShapeRenderer();
        
        buttons = new ArrayList<MenuButton>() {{
            add(new MenuButton("PLAY", 'P', NeonColors.Yellow, true));
            add(new MenuButton("ABOUT", 'A', NeonColors.Pink, false));
            add(new MenuButton("QUIT", 'Q', NeonColors.Blue, false));
        }};
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

//        drawGrid();
        
        game.getBatch().begin();
        game.getFontLarge().setColor(NeonColors.Orange);
        game.getFontLarge().draw(game.getBatch(), "NEWCOMER NINJA PARTY HATER", 50, Karmen.SCREEN_HEIGHT - 100);
        game.getBatch().end();
        
        drawButtons();
    }
    
    private void drawGrid() {
        int verticalMiddleSectionHeight = Karmen.SCREEN_HEIGHT / 2;
        int topAndBottomSectionHeight = (Karmen.SCREEN_HEIGHT - verticalMiddleSectionHeight) / 2;
        
        int verticalMiddleSectionBottom = topAndBottomSectionHeight;
        int verticalMiddleSectionTop = verticalMiddleSectionBottom + verticalMiddleSectionHeight;
        
        int horizontalSectionWidth = Karmen.SCREEN_WIDTH / 2;
        
        Vector2 verticalMiddleSectionBottomStartVector = new Vector2(0, verticalMiddleSectionBottom);
        Vector2 verticalMiddleSectionBottomEndVector = new Vector2(Karmen.SCREEN_WIDTH, verticalMiddleSectionBottom);
        Vector2 verticalMiddleSectionTopStartVector = new Vector2(0, verticalMiddleSectionTop);
        Vector2 verticalMiddleSectionTopEndVector = new Vector2(Karmen.SCREEN_WIDTH, verticalMiddleSectionTop);
        
        Vector2 horizontalMiddleStartVector = new Vector2(horizontalSectionWidth, 0);
        Vector2 horizontalMiddleEndVector = new Vector2(horizontalSectionWidth, Karmen.SCREEN_HEIGHT);
        
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeType.Line);
        
        debugRenderer.setColor(Color.PINK);
        debugRenderer.line(verticalMiddleSectionBottomStartVector, verticalMiddleSectionBottomEndVector);
        debugRenderer.line(verticalMiddleSectionTopStartVector, verticalMiddleSectionTopEndVector);
        debugRenderer.line(horizontalMiddleStartVector, horizontalMiddleEndVector);
        
        debugRenderer.setColor(Color.OLIVE);
        debugRenderer.line(100, 0, 100, Karmen.SCREEN_HEIGHT);
        debugRenderer.line(300, 0, 300, Karmen.SCREEN_HEIGHT);
        debugRenderer.line(500, 0, 500, Karmen.SCREEN_HEIGHT);
        debugRenderer.line(700, 0, 700, Karmen.SCREEN_HEIGHT);
        
        debugRenderer.end();
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

    private void drawRectangle(Vector2 origin, int width, int height, Color color, float rotation) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color);
        
        shapeRenderer.identity();
        shapeRenderer.translate(origin.x, origin.y, 0);
        shapeRenderer.rotate(0, 0, 1, rotation);
        shapeRenderer.rect(0, 0, width, height);

        shapeRenderer.end();
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
            if(System.currentTimeMillis() > button.nextDraw) {
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
        else {
            drawRectangle(new Vector2(buttonXPos, underlinePosY), labelWidth, labelHeight, NeonColors.Orange, 0f);
        }
        
//        shapeRenderer.line(buttonXPos, buttonYPos - labelYOffset, buttonXPos + 10, buttonYPos - labelYOffset);
//        
//        shapeRenderer.end();
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

    }
    
    /*
     * INNER SHTUFF
     */
    
    static class MenuButton {
        private static final long DRAW_PAUSE = 750;
        
        final String text;
        final char key; //key to call this button
        //TODO add func interface var containing function to call when clicked
        final Color color;
        
        /* render information */
        boolean selected;
        boolean on;
        long nextDraw = System.currentTimeMillis() + DRAW_PAUSE;

        public MenuButton(String text, char key, Color color, boolean selected) {
            this.text = text;
            this.key = key;
            this.color = color;
            this.on = true;
            this.selected = selected;
        }
    }
}
