package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Achievements can be unlocked, give points, and display a notification
 */
public class Achievement {

    public int scoreValue;
    public String achievementName;

    public int framesSinceUnlock;
    public boolean unlocked;

    public Texture achievementTexture;

    public Sprite unlockNotification;

    static int appearFrames = 60;
    static int stayFrames = 360;
    static int disappearFrames = 90;

    /**
     * Instantiate an achievement which can be unlocked later
     * @param AchievementName the name of the achievement
     * @param ScoreValue the score the achievement gives
     * @param AchievementTexture the texture of the achievement notification
     */
    public Achievement(String AchievementName, int ScoreValue, Texture AchievementTexture) {
        scoreValue = ScoreValue;
        achievementName = AchievementName;
        achievementTexture = AchievementTexture;

        framesSinceUnlock = 0;
        unlocked = false;
        unlockNotification = null;
    }

    /**
     * Unlocks the achievement and adds the score value.
     * @param game the Main game instance
     */
    public void unlock(Main game) {
        if (!unlocked) {
            unlocked = true;
            game.score += scoreValue;

        }
    }

    /**
     * Run every frame, will display the animation for the notification
     * @param game the Main game instance
     */
    public void render(Main game) {
        SpriteBatch batch = game.batch;

        // We only need to worry about this achievement once it's unlocked
        if (unlocked) {
            framesSinceUnlock += 1;

            // Split into 3 stages, sliding down, staying down, and sliding back up
            if (framesSinceUnlock > 0 && framesSinceUnlock <= appearFrames+stayFrames+disappearFrames) {
                if (framesSinceUnlock <= appearFrames) {
                    // Move achievement display down
                    if (unlockNotification == null) {
                        // Create a notification sprite if it doesn't exist
                        unlockNotification = new Sprite(achievementTexture);
                        unlockNotification.setCenterX(400);
                        unlockNotification.setY(450);
                    }
                    // Use the frames elapsed to work out where to position the notification
                    unlockNotification.setY(450-(framesSinceUnlock-0)*(unlockNotification.getHeight()/(appearFrames)));

                } else if (framesSinceUnlock > appearFrames+stayFrames) {
                    // Move achievement display back up
                    unlockNotification.setY(450-((appearFrames+stayFrames+disappearFrames-framesSinceUnlock)*(unlockNotification.getHeight()/(disappearFrames))));
                }
                // We always draw the sprite since we are on screen in this duration
                unlockNotification.draw(batch);
            } else if (unlockNotification != null) {
                // Delete if the notification has disappeared off screen
                unlockNotification = null;
            }

        }
    }

}
