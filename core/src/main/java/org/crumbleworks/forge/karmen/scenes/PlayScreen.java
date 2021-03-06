package org.crumbleworks.forge.karmen.scenes;

import java.util.ArrayList;
import java.util.List;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.objects.DollContactListener;
import org.crumbleworks.forge.karmen.objects.DollInputAdapter;
import org.crumbleworks.forge.karmen.objects.StatefulDoll;
import org.crumbleworks.forge.karmen.objects.Thing;
import org.crumbleworks.forge.karmen.objects.character.Floyd;
import org.crumbleworks.forge.karmen.physics.FixtureType;
import org.crumbleworks.forge.karmen.screen.arena.DanceFloor;
import org.crumbleworks.forge.karmen.util.PhysicsConstants;
import org.crumbleworks.forge.karmen.util.asset.music.MusicType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
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
    private World world;
    private Box2DDebugRenderer debugR;
    
    /* SCENE */
    private final int GRAPHICS_FACTOR = 4;
    
    private List<Thing> sceneObjects;
    
    //constructor
    public PlayScreen(final Karmen game) {
        super(game, new boolean[]{true, true});
        
        this.game = game;
        
        Box2D.init();
    }

    @Override
    public void enter() {
        game.getMusicService().play(MusicType.ARENA_INTRO, false);
        game.getMusicService().schedule(MusicType.ARENA_LEVEL_1, true);

        /* BOX2D */
        world = new World(new Vector2(PhysicsConstants.HORIZONTAL_GRAVITY, PhysicsConstants.VERTICAL_GRAVITY), true);
        World.setVelocityThreshold(1.0f);
        if(Karmen.isDebug) {
            debugR = new Box2DDebugRenderer();
        }
        
        /* ARENA BORDERS */
        final int DISTANCE_FROM_WALLS = 80;
        final int DISTANCE_FROM_BOTTOM = 80;
        
        //DEFINITIONS
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(new Vector2(Karmen.SCREEN_WIDTH / 2, DISTANCE_FROM_BOTTOM));
        Body ground = world.createBody(groundDef);
        
        BodyDef leftWallDef = new BodyDef();
        leftWallDef.position.set(new Vector2(DISTANCE_FROM_WALLS, 0));
        Body leftWall = world.createBody(leftWallDef);
        
        BodyDef rightWallDef = new BodyDef();
        rightWallDef.position.set(new Vector2(Karmen.SCREEN_WIDTH - DISTANCE_FROM_WALLS, 0));
        Body rightWall = world.createBody(rightWallDef);
        
        BodyDef roofDef = new BodyDef();
        roofDef.position.set(new Vector2(Karmen.SCREEN_WIDTH / 2, Karmen.SCREEN_HEIGHT - DISTANCE_FROM_BOTTOM));
        Body roof = world.createBody(roofDef);
        
        //PLACEMENT
        PolygonShape gbox = new PolygonShape();

        gbox.setAsBox(Karmen.SCREEN_WIDTH, 10);
        ground.createFixture(gbox, 0.0f).setUserData(FixtureType.FLOOR);
        roof.createFixture(gbox, 0.0f);
        
        gbox.setAsBox(10, Karmen.SCREEN_HEIGHT);
        leftWall.createFixture(gbox, 0.0f);
        rightWall.createFixture(gbox, 0.0f);
        
        //get rid of shape
        gbox.dispose();
        
        /* COLLISION STUFF */
        world.setContactListener(new DollContactListener());
        
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
        
        /* DANCE COMMANDER (aka DJ Ynor9) */
        
        /* ACTORS */
        /* FLOYD */
        StatefulDoll floyd = new Floyd((Karmen.SCREEN_WIDTH / 2) - 32, 100, 64, 128, game, world);
        sceneObjects.add(floyd);
        
        inputMultiplexer = new InputMultiplexer(new DollInputAdapter(floyd));
    }

    @Override
    public void leave() {
        Gdx.input.setInputProcessor(null);
        
        world.dispose();
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
