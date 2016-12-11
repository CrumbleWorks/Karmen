package org.crumbleworks.forge.karmen.character;

import java.util.HashMap;
import java.util.Map;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.util.asset.FloydFrameType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Floyd extends StatefulDoll {

    private static final float animationSpeed = 0.25f;
    private static TextureRegion[] textureRegions;
    
    public Floyd(int x, int y, int width, int height, final Karmen game) {
        super(game,
              new PSV(new Vector2(x, y), new Vector2(width, height), new Vector2(0f, 0f)),
              State.STILL_FRONT,
              initBehaviours(game));
    }
    
    private static Map<State, Behaviour> initBehaviours(Karmen game) {
        //prepTextures
        if(textureRegions == null) {
            textureRegions = game.getTextureLibrary().getFloydRegions();
        }
        
        return new HashMap<State, Behaviour>() {{
            put(State.STILL_FRONT, new FloydStillFront());
            put(State.STILL_RIGHT, new FloydStillRight());
            put(State.STILL_LEFT, new FloydStillLeft());
            put(State.RUN_RIGHT, new FloydRunRight());
            put(State.RUN_LEFT, new FloydRunLeft());
            put(State.BLOCK_FRONT, new FloydBlockFront());
            put(State.BLOCK_LEFT, new FloydBlockLeft());
            put(State.BLOCK_RIGHT, new FloydBlockRight());
            put(State.KICK_RIGHT, new FloydKickRight());
            put(State.KICK_LEFT, new FloydKickLeft());
            put(State.JUMP_FRONT, new FloydJumpFront());
            put(State.JUMP_RIGHT, new FloydJumpRight());
            put(State.JUMP_KICK_RIGHT, new FloydJumpKickRight());
            put(State.ARC_JUMP_RIGHT, new FloydArcJumpRight());
            put(State.ARC_KICK_RIGHT, new FloydArcKickRight());
            put(State.JUMP_LEFT, new FloydJumpLeft());
            put(State.JUMP_KICK_LEFT, new FloydJumpKickLeft());
            put(State.ARC_JUMP_LEFT, new FloydArcJumpLeft());
            put(State.ARC_KICK_LEFT, new FloydArcKickLeft());
        }};
    }

    /* ***********************************************************************
     * BEHAVIOURS
     */
    
    static class FloydStillFront implements Behaviour {
        private static final Animation stillFrontAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()],
                           textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;
        
        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init StillFront");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = stillFrontAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydStillRight implements Behaviour {
        private static final Animation stillSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init StillRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = stillSideAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydStillLeft implements Behaviour {
        private static final Animation stillSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        static {
            for(TextureRegion frame : stillSideAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init StillLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = stillSideAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydRunRight implements Behaviour {
        private static final Animation runAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init RunRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = runAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydRunLeft implements Behaviour {
        private static final Animation runAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        static {
            for(TextureRegion frame : runAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init RunLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = runAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydBlockFront implements Behaviour {
        private static final Animation blockFrontAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init BlockFront");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = blockFrontAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydBlockRight implements Behaviour {
        private static final Animation blockSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init BlockRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = blockSideAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydBlockLeft implements Behaviour {
        private static final Animation blockSideAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_A.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_B.ordinal()]),
             PlayMode.LOOP);
        static {
            for(TextureRegion frame : blockSideAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init BlockLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = blockSideAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydPunchRight implements Behaviour {
        private static final Animation fistAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.FIST_SIDE.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init PunchRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = fistAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydPunchLeft implements Behaviour {
        private static final Animation fistAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.FIST_SIDE.ordinal()]),
                PlayMode.NORMAL);
        static {
            for(TextureRegion frame : fistAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init PunchLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = fistAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydKickRight implements Behaviour {
        private static final Animation kickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init KickRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = kickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydKickLeft implements Behaviour {
        private static final Animation kickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE.ordinal()]),
                PlayMode.NORMAL);
        static {
            for(TextureRegion frame : kickAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init KickLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = kickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydJumpFront implements Behaviour {
        private static final Animation animUp = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation animDown = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpFront");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = animUp.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydJumpRight implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpSideUpAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydJumpKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpKickRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydArcJumpRight implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation jumpSideMoveAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A_AIR.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_B_AIR.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcJumpRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpSideUpAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydArcKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcKickRight");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydJumpLeft implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        static {
            for(TextureRegion frame : jumpSideUpAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }

            for(TextureRegion frame : jumpSideDownAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpSideUpAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydJumpKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        static {
            for(TextureRegion frame : jumpKickAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpKickLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydArcJumpLeft implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_UP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation jumpSideMoveAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_A_AIR.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_B_AIR.ordinal()]),
             PlayMode.LOOP);
        
        static {
            for(TextureRegion frame : jumpSideUpAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }

            for(TextureRegion frame : jumpSideDownAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }

            for(TextureRegion frame : jumpSideMoveAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcJumpLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpSideUpAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
    
    static class FloydArcKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(animationSpeed,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        static {
            for(TextureRegion frame : jumpKickAnimation.getKeyFrames()) {
                frame.flip(true, false);
            }
        }
        
        private float stateTime;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcKickLeft");
            stateTime = 0f;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
        }
    }
}
