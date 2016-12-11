package org.crumbleworks.forge.karmen.util.asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundLibrary {

    private Random random;

    private List<Sound> fistSounds;
    
    public SoundLibrary() {
        random = new Random();
        
        initFistSounds("sfx/fist/Fist_1.wav", "sfx/fist/Fist_2.wav", "sfx/fist/Fist_3.wav");
    }
    
    private void initFistSounds(String... soundFileNames) {
        fistSounds = new ArrayList<>(soundFileNames.length);
        for(String soundFileName : soundFileNames) {
            fistSounds.add(Gdx.audio.newSound(Gdx.files.internal(soundFileName)));
        }
    }
    
    public Sound getRandomFistSound() {
        return fistSounds.get(random.nextInt(fistSounds.size()));
    }

    public void dispose() {
        fistSounds.forEach(s -> s.dispose());
    }
    
}
