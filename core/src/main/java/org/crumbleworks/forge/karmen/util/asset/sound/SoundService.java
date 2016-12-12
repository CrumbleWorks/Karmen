package org.crumbleworks.forge.karmen.util.asset.sound;

import java.util.HashSet;
import java.util.Set;

public class SoundService {

    private SoundLibrary soundLibrary;
    private Set<SoundType> playingSounds;
    
    public SoundService() {
        soundLibrary = new SoundLibrary();
        playingSounds = new HashSet<>();
    }
    
    /**
     * Plays a specific sound.
     * 
     * @param type Soundtype
     */
    public void play(SoundType type) {
        playingSounds.add(type);
        soundLibrary.getSound(type).play();
    }
    
    /**
     * Stops all currently playing sounds.
     */
    public void stop() {
        playingSounds.forEach(s -> soundLibrary.getSound(s).stop());
        playingSounds.clear();
    }
    
    /**
     * Stops a specific sound.
     * 
     * @param type Soundtype
     */
    public void stop(SoundType type) {
        soundLibrary.getSound(type).stop();
        playingSounds.remove(type);
    }
    
    public void dispose() {
        soundLibrary.dispose();
    }
    
}
