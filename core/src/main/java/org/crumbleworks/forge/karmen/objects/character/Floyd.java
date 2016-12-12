package org.crumbleworks.forge.karmen.objects.character;

import java.util.HashMap;
import java.util.Map;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.objects.PSV;
import org.crumbleworks.forge.karmen.objects.StatefulDoll;
import org.crumbleworks.forge.karmen.util.AnimationConstants;
import org.crumbleworks.forge.karmen.util.Calc;
import org.crumbleworks.forge.karmen.util.asset.FloydFrameType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Floyd extends StatefulDoll {

    private static TextureRegion[] textureRegions;
    
    public Floyd(int x, int y, int width, int height, final Karmen game, final World world) {
        super(game,
              world,
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
            put(State.PUNCH_RIGHT, new FloydPunchRight());
            put(State.PUNCH_LEFT, new FloydPunchLeft());
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
        private static final Animation stillFrontAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
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
        private static final Animation stillSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_RB.ordinal()]),
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
        private static final Animation stillSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
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
        private static final Animation runAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_RB.ordinal()]),
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
        private static final Animation runAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
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
        private static final Animation blockFrontAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
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
            
            if(!doll.game().getSoundLibrary().getBlockSound().isPlaying()) {
                doll.game().getSoundLibrary().getBlockSound().play();
            }
        }
    }
    
    static class FloydBlockRight implements Behaviour {
        private static final Animation blockSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_RB.ordinal()]),
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
            
            if(!doll.game().getSoundLibrary().getBlockSound().isPlaying()) {
                doll.game().getSoundLibrary().getBlockSound().play();
            }
        }
    }
    
    static class FloydBlockLeft implements Behaviour {
        private static final Animation blockSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
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
            
            if(!doll.game().getSoundLibrary().getBlockSound().isPlaying()) {
                doll.game().getSoundLibrary().getBlockSound().play();
            }
        }
    }
    
    /* ***********************************************************************
     * PUNCH
     */
    
    static abstract class FloydPunch implements Behaviour {
        private final Animation punchAnimation;
        private final String debugName;
        
        private long returnTime;
        private float stateTime;
        private boolean soundPlayed;
        
        private State previousState;
        
        FloydPunch(FloydFrameType animation, String debugName) {
            punchAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[animation.ordinal()]),
                    PlayMode.NORMAL);
            this.debugName = debugName;
        }

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init " + debugName);
            
            this.previousState = previousState;
            stateTime = 0f;
            returnTime = AnimationConstants.DUR_MS_PUNCH;
            soundPlayed = false;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = punchAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            //logic
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
                Gdx.app.debug("FLOYD", "> returning to " + previousState.name());
                doll.set(previousState);
            }
            
            if(!soundPlayed) {
                doll.game().getSoundLibrary().getPunchSound().play();
                soundPlayed = true;
            }
        }
    }
    
    static class FloydPunchRight extends FloydPunch {

        FloydPunchRight() {
            super(FloydFrameType.PUNCH_SIDE_R, "PunchRight");
        }
    }
    
    static class FloydPunchLeft extends FloydPunch {
        
        public FloydPunchLeft() {
            super(FloydFrameType.PUNCH_SIDE_L, "PunchLeft");
        }
    }
    
    /* ***********************************************************************
     * KICK
     */
    
    static abstract class FloydKick implements Behaviour {
        private final Animation kickAnimation;
        private final String debugName;
        
        private long returnTime;
        private float stateTime;
        private boolean soundPlayed;
        
        private State previousState;
        
        FloydKick(FloydFrameType animation, String debugName) {
            kickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[animation.ordinal()]),
                    PlayMode.NORMAL);
            this.debugName = debugName;
        }

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init " + debugName);
            
            this.previousState = previousState;
            stateTime = 0f;
            returnTime = AnimationConstants.DUR_MS_KICK;
            soundPlayed = false;
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
            
            //logic
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
                Gdx.app.debug("FLOYD", "> returning to " + previousState.name());
                doll.set(previousState);
            }
            
            if(!soundPlayed) {
                doll.game().getSoundLibrary().getKickSound().play();
                soundPlayed = true;
            }
        }
    }
    
    static class FloydKickRight extends FloydKick {
        
        FloydKickRight() {
            super(FloydFrameType.KICK_SIDE_R, "KickRight");
        }
    }
    
    static class FloydKickLeft extends FloydKick {
        
        FloydKickLeft() {
            super(FloydFrameType.KICK_SIDE_L, "KickLeft");
        }
    }
    
    static class FloydJumpFront implements Behaviour {
        private static final Animation animUp = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation animDown = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpFront");
            stateTime = 0f;
            soundPlayed = false;
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
            
            if(!soundPlayed) {
                //doll.game().getSoundLibrary().getJumpSound().play(); TODO uncomment
                soundPlayed = true;
            }
        }
    }
    
    static class FloydJumpRight implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RUP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpRight");
            stateTime = 0f;
            soundPlayed = false;
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
            
            if(!soundPlayed) {
                //doll.game().getSoundLibrary().getJumpSound().play(); TODO uncomment
                soundPlayed = true;
            }
        }
    }
    
    static class FloydJumpKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_R_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpKickRight");
            stateTime = 0f;
            soundPlayed = false;
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
            

            
                      if(!soundPlayed) {
                          doll.game().getSoundLibrary().getKickSound().play();
                          soundPlayed = true;
                      }
        }
    }
    
    static class FloydArcJumpRight implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RUP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation jumpSideMoveAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_RA_AIR.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_RB_AIR.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcJumpRight");
            stateTime = 0f;
            soundPlayed = false;
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
            
            if(!soundPlayed) {
                //doll.game().getSoundLibrary().getJumpSound().play(); TODO uncomment
                soundPlayed = true;
            }
        }
    }
    
    static class FloydArcKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_R_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcKickRight");
            stateTime = 0f;
            soundPlayed = false;
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

            
                      if(!soundPlayed) {
                          doll.game().getSoundLibrary().getKickSound().play();
                          soundPlayed = true;
                      }
        }
    }
    
    static class FloydJumpLeft implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LUP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpLeft");
            stateTime = 0f;
            soundPlayed = false;
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
            
            if(!soundPlayed) {
                //doll.game().getSoundLibrary().getJumpSound().play(); TODO uncomment
                soundPlayed = true;
            }
        }
    }
    
    static class FloydJumpKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_L_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init JumpKickLeft");
            stateTime = 0f;
            soundPlayed = false;
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
            

            
                      if(!soundPlayed) {
                          doll.game().getSoundLibrary().getKickSound().play();
                          soundPlayed = true;
                      }
        }
    }
    
    static class FloydArcJumpLeft implements Behaviour {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LUP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation jumpSideMoveAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_LA_AIR.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_LB_AIR.ordinal()]),
             PlayMode.LOOP);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcJumpLeft");
            stateTime = 0f;
            soundPlayed = false;
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
            
            if(!soundPlayed) {
                //doll.game().getSoundLibrary().getJumpSound().play(); TODO uncomment
                soundPlayed = true;
            }
        }
    }
    
    static class FloydArcKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_L_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void init(State previousState) {
            Gdx.app.debug("FLOYD", "init ArcKickLeft");
            stateTime = 0f;
            soundPlayed = false;
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
            

            
                      if(!soundPlayed) {
                          doll.game().getSoundLibrary().getKickSound().play();
                          soundPlayed = true;
                      }
        }
    }
}
