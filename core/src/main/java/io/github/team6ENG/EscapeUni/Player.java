package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

public class Player extends SpriteAnimations{
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public TextureRegion currentPlayerFrame;
    private float speed = 1;

    public Sprite sprite;
    public enum state {
        idle,
        walkForward,
        walkBackward,
        walkLeftForward,
        walkLeftBackward,
        walkRightForward,
        walkRightBackward
    }
    public state currentState = state.idle;

    public boolean isFacingUp = false;
    public boolean isFacingLeft = false;
    public boolean isMoving;
    public boolean isMovingHorizontally;

    public Player(String file){

        super(file, 8, 7);

        animationInfo.put("idle", new Integer[]{0,8});
        animationInfo.put("walkForwards", new Integer[]{1,8});
        animationInfo.put("walkLeftForwards", new Integer[]{2,8});
        animationInfo.put("walkRightForwards", new Integer[]{6,8});
        animationInfo.put("walkRightBackwards", new Integer[]{5,8});
        animationInfo.put("walkLeftBackwards", new Integer[]{3,8});
        animationInfo.put("walkBackwards", new Integer[]{4,8});

        generateAnimation(animationInfo);

        sprite = new Sprite(animations.get("walkLeftForwards").getKeyFrame(0, true));
        sprite.setBounds(sprite.getX(), sprite.getY(), 48, 64);
    }
    public void updatePlayer(float stateTime){

        currentPlayerFrame = animations.get("walkBackwards").getKeyFrame(stateTime, true);

        if (isMoving){
            if(isFacingUp){
                if(isMovingHorizontally) {
                    if (isFacingLeft) {
                        currentPlayerFrame = animations.get("walkLeftBackwards").getKeyFrame(stateTime, true);
                    } else {
                        currentPlayerFrame = animations.get("walkRightBackwards").getKeyFrame(stateTime, true);
                    }
                }
                else{

                    currentPlayerFrame = animations.get("walkBackwards").getKeyFrame(stateTime, true);
                }
            }
            else{
                if(isMovingHorizontally) {
                    if (isFacingLeft) {
                        currentPlayerFrame = animations.get("walkLeftForwards").getKeyFrame(stateTime, true);
                    } else {
                        currentPlayerFrame = animations.get("walkRightForwards").getKeyFrame(stateTime, true);
                    }
                }
                else{
                    currentPlayerFrame = animations.get("walkForwards").getKeyFrame(stateTime, true);
                }
            }

        }
        else{

            currentPlayerFrame = animations.get("idle").getKeyFrame(stateTime, true);
        }





        sprite.setRegion(currentPlayerFrame);
    }

}
