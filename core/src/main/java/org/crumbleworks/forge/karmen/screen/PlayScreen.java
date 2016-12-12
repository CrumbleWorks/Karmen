package org.crumbleworks.forge.karmen.screen;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.character.Floyd;
import org.crumbleworks.forge.karmen.character.FloydInputAdapter;
import org.crumbleworks.forge.karmen.util.AnimationConstants;
import org.crumbleworks.forge.karmen.util.PhysicsConstants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

public class PlayScreen extends KarmenScreen {

    private final Karmen game;
    
    private Floyd floyd;
    
    private InputMultiplexer inputMultiplexer;
    
    /* ANIMATIONS */
    private Animation danceFloorAnimation;
    private float danceFloorAnimationStateTime;
    
    /* BOX2D */
    private final World world;
    private final Box2DDebugRenderer debugR;
    
    public PlayScreen(final Karmen game) {
        super(game,
                new int[]{Keys.ESCAPE, Keys.M},
                new String[]{"ESC", "M"},
                new Runnable[]{()->{game.setScreen(game.menuScreen);},
                               ()->{/*TODO TOGGLE_MUTE*/}},
                new Texture[]{game.getTextureLibrary().OPT_RET,
                              game.getTextureLibrary().OPT_NOTE},
                new Texture[]{game.getTextureLibrary().OPT_RET_INV,
                              game.getTextureLibrary().OPT_NOTE_STRIKE}
             );
        
        this.game = game;

        /* BOX2D */
        Box2D.init();
        world = new World(new Vector2(PhysicsConstants.HORIZONTAL_GRAVITY, PhysicsConstants.HORIZONTAL_GRAVITY), true);
        if(Karmen.isDebug) {
            debugR = new Box2DDebugRenderer();
        }
        
        /* FLOYD */
        this.floyd = new Floyd(Karmen.SCREEN_WIDTH / 2, 50, 64, 128, game, world);
        
        inputMultiplexer = new InputMultiplexer(new FloydInputAdapter(floyd));
//        renderer = new PlayScreenDebugRenderer();
        
        danceFloorAnimation = new Animation(AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH, Array.with(game.getTextureLibrary().getDanceFloorRegions()), PlayMode.LOOP);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    
    @Override
    void krender(float delta) {
        Gdx.gl.glClearColor(1, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        
        drawDanceFloor(delta);
        floyd.update(delta);
        
        game.getBatch().end();
        
        if(Karmen.isDebug) {
            debugR.render(world, game.getCamera().combined);
        }
        
        /* BOX2D */
        doPhysicsStep(delta);
    }

    private float accumulator = 0;
    
    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        
        
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= PhysicsConstants.WORLD_STEP) {
            world.step(PhysicsConstants.WORLD_STEP,
                       PhysicsConstants.VELOCITY_ITERATIONS,
                       PhysicsConstants.POSITION_ITERATIONS);
            accumulator -= PhysicsConstants.WORLD_STEP;
        }
    }
    
    private void drawDanceFloor(float delta) {
        danceFloorAnimationStateTime += delta;
        TextureRegion currentAnimationFrame = danceFloorAnimation.getKeyFrame(danceFloorAnimationStateTime);
        
        int danceFloorOffset = 2;
        float danceFloorBottomLeftX = (Karmen.SCREEN_WIDTH / 16) + danceFloorOffset;
        float danceFloorBottomLeftY = 100;
        
        game.getBatch().draw(currentAnimationFrame, danceFloorBottomLeftX, danceFloorBottomLeftY, currentAnimationFrame.getRegionWidth() * 4, currentAnimationFrame.getRegionHeight() * 4);
    }
    
}
