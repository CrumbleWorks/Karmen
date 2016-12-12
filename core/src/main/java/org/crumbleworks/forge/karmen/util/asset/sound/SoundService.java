package org.crumbleworks.forge.karmen.util.asset.sound;

import java.util.HashSet;
import java.util.Set;

public class SoundService {

    private SoundLibrary soundLibrary;
    private Set<SoundType> playingSounds;
    
    private boolean muted;
    
    public SoundService() {
        soundLibrary = new SoundLibrary();
        playingSounds = new HashSet<>();
    }
    
    /**
     * Plays a specific sound once.
     * 
     * @param type Soundtype
     */
    public void play(SoundType type) {
        if(!muted) {
            play(type, false);
        }
    }
    
    /**
     * Plays a specific sound.
     * 
     * @param type Soundtype
     * @param Whether to loop the sound or not
     */
    public void play(SoundType type, boolean loop) {
        if(!muted) {
            playingSounds.add(type);
            long soundId = soundLibrary.getSound(type).play();
            soundLibrary.getSound(type).setLooping(soundId, loop);
        }
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
    
    /**
     * Toggles mute state on all sounds.
     */
    public void toggleMute() {
        muted = !muted;
    }
    
    public void dispose() {
        soundLibrary.dispose();
    }
    
}
