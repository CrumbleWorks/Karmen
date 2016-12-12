package org.crumbleworks.forge.karmen.util.asset.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicLibrary {

    private Music menu;
    private Music arenaIntro;
    private Music arenaLevel1;
    private Music arenaLevel2;
    private Music arenaLevel3;
    private Music arenaLevel4;
    private Music arenaLevel5;
    private Music gameOver;
    
    public MusicLibrary() {
        menu = loadMusic("sfx/music/Menumusic (Loop).ogg");
        arenaIntro = loadMusic("sfx/music/intro.ogg");
        arenaLevel1 = loadMusic("sfx/music/level1.ogg");
        arenaLevel2 = loadMusic("sfx/music/level2.ogg");
        arenaLevel3 = loadMusic("sfx/music/level3.ogg");
        arenaLevel4 = loadMusic("sfx/music/level4.ogg");
        arenaLevel5 = loadMusic("sfx/music/level5.ogg");
        gameOver = loadMusic("sfx/music/gameover.ogg");
    }
    
    private Music loadMusic(String musicFileName) {
        return Gdx.audio.newMusic(Gdx.files.internal(musicFileName));
    }
    
    public Music getMusic(MusicType type) {
        switch (type) {
        case MENU:
            return menu;
        case ARENA_INTRO:
            return arenaIntro;
        case ARENA_LEVEL_1:
            return arenaLevel1;
        case ARENA_LEVEL_2:
            return arenaLevel2;
        case ARENA_LEVEL_3:
            return arenaLevel3;
        case ARENA_LEVEL_4:
            return arenaLevel4;
        case ARENA_LEVEL_5:
            return arenaLevel5;
        case GAME_OVER:
            return gameOver;
        default:
            Gdx.app.log("MusicLibrary", "MusicType " + type + " is not mapped to a file.");
            return null;
        }
    }
    
    public void dispose() {
        menu.dispose();
        arenaIntro.dispose();
        arenaLevel1.dispose();
        arenaLevel2.dispose();
        arenaLevel3.dispose();
        arenaLevel4.dispose();
        arenaLevel5.dispose();
        gameOver.dispose();
    }
    
}
