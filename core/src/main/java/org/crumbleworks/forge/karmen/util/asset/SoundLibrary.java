package org.crumbleworks.forge.karmen.util.asset;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundLibrary {

    private Random random;

    private Music blockSound;
    private Sound doorSound;
    private Sound jumpSound;
    private Sound kickSound;
    private Sound lightSound;
    private Sound punchSound;
    private Sound shieldSound;
    
    public SoundLibrary() {
        random = new Random();
        
        blockSound = loadMusic("sfx/effects/block.ogg");
        doorSound = loadSound("sfx/effects/door.ogg");
        jumpSound = loadSound("sfx/effects/jump.ogg");
        kickSound = loadSound("sfx/effects/kick.ogg");
        lightSound = loadSound("sfx/effects/light.ogg");
        punchSound = loadSound("sfx/effects/punch.ogg");
        shieldSound = loadSound("sfx/effects/shieldpunch.ogg");
    }
    
    private Sound loadSound(String soundFileName) {
        return Gdx.audio.newSound(Gdx.files.internal(soundFileName));
    }
    
    private Music loadMusic(String musicFileName) {
        return Gdx.audio.newMusic(Gdx.files.internal(musicFileName));
    }
    
    public Random getRandom() {
        return random;
    }

    public Music getBlockSound() {
        return blockSound;
    }

    public Sound getDoorSound() {
        return doorSound;
    }

    public Sound getJumpSound() {
        return jumpSound;
    }

    public Sound getKickSound() {
        return kickSound;
    }

    public Sound getLightSound() {
        return lightSound;
    }

    public Sound getPunchSound() {
        return punchSound;
    }

    public Sound getShieldSound() {
        return shieldSound;
    }

    public void dispose() {
        blockSound.dispose();
        doorSound.dispose();
        jumpSound.dispose();
        kickSound.dispose();
        lightSound.dispose();
        punchSound.dispose();
        shieldSound.dispose();
    }
    
}
