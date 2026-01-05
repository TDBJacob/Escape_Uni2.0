package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents and controls enemies
 */
public class Enemy extends SpriteAnimations{
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public TextureRegion currentPlayerFrame;
    public float speed = 1.25f;
    final Main game;
    protected int mapLangwithBarriersId;
    protected int mapWaterId;

    public Sprite sprite;
    private Texture torchTexture;
    public Image torch;

    public ArrayList<Coordinate> WalkPoints;

    public boolean isFacingUp = false;
    public boolean isFacingLeft = false;
    public boolean isMoving;
    public boolean isMovingHorizontally;
    public boolean inWater;
    public boolean hasEnteredLangwith;
    private AudioManager audioManager;
    boolean isFootsteps = false;
    /**
     * Initialises the player and its animations
     * @param g current instance of Main
     */
    public Enemy(final Main g, AudioManager audioManager, ArrayList<Coordinate> walkPoints) {
        super(g.activeSpritePath, 8, 7);
        this.audioManager = audioManager;
        this.WalkPoints = walkPoints;
        game = g;
        // HashMap<String, Integer[]> animationInfo:
        //      key - Name of animation
        //      Value - Array representing row of animation on sprite sheet and index of start and end frames
        animationInfo.put("idle", new Integer[]{0,0,8});
        animationInfo.put("walkForwards", new Integer[]{1,0,8});
        animationInfo.put("walkLeftForwards", new Integer[]{2,0,8});
        animationInfo.put("walkRightForwards", new Integer[]{6,0,8});
        animationInfo.put("walkRightBackwards", new Integer[]{5,0,8});
        animationInfo.put("walkLeftBackwards", new Integer[]{3,0,8});
        animationInfo.put("walkBackwards", new Integer[]{4,0,8});

        generateAnimation(animationInfo, 0.3f);

        Coordinate startingPoint = WalkPoints.get(0);

        sprite = new Sprite(animations.get("walkLeftForwards").getKeyFrame(0, true));
        sprite.setBounds(sprite.getX(), sprite.getY(), startingPoint.getX(), startingPoint.getY());


    }

    /**
     * Check for keyboard input and move player
     * @param delta time in seconds since last frame
     */
    public void handleInput(float delta, float speedModifier) {
        float actualSpeed = speed * speedModifier * 60f * delta;

        TiledMapTileLayer.Cell cell;
        int x = (int)(sprite.getX()+(sprite.getWidth()/2))/tileDimensions;
        int y = (int)(sprite.getY()+(sprite.getHeight()/2))/tileDimensions;
        int mapWidth = wallsLayer.getWidth();
        int mapHeight = wallsLayer.getHeight();



    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
