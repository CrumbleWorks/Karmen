package org.crumbleworks.forge.karmen.util.asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundLibrary {

    private Random random;

    private List<Sound> fistSounds;
    private Sound jumpSound;
    
    public SoundLibrary() {
        random = new Random();
        
        initFistSounds("sfx/fist/Fist_1.wav", "sfx/fist/Fist_2.wav", "sfx/fist/Fist_3.wav");
        jumpSound = loadSound("sfx/jump/Jump.wav");
    }
    
    private Sound loadSound(String soundFileName) {
        return Gdx.audio.newSound(Gdx.files.internal(soundFileName));
    }
    
    private void initFistSounds(String... soundFileNames) {
        fistSounds = new ArrayList<>(soundFileNames.length);
        for(String soundFileName : soundFileNames) {
            fistSounds.add(loadSound(soundFileName));
        }
    }
    
    public Sound getRandomFistSound() {
        return fistSounds.get(random.nextInt(fistSounds.size()));
    }
    
    public Sound getJumpSound() {
        return jumpSound;
    }

    public void dispose() {
        fistSounds.forEach(s -> s.dispose());
        jumpSound.dispose();
    }
    
}
