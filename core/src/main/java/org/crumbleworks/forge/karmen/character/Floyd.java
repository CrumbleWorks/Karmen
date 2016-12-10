package org.crumbleworks.forge.karmen.character;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    
    private Animation stillFrontAnimation;
    private Animation stillSideAnimation;
    private Animation jumpFrontUpAnimation;
    private Animation jumpFrontDownAnimation;
    private Animation blockFrontAnimation;
    private Animation runAnimation;
    private Animation kickAnimation;
    private Animation fistAnimation;
    private Animation jumpSideUpAnimation;
    private Animation jumpSideDownAnimation;
    private Animation jumpSideMoveAnimation;
    private Animation jumpKickAnimation;
    private Animation blockSideAnimation;
    
    /* INFO ON FLOYD STATE */
    private Animation currentAnimation;
    private Facing facing;
    
    boolean isRunning = false;
    boolean inAir = false;
    boolean isBlocking = false;
    /* END INFO ON FLOYD STATE */
    
    private float stateTime;
    private float animationSpeed;
    
    public Floyd(int x, int y, int width, int height, String textureFileName, final Karmen game) {
        this.game = game;
        
        position = new Vector2(x, y);
        size = new Vector2(width, height);
        velocity = new Vector2(0f, 0f);
        
        textureRegions = new TextureRegion[28];
        
        animationSpeed = 0.25f;
        
        initTextureRegions(textureFileName);
        initAnimations();
        
        currentAnimation = stillFrontAnimation;
        
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
        stillFrontAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()],
                           textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]),
                PlayMode.LOOP);
        
        stillSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_A.ordinal()],
                           textureRegions[FloydFrameType.STILL_SIDE_B.ordinal()]),
                PlayMode.LOOP);
        
        jumpFrontUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()]),
                PlayMode.NORMAL);
        
        jumpFrontDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        blockFrontAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()],
                           textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]),
                PlayMode.LOOP);
        
        runAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A.ordinal()],
                           textureRegions[FloydFrameType.MOVE_SIDE_B.ordinal()]),
                PlayMode.LOOP);
        
        kickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE.ordinal()]),
                PlayMode.NORMAL);
        
        fistAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.FIST_SIDE.ordinal()]),
                PlayMode.NORMAL);
        
        jumpSideUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()]),
                PlayMode.NORMAL);

        jumpSideDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        jumpSideMoveAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A_AIR.ordinal()],
                           textureRegions[FloydFrameType.MOVE_SIDE_B_AIR.ordinal()]),
                PlayMode.LOOP);
        
        jumpKickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_AIR.ordinal()]),
                PlayMode.NORMAL);
                
        blockSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_A.ordinal()],
                           textureRegions[FloydFrameType.BLOCK_SIDE_B.ordinal()]),
                PlayMode.LOOP);
    }
    
    public void update() {
        
    }
    
    public void draw() {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentAnimationFrame = currentAnimation.getKeyFrame(stateTime);
        
        if(facing == Facing.LEFT && !currentAnimationFrame.isFlipX()) {
            currentAnimationFrame.flip(true, false);
        } else if(facing == Facing.RIGHT && currentAnimationFrame.isFlipX()) {
            currentAnimationFrame.flip(true, false);
        }

        game.getBatch().draw(currentAnimationFrame, position.x, position.y, size.x, size.y);
    }

    /* ***********************************************************************
     * STUFF FLOYD CAN DO IF YOU MAKE HIM DO IT
     */
    
    /* RUNNING */
    public static enum Direction {
        RIGHT, LEFT;
    }
    
    public void startRunning(Direction dir) {
        if(!inAir) {
            isRunning = true;
            currentAnimation = runAnimation;
            if(dir == Direction.RIGHT) {
                facing = Facing.RIGHT;
            } else {
                facing = Facing.LEFT;
            }
        }
    }
    
    public void stopRunning() {
        if(!inAir) {
            isRunning = false;
            if(facing == Facing.FRONT) {
                currentAnimation = stillFrontAnimation;
            } else {
                currentAnimation = stillSideAnimation;
            }
        }
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    /* STARING */
    public void stare() {
        if(inAir) {
            //TODO logic for deciding if jump up/down
            currentAnimation = jumpFrontUpAnimation;
        } else if(!isBlocking) {
            return;
        } else {
            currentAnimation = stillFrontAnimation;
        }
        facing = Facing.FRONT;
    }
    
    /* JUMPING */
    public void jump() {
        if(!inAir && !isBlocking) {
            //TODO FALLING LOGIC
            currentAnimation = jumpFrontUpAnimation;
            inAir = true;
        }
    }
    
    public boolean inAir() {
        return inAir;
    }
    
    /* PUNCHING */
    public void punch() {
        if(!inAir) {
            currentAnimation = fistAnimation;
        }
    }
    
    /* KICKING */
    public void kick() {
        if(inAir) {
            currentAnimation = jumpKickAnimation;
        } else {
            currentAnimation = kickAnimation;
        }
    }
    
    /* BLOCKING */
    public void startBlocking() {
        if(!inAir) {
            if(facing == Facing.FRONT) {
                currentAnimation = blockFrontAnimation;
            } else {
                currentAnimation = blockSideAnimation;
            }
            isBlocking = true;
        }
    }
    
    public void stopBlocking() {
        if(facing == Facing.FRONT) {
            currentAnimation = stillFrontAnimation;
        } else {
            currentAnimation = stillSideAnimation;
        }
        isBlocking = false;
    }
    
    public boolean isBlocking() {
        return isBlocking;
    }
}
