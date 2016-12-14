package org.crumbleworks.forge.karmen.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.crumbleworks.forge.karmen.Karmen;
import org.crumbleworks.forge.karmen.scenes.SceneManager.Scenes;
import org.crumbleworks.forge.karmen.util.NeonColors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class AboutScreen extends KarmenScreen {

    private final Karmen game;
    
    private List<String> creditLines;
    private int currentTopCreditLineIndex;
    private int creditLinesOffset;
    private float relatvieCreditPosition;
    private float creditSpeed;
    
    private List<Animation> explosionAnimations;
    private List<Animation> activeExplosionAnimations;
    private float explosionAnimationDuration;
    private int maxAmountConcurrentExplosionAnimations;
    private float explosionAnimationStateTime;
    
    public AboutScreen(final Karmen game) {
        super(game, new boolean[]{true, true});
        this.game = game;
        
        currentTopCreditLineIndex = 0;
        creditLinesOffset = 30;
        relatvieCreditPosition = 0f;
        creditSpeed = 0.25f;
        
        explosionAnimationDuration = 0.1f;
        maxAmountConcurrentExplosionAnimations = 5;
        explosionAnimationStateTime = 0f;
        activeExplosionAnimations = new ArrayList<>(maxAmountConcurrentExplosionAnimations);
        
        readAboutFile("about/NNPH.about");
        initExplosionAnimation();
    }

    @Override
    public void enter() {
        //FIXME RESET CREDITS ROLL
    }

    @Override
    public void leave() {
    }
    
    private void readAboutFile(String aboutFileName) {
        creditLines = new ArrayList<>(Arrays.asList(Gdx.files.internal(aboutFileName).readString().split("\\r?\\n")));
    }
    
    private void initExplosionAnimation() {
        int width = 16;
        int height = 16;
        
        TextureRegion[][] tempTextureRegions = TextureRegion.split(game.getTextureLibrary().getExplosionTexture(), width, height);
        
        int rows = tempTextureRegions.length;
        
        explosionAnimations = new ArrayList<>(rows);
        for(int i = 0; i < rows; i++) {
            explosionAnimations.add(new Animation(explosionAnimationDuration, Array.with(tempTextureRegions[i]), PlayMode.NORMAL));
        }
    }

    @Override
    void krender(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.getBatch().begin();
        
        drawCredits();
//        drawExplosions(delta);
        
        game.getBatch().end();
    }

    private void drawCredits() {
        game.getFontLibrary().ARIAL.setColor(NeonColors.Orange);
        
        if(currentTopCreditLineIndex < creditLines.size()) {
            currentTopCreditLineIndex = (int) (relatvieCreditPosition / creditLinesOffset);
            if(currentTopCreditLineIndex >= creditLines.size()) {
                currentTopCreditLineIndex = creditLines.size() - 1;
            }
        }
        else {
            currentTopCreditLineIndex = creditLines.size() - 1;
        }
        
        relatvieCreditPosition += creditSpeed;
        
        for(int i = 0; i <= currentTopCreditLineIndex; i++) {
            float fontX = Karmen.SCREEN_WIDTH / 4;
            float fontY = relatvieCreditPosition - (i * creditLinesOffset);
            
            game.getFontLibrary().ARIAL.draw(game.getBatch(), creditLines.get(i), fontX, fontY, Karmen.SCREEN_WIDTH / 2, Align.center, true);
            
            if((i == (creditLines.size() - 1)) && fontY > Karmen.SCREEN_HEIGHT + creditLinesOffset) {
                game.getSceneManager().changeScene(Scenes.MENU);;
            }
        }
    }
    
//    private void drawExplosions(float delta) {
//        if(activeExplosionAnimations.size() < maxAmountConcurrentExplosionAnimations) {
//            activeExplosionAnimations.add(getRandomExplosionAnimation());
//        }
//        
//        explosionAnimationStateTime += delta;
//    }
    
//    private Animation getRandomExplosionAnimation() {
//        return explosionAnimations.get(new Random().nextInt(explosionAnimations.size()));
//    }
}
