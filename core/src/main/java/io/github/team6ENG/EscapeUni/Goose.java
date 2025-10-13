package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import java.util.HashMap;
import static java.lang.Math.abs;

public class Goose extends SpriteAnimations {
    private static final boolean DEBUG = false;
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public boolean isFacingLeft = true;
    private boolean hasStolenTorch = false;
    public TextureRegion currentGooseFrame;
    private float speed = 0.75f;
    private boolean isMoving;
    private TiledMapTileLayer.Cell cell;

    public Goose() {
        super("sprites/goose.png", 15, 17);

        animationInfo.put("walkLeft", new Integer[]{5,4});
        animationInfo.put("walkRight", new Integer[]{6,4});
        animationInfo.put("idleLeft", new Integer[]{16,5});
        animationInfo.put("idleRight", new Integer[]{15,5});

        generateAnimation(animationInfo);
    }

    public synchronized void setStolenTorch(boolean value) {
        this.hasStolenTorch = value;
    }

    public synchronized boolean hasStolenTorch() {
        return this.hasStolenTorch;
    }

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

    public void moveGoose(float stateTime, float playerX, float playerY, boolean isPlayerMoving) {
        int tileX = (int)(x+ getWidth() / 2) / 16;
        int tileY = (int)(y+ getHeight() / 2) / 16;

        // If player is in range, idle
        if(abs(x-playerX) + abs(y-playerY) <= 50 && !isPlayerMoving){
            if(isFacingLeft){
                currentGooseFrame = animations.get("idleLeft").getKeyFrame(stateTime, true);
            }
            else{
                currentGooseFrame = animations.get("idleRight").getKeyFrame(stateTime, true);
            }
        }
        else{
            isMoving = false;
            if (x > playerX +5 && isMoveAllowed(tileX-1,tileY)) {
                isFacingLeft = true;
                currentGooseFrame = animations.get("walkLeft").getKeyFrame(stateTime, true);
                x -= speed;
                isMoving = true;
            }
            else if(x <= playerX -5 && isMoveAllowed(tileX+1,tileY)) {
                isFacingLeft = false;
                currentGooseFrame = animations.get("walkRight").getKeyFrame(stateTime, true);
                x += speed;
                isMoving = true;
            }

            if(y > playerY +5 && isMoveAllowed(tileX,tileY-1)) {
                y -= speed;
                isMoving = true;
            }
            else if (y <= playerY -5 && isMoveAllowed(tileX ,tileY+1)) {
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

    public float getWidth() {
        return currentGooseFrame != null ? currentGooseFrame.getRegionWidth() : 16f; // default value
    }

    public float getHeight() {
        return currentGooseFrame != null ? currentGooseFrame.getRegionHeight() : 16f;
    }

    /**
     * Checks and executes torch stealing logic
     * @param game The game screen instance
     * @param playerX Player's X coordinate
     * @param playerY Player's Y coordinate
     */
    public void checkAndStealTorch(GameScreen game, float playerX, float playerY) {
        if (!hasStolenTorch && game.hasTorch()) {
            float dx = x - playerX;
            float dy = y - playerY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 30f) {
                hasStolenTorch = true;
                game.onGooseStealTorch();
                if (DEBUG) System.out.println("Goose touched player and stole the torch!");
            }
        }
    }
}


