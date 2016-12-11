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
            floyd.goLeft();
            return true;
        }
        if(keyCode == Keys.D) {
            floyd.goRight();
            return true;
        }
        if(keyCode == Keys.S) {
            floyd.face();
            return true;
        }
        if(keyCode == Keys.SPACE) {
            floyd.jump();
            return true;
        }
        
        if(keyCode == Keys.J) {
            floyd.punch();
            return true;
        }
        if(keyCode == Keys.K) {
            floyd.block();
            return true;
        }
        if(keyCode == Keys.L) {
            floyd.kick();
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean keyUp(int keyCode) {
        if(keyCode == Keys.A) {
            floyd.stopLeft();
            return true;
        }
        if(keyCode == Keys.D) {
            floyd.stopRight();
            return true;
        }
        if(keyCode == Keys.K) {
            floyd.stopBlock();
            return true;
        }
        
        return false;
    }
    

}
