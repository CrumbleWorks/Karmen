package org.crumbleworks.forge.karmen.objects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Controls a {@link StatefulDoll}
 */
public class DollInputAdapter extends InputAdapter {
    
    private StatefulDoll doll;

    public DollInputAdapter(StatefulDoll doll) {
        this.doll = doll;
    }
    
    @Override
    public boolean keyDown(int keyCode) {
        if(keyCode == Keys.A) {
            doll.goLeft();
            return true;
        }
        if(keyCode == Keys.D) {
            doll.goRight();
            return true;
        }
        if(keyCode == Keys.S) {
            doll.face();
            return true;
        }
        if(keyCode == Keys.SPACE) {
            doll.jump();
            return true;
        }
        
        if(keyCode == Keys.J) {
            doll.punch();
            return true;
        }
        if(keyCode == Keys.K) {
            doll.block();
            return true;
        }
        if(keyCode == Keys.L) {
            doll.kick();
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean keyUp(int keyCode) {
        if(keyCode == Keys.A) {
            doll.stopLeft();
            return true;
        }
        if(keyCode == Keys.D) {
            doll.stopRight();
            return true;
        }
        if(keyCode == Keys.K) {
            doll.stopBlock();
            return true;
        }
        
        return false;
    }
    

}
