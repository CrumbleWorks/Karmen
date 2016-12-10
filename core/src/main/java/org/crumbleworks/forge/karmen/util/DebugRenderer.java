package org.crumbleworks.forge.karmen.util;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class DebugRenderer {

    private ShapeRenderer renderer;
    
    public DebugRenderer() {
        renderer = new ShapeRenderer();
    }
    
    public void drawGrid(Camera camera) {
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
        
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeType.Line);
        
        renderer.setColor(Color.PINK);
        renderer.line(verticalMiddleSectionBottomStartVector, verticalMiddleSectionBottomEndVector);
        renderer.line(verticalMiddleSectionTopStartVector, verticalMiddleSectionTopEndVector);
        renderer.line(horizontalMiddleStartVector, horizontalMiddleEndVector);
        
        renderer.setColor(Color.OLIVE);
        renderer.line(100, 0, 100, Karmen.SCREEN_HEIGHT);
        renderer.line(300, 0, 300, Karmen.SCREEN_HEIGHT);
        renderer.line(500, 0, 500, Karmen.SCREEN_HEIGHT);
        renderer.line(700, 0, 700, Karmen.SCREEN_HEIGHT);
        
        renderer.end();
    }

}
