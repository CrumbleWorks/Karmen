package org.crumbleworks.forge.karmen.character;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class FloydInputAdapter extends InputAdapter {
    
    private Floyd floyd;

    public FloydInputAdapter(Floyd floyd) {
        this.floyd = floyd;
    }
    
    @Override
    public boolean keyDown(int keyCode) {
        if(keyCode == Keys.A) {
            // left
            floyd.setFacing(Facing.LEFT);
            floyd.setAction(Action.RUN);
            return true;
        }
        if(keyCode == Keys.D) {
            // right
            floyd.setFacing(Facing.RIGHT);
            floyd.setAction(Action.RUN);
            return true;
        }
        if(keyCode == Keys.S) {
            // right
            floyd.setFacing(Facing.FRONT);
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        if(keyCode == Keys.SPACE) {
            // jump
            floyd.setGrounded(false);
            return true;
        }
        
        if(keyCode == Keys.J) {
            // fist
            floyd.setAction(Action.FIST);
            return true;
        }
        if(keyCode == Keys.K) {
            // block
            floyd.setAction(Action.BLOCK);
            return true;
        }
        if(keyCode == Keys.L) {
            // kick
            floyd.setAction(Action.KICK);
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean keyUp(int keyCode) {
        if(keyCode == Keys.A) {
            // left
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        if(keyCode == Keys.D) {
            // right
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        if(keyCode == Keys.SPACE) {
            // jump
            floyd.setGrounded(true);
            return true;
        }
        
        if(keyCode == Keys.J) {
            // fist
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        if(keyCode == Keys.K) {
            // block
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        if(keyCode == Keys.L) {
            // kick
            floyd.setAction(Action.FLOYDING);
            return true;
        }
        
        return false;
    }
    

}
