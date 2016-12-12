package org.crumbleworks.forge.karmen.screen.arena;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.objects.Thing;
import org.crumbleworks.forge.karmen.util.AnimationConstants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class DanceFloor implements Thing {
    private final Karmen game;
    private final int arenaFactor;
    
    private final float botLeftX;
    private final float botLeftY;
    
    private Animation danceFloorAnimation;
    
    private float stateTime;

    public DanceFloor(final Karmen game, final int arenaFactor, float botLeftX, float botLeftY) {
        this.game = game;
        this.arenaFactor = arenaFactor;
        
        this.botLeftX = botLeftX;
        this.botLeftY = botLeftY;
        
        Gdx.app.debug("ARENA", "creating dance floor");
        danceFloorAnimation = new Animation(
                AnimationConstants.DUR_FRACT_ANIMATION_FRAME_LENGTH,
                Array.with(game.getTextureLibrary().getDanceFloorRegions()),
                PlayMode.LOOP);
    }
    
    @Override
    public final void update(float delta) {
        stateTime += delta;
        TextureRegion currentAnimationFrame = danceFloorAnimation.getKeyFrame(stateTime);
        game.getBatch().draw(
                currentAnimationFrame,
                botLeftX,
                botLeftY,
                currentAnimationFrame.getRegionWidth() * arenaFactor,
                currentAnimationFrame.getRegionHeight() * arenaFactor);
    }
}
