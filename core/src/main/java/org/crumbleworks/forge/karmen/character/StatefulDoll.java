package org.crumbleworks.forge.karmen.character;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A StatefulDoll
 * 
 * @author Michael Stocker
 */
public abstract class StatefulDoll {
    
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
        LEFT, RIGHT, BLOCK, STOP, FACE, JUMP, PUNCH, KICK;
    }
    
    /**
     * 1. request STATE
     * 2. call ACTION
     * 3. get new STATE
     */
    private static final Map<State, Map<Action, State>> stateMap;
    
    static {
        stateMap = new HashMap<State, Map<Action, State>>() {{
            /* STILLS */
            put(State.STILL_FRONT, new HashMap<Action, State>() {{
                put(Action.LEFT, State.RUN_LEFT);
                put(Action.RIGHT, State.RUN_RIGHT);
                put(Action.JUMP, State.JUMP_FRONT);
                put(Action.BLOCK, State.BLOCK_FRONT);
            }});

            put(State.STILL_RIGHT, new HashMap<Action, State>() {{
                put(Action.LEFT, State.RUN_LEFT);
                put(Action.FACE, State.STILL_FRONT);
                put(Action.BLOCK, State.BLOCK_RIGHT);
                put(Action.RIGHT, State.RUN_RIGHT);
                put(Action.PUNCH, State.PUNCH_RIGHT);
                put(Action.KICK, State.KICK_RIGHT);
                put(Action.JUMP, State.JUMP_RIGHT);
            }});

            put(State.STILL_LEFT, new HashMap<Action, State>() {{
                put(Action.FACE, State.STILL_FRONT);
                put(Action.RIGHT, State.RUN_RIGHT);
                put(Action.PUNCH, State.PUNCH_LEFT);
                put(Action.KICK, State.KICK_LEFT);
                put(Action.BLOCK, State.BLOCK_LEFT);
                put(Action.LEFT, State.RUN_LEFT);
                put(Action.JUMP, State.JUMP_LEFT);
            }});

            /* RUNNING */
            put(State.RUN_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_RIGHT);
                put(Action.BLOCK, State.BLOCK_RIGHT);
                put(Action.PUNCH, State.PUNCH_RIGHT);
                put(Action.KICK, State.KICK_RIGHT);
                put(Action.FACE, State.STILL_FRONT);
            }});

            put(State.RUN_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_LEFT);
                put(Action.BLOCK, State.BLOCK_LEFT);
                put(Action.KICK, State.KICK_LEFT);
                put(Action.PUNCH, State.PUNCH_LEFT);
                put(Action.FACE, State.STILL_FRONT);
            }});

            /* BLOCKS */
            put(State.BLOCK_FRONT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_FRONT);
            }});

            put(State.BLOCK_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_RIGHT);
            }});

            put(State.BLOCK_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_LEFT);
            }});

            /* FIGHTING */
            put(State.PUNCH_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_RIGHT);
            }});

            put(State.KICK_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_RIGHT);
            }});

            put(State.PUNCH_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_LEFT);
            }});

            put(State.KICK_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.STILL_LEFT);
            }});

            /* AIR FRONT*/
            put(State.JUMP_FRONT, new HashMap<Action, State>() {{
                put(Action.RIGHT, State.JUMP_RIGHT);
                put(Action.LEFT, State.JUMP_LEFT);
            }});

            /* AIR RIGHT */
            put(State.JUMP_RIGHT, new HashMap<Action, State>() {{
                put(Action.KICK, State.JUMP_KICK_RIGHT);
                put(Action.LEFT, State.JUMP_LEFT);
                put(Action.FACE, State.JUMP_FRONT);
            }});

            put(State.JUMP_KICK_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.JUMP_RIGHT);
            }});

            put(State.ARC_JUMP_RIGHT, new HashMap<Action, State>() {{
                put(Action.KICK, State.ARC_KICK_RIGHT);
                put(Action.LEFT, State.ARC_JUMP_LEFT);
            }});

            put(State.ARC_KICK_RIGHT, new HashMap<Action, State>() {{
                put(Action.STOP, State.ARC_JUMP_RIGHT);
            }});

            /* AIR LEFT */
            put(State.JUMP_LEFT, new HashMap<Action, State>() {{
                put(Action.KICK, State.JUMP_KICK_LEFT);
                put(Action.RIGHT, State.JUMP_RIGHT);
                put(Action.FACE, State.JUMP_FRONT);
            }});

            put(State.JUMP_KICK_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.JUMP_LEFT);
            }});

            put(State.ARC_JUMP_LEFT, new HashMap<Action, State>() {{
                put(Action.KICK, State.ARC_KICK_LEFT);
                put(Action.RIGHT, State.ARC_JUMP_RIGHT);
            }});

            put(State.ARC_KICK_LEFT, new HashMap<Action, State>() {{
                put(Action.STOP, State.ARC_JUMP_LEFT);
            }});
        }};
    }
    
    /* ***********************************************************************
     * NON STATICS
     */
    
    private final Karmen game;
    
    private final Map<State, Behaviour> behaviours;
    private final Map<Class<? extends Behaviour>, State> behaviourToState; 
    
    private Behaviour currentBehaviour;
    private final PSV psv;
    
    public StatefulDoll(Karmen game, PSV psv, State initState, final Map<State, Behaviour> behaviours) {
        this.game = game;
        this.psv = psv;
        
        this.currentBehaviour = behaviours.get(initState);
        
        this.behaviours = behaviours;
        this.behaviourToState = new HashMap<Class<? extends Behaviour>, State>();
        
        for(Entry<State, Behaviour> behaviour : behaviours.entrySet()) {
            behaviourToState.put(behaviour.getValue().getClass(), behaviour.getKey());
        }
    }
    
    /**
     * Call this method to process state logic
     */
    public final void update(float delta) {
        currentBehaviour.update(this, delta);
    }
    
    public final Karmen game() {
        return game;
    }
    
    public final PSV psv() {
        return psv;
    }
    
    protected void set(State state) {
        Behaviour newBehaviour = behaviours.get(state);
        
        if(newBehaviour != null) {
            newBehaviour.init(state);
            currentBehaviour = newBehaviour;
        }
    }
    
    private void set(Action action) {
        set(stateMap.get(behaviourToState.get(currentBehaviour.getClass())).get(action));
    }
    
    /* ***********************************************************************
     * DOLL ACTIONS
     */
    
    /** CONTINUOUS */
    public void goLeft() {
        set(Action.LEFT);
    }
    
    /** SINGLE */
    public void face() {
        set(Action.FACE);
    }
    
    /** CONTINUOUS */
    public void goRight() {
        set(Action.RIGHT);
    }
    
    /** SINGLE */
    public void jump() {
        set(Action.JUMP);
    }
    
    /** SINGLE */
    public void punch() {
        set(Action.PUNCH);
    }
    
    /** CONTINUOUS */
    public void block() {
        set(Action.BLOCK);
    }
    
    /** SINGLE */
    public void kick() {
        set(Action.KICK);
    }
    
    /** STOP doing CONTINUOUS action */
    public void stop() {
        set(Action.STOP);
    }
    
    /* ***********************************************************************
     * INNER CLASSES
     */
    
    public static interface Behaviour {
        abstract void init(State previousState);
        abstract void update(StatefulDoll doll, float delta);
    }
}
