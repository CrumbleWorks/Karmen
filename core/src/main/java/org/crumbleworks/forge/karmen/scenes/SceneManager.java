package org.crumbleworks.forge.karmen.scenes;

import org.crumbleworks.forge.karmen.Karmen;

import com.badlogic.gdx.Screen;

/**
 * Manages Scenes and transitions
 */
public class SceneManager {
    private final Karmen game;
    private Scene active;
    
    public SceneManager(Karmen game) {
        this.game = game;
        
        Scenes.INTRO.setScene(new IntroScreen(game));
        Scenes.MENU.setScene(new MenuScreen(game));
        Scenes.PLAY.setScene(new PlayScreen(game));
        Scenes.ABOUT.setScene(new AboutScreen(game));
    }
    
    public enum Scenes {
        INTRO, MENU, PLAY, ABOUT;
        
        private Scene s;
        
        private void setScene(Scene s) {
            this.s = s;
        }
        
        private Scene getScene() {
            return s;
        }
    }
    
    public void changeScene(Scenes scene) {
        if(active != null) {
            active.leave();
        }
        
        active = scene.getScene();
        active.enter();
        
        game.setScreen(active);
    }
    
    public final void dispose() {
        for(Scenes scene : Scenes.values()) {
            scene.getScene().dispose();
        }
    }
    
    public static interface Scene extends Screen {
        public void enter();
        public void leave();
    }
}
