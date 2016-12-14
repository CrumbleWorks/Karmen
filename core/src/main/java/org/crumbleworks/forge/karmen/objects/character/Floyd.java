package org.crumbleworks.forge.karmen.objects.character;

import java.util.HashMap;
import java.util.Map;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.objects.PSV;
import org.crumbleworks.forge.karmen.objects.StatefulDoll;
import org.crumbleworks.forge.karmen.util.AnimationConstants;
import org.crumbleworks.forge.karmen.util.Calc;
import org.crumbleworks.forge.karmen.util.PhysicsConstants;
import org.crumbleworks.forge.karmen.util.asset.FloydFrameType;
import org.crumbleworks.forge.karmen.util.asset.music.MusicType;
import org.crumbleworks.forge.karmen.util.asset.sound.SoundType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Floyd extends StatefulDoll {

    private static final String FLOYD_TAG = "FLOYD";
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
    
    static abstract class BaseRendering implements Behaviour {
        private final Animation animation;
        private final String debugName;

        private float stateTime;
        
        public BaseRendering(Animation animation, String debugName) {
            this.animation = animation;
            this.debugName = debugName;
        }
        
        @Override
        public final void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init " + debugName);
            stateTime = 0f;
            
            init(doll, previousState);
        }
        
        protected abstract void init(StatefulDoll doll, State previousState);

        @Override
        public final void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            
            TextureRegion currentAnimationFrame = animation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            update(doll, delta);
        }
        
        public abstract void update(StatefulDoll doll, float delta);
        
        @Override
        public void finish(StatefulDoll doll) {
        }
    }
    
    static abstract class BaseSwitchRendering implements Behaviour {
        private final Animation[] animation;
        private int animationIndex;
        private final String debugName;

        private float stateTime;
        
        public BaseSwitchRendering(Animation animationA, Animation animationB, String debugName) {
            this.animation = new Animation[]{animationA, animationB};
            this.debugName = debugName;
        }
        
        protected final void switchAnimations() {
            this.animationIndex = Math.abs(animationIndex - 1);
        }
        
        @Override
        public final void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init " + debugName);
            stateTime = 0f;
            animationIndex = 0;
            
            init(doll, previousState);
        }
        
        protected abstract void init(StatefulDoll doll, State previousState);

        @Override
        public final void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            
            TextureRegion currentAnimationFrame = animation[animationIndex].getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            update(doll, delta);
        }
        
        public abstract void update(StatefulDoll doll, float delta);
        
        @Override
        public void finish(StatefulDoll doll) {
        }
    }
    
    /* ***********************************************************************
     * STILL
     */
    
    static abstract class FloydStill extends BaseRendering {
        private final float drag;
        
        private State previousState;
        
        FloydStill(Animation animation, String debugName, float drag) {
            super(animation, debugName);
            this.drag = drag;
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            this.previousState = previousState;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            float xVelo = doll.body().getLinearVelocity().x;
            if(xVelo > 0.0f) { //movement nach rechts
                doll.body().applyLinearImpulse(
                        new Vector2(-drag, 0.0f),
                        doll.body().getPosition(),
                        true);
            } else if(xVelo < 0.0f) { //movement nach links
                doll.body().applyLinearImpulse(
                        new Vector2(drag, 0.0f),
                        doll.body().getPosition(),
                        true);
            }
        }
    }
    
    static class FloydStillFront extends FloydStill {
        private static final Animation stillFrontAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()],
                           textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]),
             PlayMode.LOOP);
        
        public FloydStillFront() {
            super(stillFrontAnimation, "StillFront", PhysicsConstants.STANDING_DRAG * 2);
        }
    }
    
    static class FloydStillRight extends FloydStill {
        private static final Animation stillSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_RB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydStillRight() {
            super(stillSideAnimation, "StillRight", PhysicsConstants.STANDING_DRAG);
        }
    }
    
    static class FloydStillLeft extends FloydStill {
        private static final Animation stillSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.STILL_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.STILL_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydStillLeft() {
            super(stillSideAnimation, "StillLeft", PhysicsConstants.STANDING_DRAG);
        }
    }
    
    /* ***********************************************************************
     * RUNNING
     */
    
    static class FloydRunRight extends BaseRendering {
        private static final Animation runAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_RB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydRunRight() {
            super(runAnimation, "RunRight");
        }
        
        @Override
        protected void init(StatefulDoll doll, State previousState) {
        }
        
        @Override
        public void update(StatefulDoll doll, float delta) {
            if(doll.body().getLinearVelocity().x < PhysicsConstants.RUNNING_SPEED_MAX) {
            doll.body().applyLinearImpulse(
                    new Vector2(PhysicsConstants.RUNNING_SPEED_ACCELERATION, 0.0f),
                    doll.body().getPosition(),
                    true);
            }
        }
    }
    
    static class FloydRunLeft extends BaseRendering {
        private static final Animation runAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.MOVE_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.MOVE_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydRunLeft() {
            super(runAnimation, "RunRight");
        }
        
        @Override
        protected void init(StatefulDoll doll, State previousState) {
        }
        
        @Override
        public void update(StatefulDoll doll, float delta) {
            if(doll.body().getLinearVelocity().x > -PhysicsConstants.RUNNING_SPEED_MAX) {
                doll.body().applyLinearImpulse(
                        new Vector2(-PhysicsConstants.RUNNING_SPEED_ACCELERATION, 0.0f),
                        doll.body().getPosition(),
                        true);
            }
        }
    }
    
    /* ***********************************************************************
     * BLOCK
     */
    
    static abstract class FloydBlock extends BaseRendering {
        private final float drag;
        
        private State previousState;
        
        FloydBlock(Animation animation, String debugName, float drag) {
            super(animation, debugName);
            this.drag = drag;
        }

        private long delayAcc;

        @Override
        public void init(StatefulDoll doll, State previousState) {
            this.previousState = previousState;
            delayAcc = 0;
            
            doll.game().getSoundService().play(SoundType.BLOCK, true);
        }
         
        @Override
        public void update(StatefulDoll doll, float delta) {
            delayAcc += Calc.gdxDeltaToMillis(delta);
            if(delayAcc >= PhysicsConstants.BLOCK_DELAY_MS) {
                doll.body().setLinearVelocity(0.0f, 0.0f);
            }
            
            //block = (almost)instastop!
            float xVelo = doll.body().getLinearVelocity().x;
            if(xVelo > 0.0f) { //movement nach rechts
                doll.body().applyLinearImpulse(
                        new Vector2(-drag, 0.0f),
                        doll.body().getPosition(),
                        true);
            } else if(xVelo < 0.0f) { //movement nach links
                doll.body().applyLinearImpulse(
                        new Vector2(drag, 0.0f),
                        doll.body().getPosition(),
                        true);
            }
        }
         
        @Override
        public void finish(StatefulDoll doll) {
            doll.game().getSoundService().stop();
        }
     }
    
    static class FloydBlockFront extends FloydBlock {
        private static final Animation blockFrontAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]),
             PlayMode.LOOP);
        
        public FloydBlockFront() {
            super(blockFrontAnimation, "BlockFront", PhysicsConstants.BLOCKING_DRAG * 4);
        }
    }
    
    static class FloydBlockRight extends FloydBlock {
        private static final Animation blockSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_RA.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_RB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydBlockRight() {
            super(blockSideAnimation, "BlockRight", PhysicsConstants.BLOCKING_DRAG);
        }
    }
    
    static class FloydBlockLeft extends FloydBlock {
        private static final Animation blockSideAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_LA.ordinal()],
                        textureRegions[FloydFrameType.BLOCK_SIDE_LB.ordinal()]),
             PlayMode.LOOP);
        
        public FloydBlockLeft() {
            super(blockSideAnimation, "BlockLeft", PhysicsConstants.BLOCKING_DRAG);
        }
    }
    
    /* ***********************************************************************
     * PUNCH
     */
    
    static abstract class FloydPunch extends BaseRendering {
        private long returnTime;
        
        private State previousState;
        
        FloydPunch(FloydFrameType animation, String debugName) {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[animation.ordinal()]),
                    PlayMode.NORMAL),
                    debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            this.previousState = previousState;
            returnTime = AnimationConstants.DUR_MS_PUNCH;
            doll.game().getSoundService().play(SoundType.PUNCH);
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
                Gdx.app.debug(FLOYD_TAG, "> returning to " + previousState.name());
                doll.done();
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
    
    static abstract class FloydKick extends BaseRendering {
        private long returnTime;
        private boolean soundPlayed;
        
        private State previousState;
        
        FloydKick(FloydFrameType animation, String debugName) {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[animation.ordinal()]),
                    PlayMode.NORMAL),
                    debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            this.previousState = previousState;
            returnTime = AnimationConstants.DUR_MS_KICK;
            soundPlayed = false;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
                Gdx.app.debug(FLOYD_TAG, "> returning to " + previousState.name());
                doll.done();
            }
            
            if(!soundPlayed) {
                doll.game().getSoundService().play(SoundType.KICK);
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
    
    /* ***********************************************************************
     * JUMP
     */
    
    static abstract class FloydJump extends BaseSwitchRendering {
        private State previousState;
        private boolean hasSwitched;
        private boolean jumpFlag;
        
        public FloydJump(Animation upAnimation, Animation downAnimation, String debugName) {
            super(upAnimation, downAnimation, debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            this.previousState = previousState;
            
            hasSwitched = false;
            jumpFlag = false;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            if(!jumpFlag) {
//                doll.body().setLinearVelocity(
//                        new Vector2(0.0f, PhysicsConstants.JUMP_FORCE));
                doll.body().applyLinearImpulse(
                        new Vector2(0.0f, PhysicsConstants.JUMP_FORCE),
                        doll.body().getPosition(),
                        true);
                jumpFlag = true;
            }
            
            if(!hasSwitched && doll.body().getLinearVelocity().y <= 0.0f) { //aka going down again
                switchAnimations();
                hasSwitched = true;
            }
        }
    }
    
    static class FloydJumpFront extends FloydJump {
        private static final Animation animUp = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation animDown = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]),
                PlayMode.NORMAL);
        
        public FloydJumpFront() {
            super(animUp, animDown, "FrontJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
//            doll.switchState(State.STILL_FRONT);
        }
    }
    
    static class FloydJumpRight extends FloydJump {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RUP.ordinal()]),
                PlayMode.NORMAL);

        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        public FloydJumpRight() {
            super(jumpSideUpAnimation, jumpSideDownAnimation, "RightJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
//            doll.switchState(State.STILL_RIGHT);
        }
    }
    
    static class FloydJumpLeft extends FloydJump {
        private static final Animation jumpSideUpAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LUP.ordinal()]),
                PlayMode.NORMAL);
        
        private static final Animation jumpSideDownAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LDOWN.ordinal()]),
                PlayMode.NORMAL);
        
        public FloydJumpLeft() {
            super(jumpSideUpAnimation, jumpSideDownAnimation, "LeftJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
//            doll.switchState(State.STILL_LEFT);
        }
    }
    
    /* ***********************************************************************
     * JUMPKICK
     */
    
    static class FloydJumpKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_R_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init JumpKickRight");
            stateTime = 0f;
            soundPlayed = false;
        }

        @Override
        public void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            if(!soundPlayed) {
                doll.game().getSoundService().play(SoundType.KICK);
                soundPlayed = true;
            }
        }

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
        }
    }
    
    static class FloydJumpKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_L_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;
        
        @Override
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init JumpKickLeft");
            stateTime = 0f;
            soundPlayed = false;
        }
        
        @Override
        public void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            
            
            if(!soundPlayed) {
                doll.game().getSoundService().play(SoundType.KICK);
                soundPlayed = true;
            }
        }

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
        }
    }
    
    /* ***********************************************************************
     * ARC
     */
    
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
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init ArcJumpRight");
            stateTime = 0f;
            soundPlayed = false;
        }

        @Override
        public void _update(StatefulDoll doll, float delta) {
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

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
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
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init ArcJumpLeft");
            stateTime = 0f;
            soundPlayed = false;
        }
        
        @Override
        public void _update(StatefulDoll doll, float delta) {
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

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
        }
    }
    
    /* ***********************************************************************
     * ARC KICK
     */
    
    static class FloydArcKickRight implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_R_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init ArcKickRight");
            stateTime = 0f;
            soundPlayed = false;
        }

        @Override
        public void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);

            
            if(!soundPlayed) {
                doll.game().getSoundService().play(SoundType.KICK);
                soundPlayed = true;
            }
        }

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
        }
    }
    
    static class FloydArcKickLeft implements Behaviour {
        private static final Animation jumpKickAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(textureRegions[FloydFrameType.KICK_SIDE_L_AIR.ordinal()]),
                PlayMode.NORMAL);
        
        private float stateTime;
        private boolean soundPlayed;

        @Override
        public void _init(StatefulDoll doll, State previousState) {
            Gdx.app.debug(FLOYD_TAG, "init ArcKickLeft");
            stateTime = 0f;
            soundPlayed = false;
        }

        @Override
        public void _update(StatefulDoll doll, float delta) {
            stateTime += delta;
            TextureRegion currentAnimationFrame = jumpKickAnimation.getKeyFrame(stateTime);
            doll.game().getBatch().draw(
                    currentAnimationFrame,
                    doll.psv().position.x,
                    doll.psv().position.y,
                    doll.psv().size.x,
                    doll.psv().size.y);
            
            if(!soundPlayed) {
                doll.game().getSoundService().play(SoundType.KICK);
                soundPlayed = true;
            }
        }

        @Override
        public void finish(StatefulDoll doll) {
            // TODO Auto-generated method stub
            
        }
    }
}
