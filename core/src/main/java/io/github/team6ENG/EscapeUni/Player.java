package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

public class Player extends SpriteAnimations{
    private HashMap<String, Integer[]> animationInfo = new HashMap<String, Integer[]>();
    public TextureRegion currentPlayerFrame;
    private float speed = 1;
    final Main game;

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

    public Player(final Main g){

        super(g.activeSpritePath, 8, 7);

        game = g;
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

    // handle keyboard input and move player
    public void handleInput(float delta) {
        float actualSpeed = speed * 60f * delta;

        TiledMapTileLayer.Cell cell;
        int x = (int)(sprite.getX()+(sprite.getWidth()/2))/16;
        int y = (int)(sprite.getY()+(sprite.getHeight()/2))/16;
        int mapWidth = wallsLayer.getWidth();
        int mapHeight = wallsLayer.getHeight();

        System.out.println(x + " , " + y);

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
            System.out.println("Move Up (W or UP)");
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
            System.out.println("Move Down (S or DOWN)");
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
            System.out.println("Move Left (A or LEFT)");

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
            System.out.println("Move Right (D or RIGHT)");

        }
        // check boundary
        keepPlayerInBounds();

    }

    // limit inside screen
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
