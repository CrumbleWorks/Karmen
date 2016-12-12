package org.crumbleworks.forge.karmen.util.asset.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundLibrary {

    private Sound light;
    private Sound door;
    private Sound jump;
    private Sound kick;
    private Sound punch;
    private Sound shieldPunch;
    
    public SoundLibrary() {
        light = loadSound("sfx/effects/light.ogg");
        door = loadSound("sfx/effects/door.ogg");
        jump = loadSound("sfx/effects/jump.ogg");
        kick = loadSound("sfx/effects/kick.ogg");
        punch = loadSound("sfx/effects/punch.ogg");
        shieldPunch = loadSound("sfx/effects/shieldpunch.ogg");
    }
    
    private Sound loadSound(String soundFileName) {
        return Gdx.audio.newSound(Gdx.files.internal(soundFileName));
    }
    
    public Sound getSound(SoundType type) {
        switch (type) {
        case LIGHT:
            return light;
        case DOOR:
            return door;
        case JUMP:
            return jump;
        case KICK:
            return kick;
        case PUNCH:
            return punch;
        case SHIELD_PUNCH:
            return shieldPunch;
//        case BLOCK:
//            return blockSound; TODO BLOCK als Sound und nicht als Music verwenden
        default:
            Gdx.app.log("SoundLibrary", "SoundType " + type + " is not mapped to a file.");
            return null;
        }
    }
    
    public void dispose() {
        door.dispose();
        light.dispose();
        jump.dispose();
        kick.dispose();
        punch.dispose();
        shieldPunch.dispose();
    }
    
}
