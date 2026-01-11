package io.github.team9.escapefromuni;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Fish {

    private Main game;
    private boolean FoundHidden = false;
    public static Boolean fishText = false;

    public Fish(Main game) {
        this.game = game;
    }

    public static void renderText(SpriteBatch batch, BitmapFont font){
        font.draw(batch ,"A fish has bit you (-10 score)", 300 , 200);
    }


    public void onPlayerInWaterTooLong() {
        // Reduce score
        game.score -= 10;

        // Clamp score so it doesn't go negative
        if (game.score < 0) {
            game.score = 0;
        }
        if (!FoundHidden) {
            game.foundHiddenEvents += 1;
            fishText = true;
        }
    }
}
