package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * New
 *
 * Represents the Fish hidden event in the game
 *
 * This class handles the logic for a hidden event where a fish bites the player
 * if they remain in water for too long. When triggered, the player's score is
 * reduced and the hidden event counter is updated.
 *
 */
public class Fish {

    private Main game;

    private boolean foundHidden = false;

    /** Controls whether the fish event text should be rendered. */
    public static boolean fishText = false;

    public Fish(Main game) {
        this.game = game;
    }

    /**
     * Renders the on-screen message indicating the fish bite event
     *
     * @param batch the SpriteBatch used for rendering text
     * @param font  the BitmapFont used to draw the message
     */
    public static void renderText(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, "A fish has bitten you (-10 score)", 300, 200);
    }

    /**
     * Triggered when the player remains in water for too long.
     *
     * This method reduces the player's score by 10 points, ensures the score
     * does not drop below zero, and increments the hidden event counter the
     * first time this event occurs.
     *
     */
    public void onPlayerInWaterTooLong() {
        game.score -= 10;

        if (game.score < 0) {
            game.score = 0;
        }

        if (!foundHidden) {
            game.foundHiddenEvents += 1;
            fishText = true;
            foundHidden = true;
        }
    }
}
