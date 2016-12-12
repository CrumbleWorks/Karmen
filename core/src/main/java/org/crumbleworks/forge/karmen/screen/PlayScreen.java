package org.crumbleworks.forge.karmen.screen;

import java.util.ArrayList;
import java.util.List;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.objects.DollInputAdapter;
import org.crumbleworks.forge.karmen.objects.StatefulDoll;
import org.crumbleworks.forge.karmen.objects.Thing;
import org.crumbleworks.forge.karmen.objects.character.Floyd;
import org.crumbleworks.forge.karmen.screen.arena.DanceFloor;
import org.crumbleworks.forge.karmen.util.PhysicsConstants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayScreen extends KarmenScreen {

    private final Karmen game;
    
    private InputMultiplexer inputMultiplexer;
    
    /* BOX2D */
    private final World world;
    private Box2DDebugRenderer debugR;
    
    /* SCENE */
    private final int GRAPHICS_FACTOR = 4;
    
    private final List<Thing> sceneObjects;
    
    //constructor
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
        
        /* SCENE */
        this.sceneObjects = new ArrayList<Thing>();
        
        /* DANCE FLOOR */
        int danceFloorOffset = 2;
        float danceFloorBottomLeftX = (Karmen.SCREEN_WIDTH / 16) + danceFloorOffset;
        float danceFloorBottomLeftY = 12 * GRAPHICS_FACTOR;
        DanceFloor df = new DanceFloor(
                game, GRAPHICS_FACTOR,
                danceFloorBottomLeftX,
                danceFloorBottomLeftY);
        sceneObjects.add(df);
        
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(new Vector2((Karmen.SCREEN_WIDTH / 2),
                                         40));
        Body groundBody = world.createBody(groundDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(Karmen.SCREEN_WIDTH, 10);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        
        /* DANCE COMMANDER (aka DJ Ynor9) */
        
        /* ACTORS */
        /* FLOYD */
        StatefulDoll floyd = new Floyd((Karmen.SCREEN_WIDTH / 2) - 32, 100, 64, 128, game, world);
        sceneObjects.add(floyd);
        inputMultiplexer = new InputMultiplexer(new DollInputAdapter(floyd));
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
        
        for(Thing so : sceneObjects) {
            so.update(delta);
        }
        
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
}
