package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;

/**
 * Represents and controls the main player character
 */
public class Player extends SpriteAnimations{
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public TextureRegion currentPlayerFrame;
    public float speed = 1;
    final Main game;

    public Sprite sprite;
    private Texture torchTexture;
    public Image torch;
    private static final boolean DEBUG = false;

    public boolean isFacingUp = false;
    public boolean isFacingLeft = false;
    public boolean isMoving;
    public boolean isMovingHorizontally;

    Sound footSteps;
    boolean isFootsteps = false;
    /**
     * Initialises the player and its animations
     * @param g current instance of Main
     */
    public Player(final Main g){
        super(g.activeSpritePath, 8, 7);

        game = g;
        // HashMap<String, Integer[]> animationInfo:
        //      key - Name of animation
        //      Value - Array representing row of animation on sprite sheet and number of frames it contains
        animationInfo.put("idle", new Integer[]{0,8});
        animationInfo.put("walkForwards", new Integer[]{1,8});
        animationInfo.put("walkLeftForwards", new Integer[]{2,8});
        animationInfo.put("walkRightForwards", new Integer[]{6,8});
        animationInfo.put("walkRightBackwards", new Integer[]{5,8});
        animationInfo.put("walkLeftBackwards", new Integer[]{3,8});
        animationInfo.put("walkBackwards", new Integer[]{4,8});

        generateAnimation(animationInfo, 0.3f);

        sprite = new Sprite(animations.get("walkLeftForwards").getKeyFrame(0, true));
        sprite.setBounds(sprite.getX(), sprite.getY(), 48, 64);

        torchTexture = new Texture("items/torch.png");
        torch = new Image(torchTexture);
        torch.setPosition(sprite.getX(), sprite.getY());
        torch.setScale(0.02f);
        torch.setRotation(180);

        footSteps = Gdx.audio.newSound(Gdx.files.internal("soundEffects/footsteps.mp3"));

    }

    /**
     * Check for keyboard input and move player
     * @param delta time in seconds since last frame
     */
    public void handleInput(float delta) {
        float actualSpeed = speed * 60f * delta;

        TiledMapTileLayer.Cell cell;
        int x = (int)(sprite.getX()+(sprite.getWidth()/2))/16;
        int y = (int)(sprite.getY()+(sprite.getHeight()/2))/16;
        int mapWidth = wallsLayer.getWidth();
        int mapHeight = wallsLayer.getHeight();

        isMoving = false;
        isFacingLeft = false;
        isFacingUp = false;
        isMovingHorizontally = false;
        // move up
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (y + 1 < mapHeight) {
                cell = wallsLayer.getCell(x, y + 1);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    sprite.translateY(actualSpeed);
                    isMoving = true;
                    isFacingUp = true;
                }
            }
        }

        // move down
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (y - 1 >= 0) {
                cell = wallsLayer.getCell(x, y -1);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    sprite.translateY(-actualSpeed);
                    isMoving = true;
                    isFacingUp = false;
                }
            }
        }

        // move left
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (x - 1 >= 0) {
                cell = wallsLayer.getCell(x -1 , y);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    sprite.translateX(-actualSpeed);
                    isMoving = true;
                    isFacingLeft = true;
                    isMovingHorizontally = true;
                }
            }

        }

        // move right
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (x + 1 < mapWidth) {
                cell = wallsLayer.getCell(x + 1, y);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    sprite.translateX(actualSpeed);
                    isMoving = true;
                    isFacingLeft = false;
                    isMovingHorizontally = true;
                }
            }

        }
        // check boundary
        keepPlayerInBounds();
        if(isMoving && !isFootsteps){
            isFootsteps = true;
            footSteps.loop(.2f * game.gameVolume);
        }
        else if (!isMoving){
            footSteps.stop();
            isFootsteps = false;
        }

    }

    /**
     * Ensure player can't leave the map
     */
    private void keepPlayerInBounds() {
        float tileSize = 16f;

        float worldWidth = game.viewport.getWorldWidth() * tileSize;
        float worldHeight = game.viewport.getWorldHeight() * tileSize;

        if (sprite.getX() < 0) sprite.setX(0);

        if (sprite.getY() < 0) sprite.setY(0);

        if (sprite.getX() > worldWidth - sprite.getWidth())
            sprite.setX(worldWidth - sprite.getWidth());

        if (sprite.getY() > worldHeight - sprite.getHeight())
            sprite.setY(worldHeight - sprite.getHeight());
    }

    /**
     * Update player animation and torch position
     * @param stateTime time in seconds since last frame
     */
    public void updatePlayer(float stateTime){


        if (isMoving){
            if(isFacingUp){
                if(isMovingHorizontally) {
                    if (isFacingLeft) {
                        currentPlayerFrame = animations.get("walkLeftBackwards").getKeyFrame(stateTime, true);
                        torch.setRotation(150);
                        torch.setPosition(sprite.getX() + 22, sprite.getY() + 30);
                    } else {
                        currentPlayerFrame = animations.get("walkRightBackwards").getKeyFrame(stateTime, true);
                        torch.setRotation(30);
                        torch.setPosition(sprite.getX() + 26, sprite.getY() + 25);
                    }
                }
                else{

                    currentPlayerFrame = animations.get("walkBackwards").getKeyFrame(stateTime, true);
                    torch.setRotation(120);
                    torch.setPosition(sprite.getX() + 22, sprite.getY() + 30);
                }
            }
            else{
                if(isMovingHorizontally) {
                    if (isFacingLeft) {
                        currentPlayerFrame = animations.get("walkLeftForwards").getKeyFrame(stateTime, true);
                        torch.setRotation(180);
                        torch.setPosition(sprite.getX() + 24, sprite.getY() + 30);
                    } else {
                        currentPlayerFrame = animations.get("walkRightForwards").getKeyFrame(stateTime, true);
                        torch.setRotation(0);
                        torch.setPosition(sprite.getX() + 26, sprite.getY() + 25);
                    }
                }
                else{
                    currentPlayerFrame = animations.get("walkForwards").getKeyFrame(stateTime, true);
                    torch.setRotation(-90);
                    torch.setPosition(sprite.getX() + 20, sprite.getY() + 25);
                }
            }

        }
        else{

            currentPlayerFrame = animations.get("idle").getKeyFrame(stateTime, true);
            torch.setRotation(-60);
            torch.setPosition(sprite.getX() + 22, sprite.getY() + 26);
        }
        sprite.setRegion(currentPlayerFrame);

    }

}
