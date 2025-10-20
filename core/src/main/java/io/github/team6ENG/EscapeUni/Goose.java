package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.HashMap;
import static java.lang.Math.abs;

/**
 * Represents and controls a goose
 */
public class Goose extends SpriteAnimations {
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public boolean isFacingLeft = true;
    public boolean hasStolenTorch = false;
    public TextureRegion currentGooseFrame;
    private float speed = 0.75f;
    public boolean isMoving;
    private TiledMapTileLayer.Cell cell;

    public Goose baby = null;

    /**
     * Generate goose and its animations
     */
    public Goose() {
        super("sprites/goose.png", 15, 17);

        // HashMap<String, Integer[]> animationInfo:
        //      key - Name of animation
        //      Value - Array representing row of animation on sprite sheet and number of frames it contains
        animationInfo.put("walkLeft", new Integer[]{5,4});
        animationInfo.put("walkRight", new Integer[]{6,4});
        animationInfo.put("idleLeft", new Integer[]{16,5});
        animationInfo.put("idleRight", new Integer[]{15,5});

        generateAnimation(animationInfo);
    }

    /**
     * Check if goose is obstructed
     * @param tileX y position of tile approaching
     * @param tileY y position of tile approaching
     * @return true if goose can move, else false
     */
    private boolean isMoveAllowed(int tileX, int tileY) {
        // check map boundaries
        if (tileX < 0 || tileY < 0 || tileX >= wallsLayer.getWidth() || tileY >= wallsLayer.getHeight()) {
            return false;
        }

        // check if tile is empty (null) or contains a wall
        cell = wallsLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) {
            return true;
        }

        return cell.getTile().getId() != mapWallsId;

    }

    /**
     * Move goose towards target and update animations
     * @param stateTime time in seconds since last frame
     * @param followX x of target
     * @param followY y of target
     * @param isPlayerMoving is player moving
     */
    public void moveGoose(float stateTime, float followX, float followY, boolean isPlayerMoving) {
        int tileX = (int)(x+ getWidth() / 2) / 16;
        int tileY = (int)(y+ getHeight() / 2) / 16;

        float distance = (float) Math.sqrt(((x-followX) * (x-followX)) + ((y-followY)*(y-followY)));
        // If target is in range, idle
        if( distance <= 20 && !isPlayerMoving){
            if(isFacingLeft){
                currentGooseFrame = animations.get("idleLeft").getKeyFrame(stateTime, true);
            }
            else{
                currentGooseFrame = animations.get("idleRight").getKeyFrame(stateTime, true);
            }
        }
        else{
            isMoving = false;
            if (x > followX +5 && isMoveAllowed(tileX-1,tileY)) {
                isFacingLeft = true;
                currentGooseFrame = animations.get("walkLeft").getKeyFrame(stateTime, true);
                x -= speed;
                isMoving = true;
            }
            else if(x <= followX -5 && isMoveAllowed(tileX+1,tileY)) {
                isFacingLeft = false;
                currentGooseFrame = animations.get("walkRight").getKeyFrame(stateTime, true);
                x += speed;
                isMoving = true;
            }

            if(y > followY +5 && isMoveAllowed(tileX,tileY-1)) {
                y -= speed;
                isMoving = true;
            }
            else if (y <= followY -5 && isMoveAllowed(tileX ,tileY+1)) {
                y += speed;
                isMoving = true;
            }

            if(!isMoving){
                if(isFacingLeft){
                    currentGooseFrame = animations.get("idleLeft").getKeyFrame(stateTime, true);
                }
                else{
                    currentGooseFrame = animations.get("idleRight").getKeyFrame(stateTime, true);
                }
            }
        }
    }

    /**
     *
     * @return width of goose
     */
    public float getWidth() {
        return currentGooseFrame != null ? currentGooseFrame.getRegionWidth() : 16f; // default value
    }

    /**
     *
     * @return height of goose
     */
    public float getHeight() {

        return currentGooseFrame != null ? currentGooseFrame.getRegionHeight() : 16f;
    }

    /**
     * Recursively generate a trail of baby geese
     * Each goose points to the next baby goose
     * Base case: Goose index, only generate 5 geese
     * @param gooseIndex number of current goose in the trail
     */
    public void loadBabyGoose(int gooseIndex){
        if (gooseIndex < 5) {
            baby = new Goose();

            baby.loadSprite(wallsLayer, mapWallsId);
            baby.x = x;
            baby.y = y;
            baby.speed = speed*0.9f;

            baby.loadBabyGoose(gooseIndex + 1);

        }
    }
}


