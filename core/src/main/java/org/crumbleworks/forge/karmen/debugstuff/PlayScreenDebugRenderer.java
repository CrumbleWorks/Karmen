package org.crumbleworks.forge.karmen.debugstuff;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class PlayScreenDebugRenderer {
    
    private static final int HELPER_LINE_OFFSET = 20;

    private ShapeRenderer renderer;
    
    public PlayScreenDebugRenderer() {
        renderer = new ShapeRenderer();
    }
    
    public void drawGrid(Camera camera) {
        renderer.setProjectionMatrix(camera.combined);
        
        drawBoundaryLines();
        drawStageHelperLines();
        drawDanceFloorBoundaries();
        drawDummyFloyd();
    }
    
    private float getBottomBoundaryY() {
        return 20;
    }
    
    private float getTopBoundaryY() {
        System.out.println(Karmen.SCREEN_HEIGHT);
        System.out.println(Karmen.SCREEN_WIDTH);
        
        return Karmen.SCREEN_HEIGHT - 20;
    }
    
    private float getCenterBoundaryY() {
        return Karmen.SCREEN_HEIGHT / 2;
    }
    
    private float getLeftHelperLineX() {
        return Karmen.SCREEN_WIDTH / 8;
    }
    
    private float getRightHelperLineX() {
        return (Karmen.SCREEN_WIDTH / 8) * 7;
    }
    
    private float getLeftOuterHelperLineX() {
        return getLeftHelperLineX() - HELPER_LINE_OFFSET;
    }
    
    private float getLeftInnerHelperLineX() {
        return getLeftHelperLineX() + HELPER_LINE_OFFSET;
    }
    
    private float getRightOuterHelperLineX() {
        return getRightHelperLineX() + HELPER_LINE_OFFSET;
    }
    
    private float getRightInnerHelperLineX() {
        return getRightHelperLineX() - HELPER_LINE_OFFSET;
    }
    
    private float getDanceFloorTopY() {
        return getCenterBoundaryY() / 1.5f;
    }
    
    private void drawBoundaryLines() {
        renderer.setColor(Color.BLACK);
        renderer.begin(ShapeType.Line);
        
        drawLine(0, getBottomBoundaryY(), Karmen.SCREEN_WIDTH, getBottomBoundaryY());
        drawLine(0, getTopBoundaryY(), Karmen.SCREEN_WIDTH, getTopBoundaryY());
        
        renderer.end();
    }
    
    private void drawStageHelperLines() {
        renderer.setColor(Color.FIREBRICK);
        renderer.begin(ShapeType.Line);
        
        drawLine(getLeftOuterHelperLineX(), getBottomBoundaryY(), getLeftOuterHelperLineX(), getTopBoundaryY());
        drawLine(getLeftInnerHelperLineX(), getBottomBoundaryY(), getLeftInnerHelperLineX(), getTopBoundaryY());
        
        drawLine(getRightInnerHelperLineX(), getBottomBoundaryY(), getRightInnerHelperLineX(), getTopBoundaryY());
        drawLine(getRightOuterHelperLineX(), getBottomBoundaryY(), getRightOuterHelperLineX(), getTopBoundaryY());
        
        drawLine(getLeftInnerHelperLineX(), getCenterBoundaryY(), getRightInnerHelperLineX(), getCenterBoundaryY());
        
        renderer.end();
    }
    
    private void drawDanceFloorBoundaries() {
        renderer.setColor(Color.PURPLE);
        renderer.begin(ShapeType.Line);
        
        drawLine(getLeftOuterHelperLineX(), getBottomBoundaryY(), getLeftInnerHelperLineX(), getDanceFloorTopY());
        drawLine(getRightOuterHelperLineX(), getBottomBoundaryY(), getRightInnerHelperLineX(), getDanceFloorTopY());
        drawLine(getLeftInnerHelperLineX(), getDanceFloorTopY(), getRightInnerHelperLineX(), getDanceFloorTopY());
        drawLine(getLeftOuterHelperLineX(), getBottomBoundaryY(), getRightOuterHelperLineX(), getBottomBoundaryY());
        
        renderer.end();
    }
    
    private void drawDummyFloyd() {
        int width = 64;
        int height = 128;
        
        int middleOfScreenX = Karmen.SCREEN_WIDTH / 2;
        
        float floydLeftX = middleOfScreenX - (width / 2);
        float floydRightX = middleOfScreenX + (width / 2);
        float floydBottomY = getBottomBoundaryY();
        float floydTopY = floydBottomY + height;
        
        renderer.setColor(Color.CYAN);
        renderer.begin(ShapeType.Line);
        
        drawRectangle(floydLeftX, floydBottomY, floydRightX, floydTopY);
        
        renderer.end();
    }
    
    private void drawLine(float x1, float y1, float x2, float y2) {
        renderer.line(x1, y1, x2, y2);
    }
    
    private void drawRectangle(float x1, float y1, float x2, float y2) {
        renderer.rect(x1, y1, x2 - x1, y2 - y1);
    }

}
