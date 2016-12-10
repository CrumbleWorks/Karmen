package org.crumbleworks.forge.karmen.character;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
    
    private boolean grounded;
    private Facing facing;
    private Action action;
    
    private boolean fistDone;
    private boolean kickDone;
    
    private Animation stillFrontAnimation;
    private Animation stillSideAnimation;
    private Animation runAnimation;
    private Animation jumpFrontAnimation;
    private Animation jumpSideAnimation;
    private Animation blockFrontAnimation;
    private Animation blockSideAnimation;
    private Animation kickAnimation;
    private Animation fistAnimation;
    
//    private Animation walkLeftAnimation;
//    private Animation walkRightAnimation;
    
    private Animation currentAnimation;
    
    private float stateTime;
    private float animationSpeed;
    
    public Floyd(int x, int y, int width, int height, String textureFileName, final Karmen game) {
        this.game = game;
        
        position = new Vector2(x, y);
        size = new Vector2(width, height);
        velocity = new Vector2(0f, 0f);
        
        textureRegions = new TextureRegion[28];
        
        grounded = true;
        facing = Facing.FRONT;
        action = Action.FLOYDING;
        
        fistDone = true;
        kickDone = true;
        
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
        stillFrontAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()], textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]), PlayMode.LOOP);
        stillSideAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.STILL_SIDE_A.ordinal()], textureRegions[FloydFrameType.STILL_SIDE_B.ordinal()]), PlayMode.LOOP);
        runAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A.ordinal()], textureRegions[FloydFrameType.MOVE_SIDE_B.ordinal()]), PlayMode.LOOP);
        jumpFrontAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()], textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]), PlayMode.NORMAL);
        jumpSideAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()], textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]), PlayMode.NORMAL);
        blockFrontAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()], textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]), PlayMode.LOOP);
        blockSideAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_A.ordinal()], textureRegions[FloydFrameType.BLOCK_SIDE_B.ordinal()]), PlayMode.LOOP);
        kickAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.KICK_SIDE.ordinal()]), PlayMode.NORMAL);
        fistAnimation = new Animation(animationSpeed, Array.with(textureRegions[FloydFrameType.FIST_SIDE.ordinal()]), PlayMode.NORMAL);
    }
    
    public void update() {
        if(facing == Facing.LEFT || facing == Facing.RIGHT) {
            if(action == Action.RUN) {
                currentAnimation = runAnimation;
            }
            else if(action == Action.BLOCK) {
                currentAnimation = blockSideAnimation;
            }
            else if(action == Action.KICK && !kickDone) {
                currentAnimation = kickAnimation;
                kickDone = true;
            }
            else if(action == Action.FIST && !fistDone) {
                currentAnimation = fistAnimation;
                fistDone = true;
            }
            else if(!grounded) {
                currentAnimation = jumpSideAnimation;
            }
            else {
                currentAnimation = stillSideAnimation;
            }
        }
        else if(facing == Facing.FRONT) {
            if(action == Action.FLOYDING) {
                currentAnimation = stillFrontAnimation;
            }
            else if(action == Action.BLOCK) {
                currentAnimation = blockFrontAnimation;
            }
            else if(!grounded) {
                currentAnimation = jumpFrontAnimation;
            }
        }
    }
    
    public void draw() {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimationFrame = currentAnimation.getKeyFrame(stateTime);
        
        if(facing == Facing.LEFT && !currentAnimationFrame.isFlipX()) {
            currentAnimationFrame.flip(true, false);
        }
        else if(facing == Facing.RIGHT && currentAnimationFrame.isFlipX()) {
            currentAnimationFrame.flip(true, false);
        }

        game.getBatch().draw(currentAnimationFrame, position.x, position.y, size.x, size.y);
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

    public boolean isFistDone() {
        return fistDone;
    }
    
    public void setFistDone(boolean fistDone) {
        this.fistDone = fistDone;
    }
    
    public boolean isKickDone() {
        return kickDone;
    }

    public void setKickDone(boolean kickDone) {
        this.kickDone = kickDone;
    }
    
}
