package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class SpriteAnimations  {

    public HashMap<String, Animation<TextureRegion>> animations = new HashMap<String, Animation<TextureRegion>>();
    public float x;
    public float y;
    private HashMap<String, Integer[]> animationInfo;
    private int COLUMNS;
    private int ROWS;
    private Texture sheet;


    protected TiledMapTileLayer wallsLayer;
    protected int mapWallsId;


    // Used to generate animations dictionary which contains all animations for that sprite
    public SpriteAnimations(String file, int SheetCols, int SheetRows) {
        COLUMNS = SheetCols;
        ROWS = SheetRows;
        sheet= new Texture(Gdx.files.internal(file));
    }
    public void loadSprite(TiledMapTileLayer walls, int id) {
        wallsLayer = walls;
        mapWallsId = id;
    }
    protected void generateAnimation( HashMap<String, Integer[]> animInfo){
        //animInfo [0] = Row of animation, [1] = number of frames
        animationInfo = animInfo;

        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / COLUMNS,
            sheet.getHeight() / ROWS);

        TextureRegion[] frames;
        for(String key : animationInfo.keySet())
        {
            frames = new TextureRegion[animationInfo.get(key)[1]];
            for (int i = 0; i < animationInfo.get(key)[1]; i++ ){
                frames[i] = tmp[animationInfo.get(key)[0]][i];
                System.out.println(frames[i]);
            }
            animations.put(key, new Animation<TextureRegion>(0.25f, frames));

        }
    }

}
