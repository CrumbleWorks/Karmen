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
import org.crumbleworks.forge.karmen.util.asset.sound.SoundType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sun.accessibility.internal.resources.accessibility;

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
        
        FloydStill(Animation animation, String debugName, float drag) {
            super(animation, debugName);
            this.drag = drag;
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
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
        public FloydStillFront() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.STILL_FRONT_A.ordinal()],
                               textureRegions[FloydFrameType.STILL_FRONT_B.ordinal()]),
                    PlayMode.LOOP),
                    "StillFront",
                    PhysicsConstants.STANDING_DRAG * 2);
        }
    }
    
    static class FloydStillRight extends FloydStill {
        public FloydStillRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.STILL_SIDE_RA.ordinal()],
                               textureRegions[FloydFrameType.STILL_SIDE_RB.ordinal()]),
                    PlayMode.LOOP),
                    "StillRight",
                    PhysicsConstants.STANDING_DRAG);
        }
    }
    
    static class FloydStillLeft extends FloydStill {
        public FloydStillLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.STILL_SIDE_LA.ordinal()],
                               textureRegions[FloydFrameType.STILL_SIDE_LB.ordinal()]),
                    PlayMode.LOOP),
                    "StillLeft",
                    PhysicsConstants.STANDING_DRAG);
        }
    }
    
    /* ***********************************************************************
     * RUNNING
     */
    
    static class FloydRun extends BaseRendering {
        private final float acceleration;
        
        public FloydRun(Animation runAnimation, String debugName, float acceleration) {
            super(runAnimation, debugName);
            
            this.acceleration = acceleration;
        }
        
        @Override
        protected void init(StatefulDoll doll, State previousState) {
        }
        
        @Override
        public void update(StatefulDoll doll, float delta) {
            if(doll.body().getLinearVelocity().x < PhysicsConstants.RUNNING_SPEED_MAX) {
                doll.body().applyLinearImpulse(
                        new Vector2(acceleration, 0.0f),
                        doll.body().getPosition(),
                        true);
            }
        }
    }

    static class FloydRunRight extends FloydRun {
        public FloydRunRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.MOVE_SIDE_RA.ordinal()],
                               textureRegions[FloydFrameType.MOVE_SIDE_RB.ordinal()]),
                    PlayMode.LOOP),
                    "RunRight",
                    PhysicsConstants.RUNNING_SPEED_ACCELERATION);
        }
    }
    
    static class FloydRunLeft extends FloydRun {
        public FloydRunLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.MOVE_SIDE_LA.ordinal()],
                               textureRegions[FloydFrameType.MOVE_SIDE_LB.ordinal()]),
                    PlayMode.LOOP),
                    "RunRight",
                    -PhysicsConstants.RUNNING_SPEED_ACCELERATION); //note the minus!!
        }
    }
    
    /* ***********************************************************************
     * BLOCK
     */
    
    static abstract class FloydBlock extends BaseRendering {
        private final float drag;
        
        FloydBlock(Animation animation, String debugName, float drag) {
            super(animation, debugName);
            this.drag = drag;
        }

        private long delayAcc;

        @Override
        public void init(StatefulDoll doll, State previousState) {
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
        public FloydBlockFront() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.BLOCK_FRONT_A.ordinal()],
                               textureRegions[FloydFrameType.BLOCK_FRONT_B.ordinal()]),
                    PlayMode.LOOP),
                    "BlockFront",
                    PhysicsConstants.BLOCKING_DRAG * 4);
        }
    }
    
    static class FloydBlockRight extends FloydBlock {
        public FloydBlockRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_RA.ordinal()],
                               textureRegions[FloydFrameType.BLOCK_SIDE_RB.ordinal()]),
                    PlayMode.LOOP),
                    "BlockRight",
                    PhysicsConstants.BLOCKING_DRAG);
        }
    }
    
    static class FloydBlockLeft extends FloydBlock {
        public FloydBlockLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.BLOCK_SIDE_LA.ordinal()],
                               textureRegions[FloydFrameType.BLOCK_SIDE_LB.ordinal()]),
                    PlayMode.LOOP),
                    "BlockLeft",
                    PhysicsConstants.BLOCKING_DRAG);
        }
    }
    
    /* ***********************************************************************
     * PUNCH
     */
    
    static abstract class FloydPunch extends BaseRendering {
        private long returnTime;
        
        FloydPunch(Animation animation, String debugName) {
            super(animation, debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            returnTime = AnimationConstants.DUR_MS_PUNCH;
            doll.game().getSoundService().play(SoundType.PUNCH);
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
                doll.done();
            }
        }
    }
    
    static class FloydPunchRight extends FloydPunch {
        FloydPunchRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.PUNCH_SIDE_R.ordinal()]),
                    PlayMode.NORMAL),
                    "PunchRight");
        }
    }
    
    static class FloydPunchLeft extends FloydPunch {
        public FloydPunchLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.PUNCH_SIDE_L.ordinal()]),
                    PlayMode.NORMAL),
                    "PunchLeft");
        }
    }
    
    /* ***********************************************************************
     * KICK
     */
    
    static abstract class FloydKick extends BaseRendering {
        private long returnTime;
        private boolean soundPlayed;
        
        FloydKick(Animation animation, String debugName) {
            super(animation, debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            returnTime = AnimationConstants.DUR_MS_KICK;
            soundPlayed = false;
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            returnTime -= Calc.gdxDeltaToMillis(delta);
            if(returnTime <= 0) {
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
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.KICK_SIDE_R.ordinal()]),
                    PlayMode.NORMAL),
                    "KickRight");
        }
    }
    
    static class FloydKickLeft extends FloydKick {
        FloydKickLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.KICK_SIDE_L.ordinal()]),
                    PlayMode.NORMAL),
                    "KickLeft");
        }
    }
    
    /* ***********************************************************************
     * JUMP
     */
    
    static abstract class FloydJump extends BaseSwitchRendering {
        private boolean hasSwitched;
        private boolean jumpFlag;
        
        public FloydJump(Animation upAnimation, Animation downAnimation, String debugName) {
            super(upAnimation, downAnimation, debugName);
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            hasSwitched = false;
            
            if(previousState != State.JUMP_FRONT
            && previousState != State.JUMP_LEFT
            && previousState != State.JUMP_RIGHT) {
                jumpFlag = false;
            }
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            if(!jumpFlag) {
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
        public FloydJumpFront() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_FRONT_UP.ordinal()]),
                    PlayMode.NORMAL),
                    new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_FRONT_DOWN.ordinal()]),
                    PlayMode.NORMAL),
                    "FrontJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
        }
    }
    
    static class FloydJumpRight extends FloydJump {
        public FloydJumpRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RUP.ordinal()]),
                    PlayMode.NORMAL),
                    new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_SIDE_RDOWN.ordinal()]),
                    PlayMode.NORMAL),
                    "RightJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
        }
    }
    
    static class FloydJumpLeft extends FloydJump {
        public FloydJumpLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LUP.ordinal()]),
                    PlayMode.NORMAL),
                    new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.JUMP_SIDE_LDOWN.ordinal()]),
                    PlayMode.NORMAL),
                    "LeftJump");
        }
        
        @Override
        public void finish(StatefulDoll doll) {
        }
    }
    
    /* ***********************************************************************
     * ARC
     */
    
    static abstract class FloydArc extends BaseRendering {
        private boolean jumpFlag;
        private final float acceleration;
        
        public FloydArc(Animation flightAnimation, String debugName, float acceleration) {
            super(flightAnimation, debugName);
            
            this.acceleration = acceleration;
        }

        @Override
        public void init(StatefulDoll doll, State previousState) {
            if(previousState != State.ARC_JUMP_LEFT
            && previousState != State.ARC_JUMP_RIGHT) {
                jumpFlag = false;
            }
        }

        @Override
        public void update(StatefulDoll doll, float delta) {
            if(!jumpFlag) {
                doll.body().applyLinearImpulse(
                        new Vector2(acceleration, PhysicsConstants.JUMP_FORCE),
                        doll.body().getPosition(),
                        true);
                jumpFlag = true;
            }
        }
    }
    
    static class FloydArcJumpRight extends FloydArc {
        public FloydArcJumpRight() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.MOVE_SIDE_RA_AIR.ordinal()],
                               textureRegions[FloydFrameType.MOVE_SIDE_RB_AIR.ordinal()]),
                    PlayMode.LOOP),
                    "RightArc",
                    PhysicsConstants.JUMP_FORCE);
        }
    }
    
    static class FloydArcJumpLeft extends FloydArc {
        public FloydArcJumpLeft() {
            super(new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                    Array.with(textureRegions[FloydFrameType.MOVE_SIDE_LA_AIR.ordinal()],
                               textureRegions[FloydFrameType.MOVE_SIDE_LB_AIR.ordinal()]),
                    PlayMode.LOOP),
                    "LeftArc",
                    -PhysicsConstants.JUMP_FORCE); //note the minus!!
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
