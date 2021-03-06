package org.crumbleworks.forge.karmen.util.asset.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class MusicService {

    private final float DEFAULT_VOLUME = 0.5f;
    
    private MusicLibrary musicLibrary;
    private MusicType playingMusic;
    
    private float globalVolume;
    
    public MusicService() {
        musicLibrary = new MusicLibrary();
        playingMusic = null;
        
        globalVolume = DEFAULT_VOLUME;
    }
    
    /**
     * Plays a specific music.
     * <li>Music is set to looping
     * <li>If the Music is already running, nothing will happen (as in: it will continue playing without interruption)
     * 
     * @param type {@link MusicType}
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
     * @param type {@link MusicType}
     * @param loop Whether to loop the music or not
     * 
     * @see #play(MusicType)
     * @see #play(MusicType, boolean)
     */
    public void play(MusicType type, boolean loop) {
        if(playingMusic != null) {
                stop();
        }
        
        playingMusic = type;
        musicLibrary.getMusic(type).setLooping(loop);
        musicLibrary.getMusic(type).setVolume(globalVolume);
        musicLibrary.getMusic(type).play();
    }
    
    public void schedule(MusicType type, boolean loop) {
        musicLibrary.getMusic(playingMusic).setOnCompletionListener(new OnCompListener(type, loop, this));
    }
    
    /**
     * Stops all currently playing music.
     */
    public void stop() {
        musicLibrary.getMusic(playingMusic).stop();
        playingMusic = null;
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
    }
    
    public void dispose() {
        musicLibrary.dispose();
    }
    
    /* ***********************************************************************
     * INFO STUFF
     */
    
    /**
     * @return the {@link MusicType} currently playing
     */
    public MusicType playing() {
        return playingMusic;
    }
    
    /* ***********************************************************************
     * ON COMPLETION LISTENER
     */
    
    static final class OnCompListener implements OnCompletionListener {
        private final MusicType type;
        private final boolean loop;
        private final MusicService service;
        
        public OnCompListener(MusicType type, boolean loop, MusicService service) {
            this.type = type;
            this.loop = loop;
            this.service = service;
        }
        
        @Override
        public void onCompletion(Music music) {
            service.play(type, loop);
        }
    }
}
