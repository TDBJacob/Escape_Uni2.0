package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Collectable {
    Main game;
    public boolean playerHas = false;
    public Image img;
    public float x, y;
    public Collectable(final Main game, String path, float x, float y, float scale){
        this.game = game;
        this.x = x;
        this.y = y;
        Texture tex = new Texture(path);
        img = new Image(tex);
        img.setPosition(x, y);
        img.setScale(scale);

    }


    public boolean checkInRange(float playerX, float playerY){

        if(!playerHas) {
            float dx = x - playerX;
            float dy = y - playerY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            return distance < 30f;


            }

        return false;
    }

    public void Collect(){

        playerHas = true;
        img.setScale(1f, 1f);
        img.setWidth(30);
        img.setHeight(30);

    }


}
