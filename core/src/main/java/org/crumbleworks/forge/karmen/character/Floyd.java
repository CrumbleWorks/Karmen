package org.crumbleworks.forge.karmen.character;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;

public class Floyd {

    private final Karmen game;
    
    private Vector2 position;
    private Vector2 size;
    private Vector2 velocity;
    
    public static final float MAX_SPEED = 1f;
    
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 32;
    
    private Sprite sprite;
    private Texture texture;
    private TextureRegion[] textureRegions;
    private FloydFrameType currentFrame;
    
    private boolean grounded;
    private Facing facing;
    private Action action;
    
    private Animation stillFrontAnimation;
    private Animation stillSideAnimation;
    private Animation runAnimation;
    private Animation blockAnimation;
    
//    private Animation walkLeftAnimation;
//    private Animation walkRightAnimation;
    
    private Animation currentAnimation;
    private boolean useAnimation;
    
    private float stateTime;
    private float animationSpeed;
    
    public Floyd(int x, int y, int width, int height, String textureFileName, final Karmen game) {
        this.game = game;
        
        position = new Vector2(x, y);
        size = new Vector2(width, height);
        velocity = new Vector2(0f, 0f);
        
        textureRegions = new TextureRegion[28];
        currentFrame = FloydFrameType.STILL_FRONT_A;
        
        grounded = true;
        facing = Facing.FRONT;
        action = Action.FLOYDING;
        
        animationSpeed = 0.25f;
        
        initTextureRegions(textureFileName);
        initAnimations();
        
        sprite = new Sprite();
        sprite.setTexture(texture);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(size.x, size.y);
        sprite.setOrigin(size.x / 2, size.y / 2);
        
        stateTime = 0f;
    }
    
    private void initTextureRegions(String textureFileName) {
        texture = new Texture(Gdx.files.internal(textureFileName));
        
        for(FloydFrameType type : FloydFrameType.values()) {
            textureRegions[type.ordinal()] = new TextureRegion(texture, type.getX() * FRAME_WIDTH, type.getY() * FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
        }
    }
    
    private void initAnimations() {
        stillFrontAnimation = new Animation(animationSpeed, textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()], textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]);
        stillSideAnimation = new Animation(animationSpeed, textureRegions[FloydFrameType.STILL_SIDE_A.ordinal()], textureRegions[FloydFrameType.STILL_SIDE_B.ordinal()]);
        runAnimation = new Animation(animationSpeed, textureRegions[FloydFrameType.MOVE_SIDE_A.ordinal()], textureRegions[FloydFrameType.MOVE_SIDE_B.ordinal()]);
        blockAnimation = new Animation(animationSpeed, textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()], textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]);
    }
    
    public void update() {
        if(facing == Facing.LEFT || facing == Facing.RIGHT) {
            if(action == Action.FLOYDING) {
                currentAnimation = stillSideAnimation;
            }
            else if(action == Action.RUN) {
                currentAnimation = runAnimation;
            }
            useAnimation = true;
        }
        else if(facing == Facing.FRONT) {
            useAnimation = true;
            if(action == Action.BLOCK) {
                currentAnimation = blockAnimation;
            }
            else {
                currentAnimation = stillFrontAnimation;
            }
        }
    }
    
    public void draw() {
        if(useAnimation) {
            stateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentAnimationFrame = currentAnimation.getKeyFrame(stateTime, true);
            
            if(facing == Facing.LEFT && !currentAnimationFrame.isFlipX()) {
                currentAnimationFrame.flip(true, false);
            }
            else if(facing == Facing.RIGHT && currentAnimationFrame.isFlipX()) {
                currentAnimationFrame.flip(true, false);
            }

            game.getBatch().draw(currentAnimationFrame, position.x, position.y, size.x, size.y);
        }
        else {
            sprite.setRegion(textureRegions[currentFrame.ordinal()]);
            sprite.draw(game.getBatch());
        }
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
