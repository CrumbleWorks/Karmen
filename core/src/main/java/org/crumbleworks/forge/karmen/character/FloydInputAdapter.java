package org.crumbleworks.forge.karmen.character;

import org.crumbleworks.forge.karmen.character.Floyd.Direction;

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
            floyd.startRunning(Direction.LEFT);
            return true;
        }
        if(keyCode == Keys.D) {
            // right
            floyd.startRunning(Direction.RIGHT);
            return true;
        }
        if(keyCode == Keys.S) {
            // right
            floyd.stare();
            return true;
        }
        if(keyCode == Keys.SPACE) {
            // jump
            floyd.jump();
            return true;
        }
        
        if(keyCode == Keys.J) {
            // fist
            floyd.punch();
            return true;
        }
        if(keyCode == Keys.K) {
            // block
            floyd.startBlocking();
            return true;
        }
        if(keyCode == Keys.L) {
            // kick
            floyd.kick();
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean keyUp(int keyCode) {
        if(keyCode == Keys.A) {
            // left
            floyd.stopRunning();
            return true;
        }
        if(keyCode == Keys.D) {
            // right
            floyd.stopRunning();
            return true;
        }
        if(keyCode == Keys.K) {
            // block
            floyd.stopBlocking();
            return true;
        }
        
        return false;
    }
    

}
