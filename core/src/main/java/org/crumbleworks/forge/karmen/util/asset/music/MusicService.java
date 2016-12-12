package org.crumbleworks.forge.karmen.util.asset.music;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MusicService {

    private final float DEFAULT_VOLUME = 1.0f;
    
    private MusicLibrary musicLibrary;
    private Set<MusicType> playingMusic;
    
    private float globalVolume;
    
    public MusicService() {
        musicLibrary = new MusicLibrary();
        playingMusic = new HashSet<>();
        
        globalVolume = DEFAULT_VOLUME;
    }
    
    /**
     * Plays a specific music.
     * <li>Music is set to looping
     * <li>If the Music is already running, nothing will happen
     * 
     * @param type Musictype
     * 
     * @see #play(MusicType, boolean)
     * @see #play(MusicType, boolean, boolean)
     */
    public void play(MusicType type) {
        play(type, true);
    }
    
    /**
     * Plays a specific music.
     * 
     * <li>If the Music is already running, nothing will happen
     * 
     * @param type Musictype
     * @param loop Whether to loop the music or not
     * 
     * @see #play(MusicType)
     * @see #play(MusicType, boolean, boolean)
     */
    public void play(MusicType type, boolean loop) {
        play(type, loop, true);
    }
    
    /**
     * Plays a specific music.
     * 
     * @param type Musictype
     * @param loop Whether to loop the music or not
     * @param stopRunning Whether a running instance of <code>type</code> is to be stopped
     * 
     * @see #play(MusicType)
     * @see #play(MusicType, boolean)
     */
    public void play(MusicType type, boolean loop, boolean stopRunning) {
        if(playingMusic.contains(type)) {
            if(stopRunning) {
                stop(type);
            }
            else {
                return;
            }
        }
        
        playingMusic.add(type);
        musicLibrary.getMusic(type).setLooping(loop);
        musicLibrary.getMusic(type).setVolume(globalVolume);
        musicLibrary.getMusic(type).play();
    }
    
    /**
     * Stops all currently playing music.
     */
    public void stop() {
        playingMusic.forEach(m -> musicLibrary.getMusic(m).stop());
        playingMusic.clear();
    }
    
    /**
     * Stops a specific music.
     * 
     * @param type Musictype
     */
    public void stop(MusicType type) {
        musicLibrary.getMusic(type).stop();
        playingMusic.remove(type);
    }
    
    /**
     * Toggles mute state on all music.
     */
    public void toggleMute() {
        if(globalVolume == 0f) {
            applyVolumeOnAll(DEFAULT_VOLUME);
            globalVolume = DEFAULT_VOLUME;
        }
        else {
            applyVolumeOnAll(0f);
            globalVolume = 0f;
        }
    }
    
    private void applyVolumeOnAll(float volume) {
        for(MusicType type : MusicType.values()) {
            musicLibrary.getMusic(type).setVolume(volume);
        }
    }
    
    public void update() {
        playingMusic = playingMusic.stream().filter(m -> musicLibrary.getMusic(m).isPlaying()).collect(Collectors.toSet());
    }
    
    public void dispose() {
        musicLibrary.dispose();
    }
    
}
