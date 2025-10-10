package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;

import static java.lang.Math.abs;

public class Goose extends SpriteAnimations {


    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public boolean isFacingLeft = true;
    public TextureRegion currentGooseFrame;
    private float speed = 0.75f;
    private boolean isMoving;
    public Goose() {

        super("sprites/goose.png", 15, 17);

        animationInfo.put("walkLeft", new Integer[]{5,4});
        animationInfo.put("walkRight", new Integer[]{6,4});
        animationInfo.put("idleLeft", new Integer[]{16,5});
        animationInfo.put("idleRight", new Integer[]{15,5});

        generateAnimation(animationInfo);
    }


    public void moveGoose(float stateTime, float playerX, float playerY, boolean isPlayerMoving) {


        int tileX = (int)(x+8)/16;
        int tileY = (int)(y+8)/16;


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
            if(x > playerX +5 && wallsLayer.getCell(tileX-1,tileY).getTile().getId() !=mapWallsId){
                isFacingLeft = true;
                currentGooseFrame = animations.get("walkLeft").getKeyFrame(stateTime, true);
                x -= speed;
                isMoving = true;
            }
            else if(x <= playerX -5&& wallsLayer.getCell(tileX+1,tileY).getTile().getId() !=mapWallsId){
                isFacingLeft = false;
                currentGooseFrame = animations.get("walkRight").getKeyFrame(stateTime, true);
                x += speed;
                isMoving = true;
                System.out.println(2);
            }

            if(y > playerY +5 && wallsLayer.getCell(tileX,tileY-1).getTile().getId() !=mapWallsId){
                y -= speed;
                isMoving = true;
                System.out.println(3);
            }
            else if (y <= playerY -5 && wallsLayer.getCell(tileX ,tileY+1).getTile().getId() !=mapWallsId){
                y += speed;
                isMoving = true;
                System.out.println(4);
            }

            if(!isMoving){
                if(isFacingLeft){
                    currentGooseFrame = animations.get("idleLeft").getKeyFrame(stateTime, true);
                }
                else{
                    currentGooseFrame = animations.get("idleRight").getKeyFrame(stateTime, true);
                }
                System.out.println(5);
            }
        }



    }
}
