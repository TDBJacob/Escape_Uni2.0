package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class SpriteAnimations  {

    public HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
    public float x;
    public float y;
    private HashMap<String, Integer[]> animationInfo = new HashMap<>();
    private int COLUMNS;
    private int ROWS;
    private Texture sheet;

    // Used to generate animations dictionary which contains all animations for that sprite
    public SpriteAnimations(String file, int SheetCols, int SheetRows) {
        COLUMNS = SheetCols;
        ROWS = SheetRows;
        sheet= new Texture(Gdx.files.internal(file));
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
            animations.put(key, new Animation<>(0.5f, frames));

        }
    }

     public void dispose() {
        if (sheet != null) {
            sheet.dispose();
        }
    }


}
