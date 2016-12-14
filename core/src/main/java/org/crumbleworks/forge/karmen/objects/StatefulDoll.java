package org.crumbleworks.forge.karmen.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.physics.FixtureType;
import org.crumbleworks.forge.karmen.util.Calc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A StatefulDoll
 * 
 * @author Michael Stocker
 */
public abstract class StatefulDoll implements Thing {
    
    public static enum State {
        STILL_FRONT, STILL_RIGHT, STILL_LEFT,
        RUN_RIGHT, RUN_LEFT,
        BLOCK_FRONT, BLOCK_RIGHT, BLOCK_LEFT,
        PUNCH_RIGHT, KICK_RIGHT,
        PUNCH_LEFT, KICK_LEFT,
        JUMP_FRONT,
        JUMP_RIGHT, JUMP_KICK_RIGHT, ARC_JUMP_RIGHT, ARC_KICK_RIGHT,
        JUMP_LEFT, JUMP_KICK_LEFT, ARC_JUMP_LEFT, ARC_KICK_LEFT;
    }
    
    private static enum Action {
        LEFT, STOP_LEFT, 
        RIGHT, STOP_RIGHT, 
        BLOCK, STOP_BLOCK,
        FACE,
        JUMP, LAND,
        PUNCH, KICK, DONE;
    }
    
    /**
     * 1. request STATE
     * 2. call ACTION
     * 3. get new STATE
     */
    private final Map<State, Map<Action, Supplier<State>>> stateMap;
    
    {
        stateMap = new HashMap<State, Map<Action, Supplier<State>>>() {{
            /* STILLS */
            put(State.STILL_FRONT, new HashMap<Action, Supplier<State>>() {{
                put(Action.LEFT, ()->{return State.RUN_LEFT;});
                put(Action.RIGHT, ()->{return State.RUN_RIGHT;});
                put(Action.JUMP, ()->{return State.JUMP_FRONT;});
                put(Action.BLOCK, ()->{return State.BLOCK_FRONT;});
            }});

            put(State.STILL_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.LEFT, ()->{return State.RUN_LEFT;});
                put(Action.FACE, ()->{return State.STILL_FRONT;});
                put(Action.BLOCK, ()->{return State.BLOCK_RIGHT;});
                put(Action.RIGHT, ()->{return State.RUN_RIGHT;});
                put(Action.PUNCH, ()->{return State.PUNCH_RIGHT;});
                put(Action.KICK, ()->{return State.KICK_RIGHT;});
                put(Action.JUMP, ()->{return State.JUMP_RIGHT;});
            }});

            put(State.STILL_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.FACE, ()->{return State.STILL_FRONT;});
                put(Action.RIGHT, ()->{return State.RUN_RIGHT;});
                put(Action.PUNCH, ()->{return State.PUNCH_LEFT;});
                put(Action.KICK, ()->{return State.KICK_LEFT;});
                put(Action.BLOCK, ()->{return State.BLOCK_LEFT;});
                put(Action.LEFT, ()->{return State.RUN_LEFT;});
                put(Action.JUMP, ()->{return State.JUMP_LEFT;});
            }});

            /* RUNNING */
            put(State.RUN_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.STOP_RIGHT, ()->{return State.STILL_RIGHT;});
                put(Action.LEFT, ()->{return State.RUN_LEFT;});
                put(Action.BLOCK, ()->{return State.BLOCK_RIGHT;});
                put(Action.PUNCH, ()->{return State.PUNCH_RIGHT;});
                put(Action.KICK, ()->{return State.KICK_RIGHT;});
                put(Action.FACE, ()->{return State.STILL_FRONT;});
            }});

            put(State.RUN_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.STOP_LEFT, ()->{return State.STILL_LEFT;});
                put(Action.RIGHT, ()->{return State.RUN_RIGHT;});
                put(Action.BLOCK, ()->{return State.BLOCK_LEFT;});
                put(Action.KICK, ()->{return State.KICK_LEFT;});
                put(Action.PUNCH, ()->{return State.PUNCH_LEFT;});
                put(Action.FACE, ()->{return State.STILL_FRONT;});
            }});

            /* BLOCKS */
            put(State.BLOCK_FRONT, new HashMap<Action, Supplier<State>>() {{
                put(Action.STOP_BLOCK, ()->{return State.STILL_FRONT;});
            }});

            put(State.BLOCK_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.STOP_BLOCK, ()->{
                        if(runRight) {
                            return State.RUN_RIGHT;
                        }
                        
                        if(runLeft) {
                            return State.RUN_LEFT;
                        }
                        
                        return State.STILL_RIGHT;
                    });
            }});

            put(State.BLOCK_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.STOP_BLOCK, ()->{
                    if(runLeft) {
                        return State.RUN_LEFT;
                    }
                    
                    if(runRight) {
                        return State.RUN_RIGHT;
                    }
                    
                    return State.STILL_LEFT;
                });
            }});

            /* PUNCH */
            put(State.PUNCH_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.DONE, ()->{
                    if(runRight) {
                        return State.RUN_RIGHT;
                    }
                    
                    if(runLeft) {
                        return State.RUN_LEFT;
                    }
                    
                    return State.STILL_RIGHT;
                });
            }});

            put(State.PUNCH_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.DONE, ()->{
                    if(runLeft) {
                        return State.RUN_LEFT;
                    }
                    
                    if(runRight) {
                        return State.RUN_RIGHT;
                    }
                    
                    return State.STILL_LEFT;
                });
            }});

            /* KICK */
            put(State.KICK_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.DONE, ()->{
                    if(runRight) {
                        return State.RUN_RIGHT;
                    }
                    
                    if(runLeft) {
                        return State.RUN_LEFT;
                    }
                    
                    return State.STILL_RIGHT;
                });
            }});
            
            put(State.KICK_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.DONE, ()->{
                    if(runLeft) {
                        return State.RUN_LEFT;
                    }
                    
                    if(runRight) {
                        return State.RUN_RIGHT;
                    }
                    
                    return State.STILL_LEFT;
                });
            }});

            /* JUMP */
            put(State.JUMP_FRONT, new HashMap<Action, Supplier<State>>() {{
                put(Action.RIGHT, ()->{return State.JUMP_RIGHT;});
                put(Action.LEFT, ()->{return State.JUMP_LEFT;});
                put(Action.LAND, ()->{return State.STILL_FRONT;});
            }});

            put(State.JUMP_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.KICK, ()->{return State.JUMP_KICK_RIGHT;});
                put(Action.LEFT, ()->{return State.JUMP_LEFT;});
                put(Action.FACE, ()->{return State.JUMP_FRONT;});
                put(Action.LAND, ()->{return State.STILL_RIGHT;});
            }});
            
            put(State.JUMP_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.KICK, ()->{return State.JUMP_KICK_LEFT;});
                put(Action.RIGHT, ()->{return State.JUMP_RIGHT;});
                put(Action.FACE, ()->{return State.JUMP_FRONT;});
                put(Action.LAND, ()->{return State.STILL_LEFT;});
            }});
            
            /* JUMP KICK */
            put(State.JUMP_KICK_RIGHT, new HashMap<Action, Supplier<State>>() {{
            }});
            
            put(State.JUMP_KICK_LEFT, new HashMap<Action, Supplier<State>>() {{
            }});

            /* ARC */
            put(State.ARC_JUMP_RIGHT, new HashMap<Action, Supplier<State>>() {{
                put(Action.KICK, ()->{return State.ARC_KICK_RIGHT;});
                put(Action.LEFT, ()->{return State.ARC_JUMP_LEFT;});
            }});

            put(State.ARC_JUMP_LEFT, new HashMap<Action, Supplier<State>>() {{
                put(Action.KICK, ()->{return State.ARC_KICK_LEFT;});
                put(Action.RIGHT, ()->{return State.ARC_JUMP_RIGHT;});
            }});

            /* ARC KICK */
            put(State.ARC_KICK_RIGHT, new HashMap<Action, Supplier<State>>() {{
            }});

            put(State.ARC_KICK_LEFT, new HashMap<Action, Supplier<State>>() {{
            }});
        }};
    }
    
    /* ***********************************************************************
     * NON STATICS
     */
    
    //game ref
    private final Karmen game;
    
    //world
    private final World world;
    
    //behaviours
    private final Map<State, Behaviour> behaviours;
    private final Map<Class<? extends Behaviour>, State> behaviourToState; 
    
    //contant data
    private final float PHYS_DENSITY = 1.0f;
    private final float PHYS_FRICTION = 0.0f;
    private final float PHYS_RESTITUTION = 0.0f; //bouncyness
    //runtime data
    private Behaviour currentBehaviour;
    private final PSV psv;
    private final Body body;
    
    //constructor
    public StatefulDoll(Karmen game, World inWorld, PSV psv, State initState, final Map<State, Behaviour> behaviours) {
        this.game = game;
        this.world = inWorld;
        this.psv = psv;
        
        this.currentBehaviour = behaviours.get(initState);
        
        this.behaviours = behaviours;
        this.behaviourToState = new HashMap<Class<? extends Behaviour>, State>();
        
        for(Entry<State, Behaviour> behaviour : behaviours.entrySet()) {
            behaviourToState.put(behaviour.getValue().getClass(), behaviour.getKey());
        }
        
        //MAKE ME A NEW BODY, MORTAL!
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(psv.position.x + psv.size.x / 2,
                             psv.position.y + psv.size.y / 2
                             ); //set start pos
        
        body = world.createBody(bodyDef);
        
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.setAsBox(psv.size.x / 2, psv.size.y / 2);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boundingBox;
        fixtureDef.density = PHYS_DENSITY;
        fixtureDef.friction = PHYS_FRICTION;
        fixtureDef.restitution = PHYS_RESTITUTION;
        
        Fixture f = body.createFixture(fixtureDef);
        f.setUserData(FixtureType.DOLL);
        
        body.setUserData(this);
        
        boundingBox.dispose();
    }
    
    /* ********************************
     * UPDATE
     */
    
    private String horizontalDeltaString = "HS: ";
    private float hsaSpeedAve = 0;
    private String verticalDeltaString =   "VS: ";
    private float vsaSpeedAve = 0;
    
    @Override
    public final void update(float delta) {
        psv().position.x = body().getPosition().x - psv.size.x / 2;
        psv().position.y = body().getPosition().y - psv.size.y / 2;

        currentBehaviour._update(this, delta);
        
        if(Karmen.isDebug) {
            hsaSpeedAve = (hsaSpeedAve + body.getLinearVelocity().x) / 2.0f;
            vsaSpeedAve = (vsaSpeedAve + body.getLinearVelocity().y) / 2.0f;

            game.getArial().setColor(Color.CYAN);
            game.getArial().draw(game.getBatch(),
                    horizontalDeltaString + hsaSpeedAve,
                    60,
                    Karmen.SCREEN_HEIGHT - 20);
            game.getArial().draw(game.getBatch(),
                    verticalDeltaString + vsaSpeedAve,
                    60,
                    Karmen.SCREEN_HEIGHT - 40);
        }
    }
    
    /* *********************
     * GETTERS
     */
    
    public final Karmen game() {
        return game;
    }
    
    public final PSV psv() {
        return psv;
    }
    
    public final Body body() {
        return body;
    }
    
    public final Behaviour activeBehaviour() {
        return currentBehaviour;
    }
    
    private void set(Supplier<State> supplier) {
        if(supplier != null) {
            Behaviour newBehaviour = behaviours.get(supplier.get());
            if(newBehaviour != null) {
                currentBehaviour.finish(this);
                newBehaviour._init(this, behaviourToState.get(currentBehaviour.getClass()));
                currentBehaviour = newBehaviour;
            }
        }
    }
    
    private void doAction(Action action) {
        set(stateMap.get(behaviourToState.get(currentBehaviour.getClass())).get(action));
    }
    
    /* ***********************************************************************
     * DOLL ACTIONS
     */
    
    private boolean runRight = false;
    private boolean runLeft = false;
    
    public void face() {
        doAction(Action.FACE);
    }
    
    public void goLeft() {
        doAction(Action.LEFT);
        runLeft = true;
    }
    
    public void stopLeft() {
        doAction(Action.STOP_LEFT);
        runLeft = false;
    }
    
    public void goRight() {
        doAction(Action.RIGHT);
        runRight = true;
    }
    
    public void stopRight() {
        doAction(Action.STOP_RIGHT);
        runRight = false;
    }
    
    public void jump() {
        doAction(Action.JUMP);
    }
    
    public void land() {
        doAction(Action.LAND);
    }
    
    public void block() {
        doAction(Action.BLOCK);
    }
    
    public void stopBlock() {
        doAction(Action.STOP_BLOCK);
    }
    
    public void punch() {
        doAction(Action.PUNCH);
    }
    
    public void kick() {
        doAction(Action.KICK);
    }
    
    public void done() {
        doAction(Action.DONE);
    }
    
    /* ***********************************************************************
     * INNER CLASSES
     */
    
    public static interface Behaviour {
        abstract void _init(StatefulDoll doll, State previousState);
        abstract void _update(StatefulDoll doll, float delta);
        
        abstract void finish(StatefulDoll doll);
    }
}
