package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public Achievement(String AchievementName, int ScoreValue, Texture AchievementTexture) {
        scoreValue = ScoreValue;
        achievementName = AchievementName;
        achievementTexture = AchievementTexture;

        framesSinceUnlock = 0;
        unlocked = false;
        unlockNotification = null;
    }

    public void unlock(Main game) {
        if (!unlocked) {
            unlocked = true;
            game.score += scoreValue;

        }
    }

    public void render(Main game) {
        SpriteBatch batch = game.batch;

        if (unlocked) {
            framesSinceUnlock += 1;

            if (framesSinceUnlock > 0 && framesSinceUnlock <= appearFrames+stayFrames+disappearFrames) {
                if (framesSinceUnlock <= appearFrames) {
                    // move achievement display down
                    if (unlockNotification == null) {
                        unlockNotification = new Sprite(achievementTexture);
                        unlockNotification.setCenterX(400);
                        unlockNotification.setY(450);
                    }
                    unlockNotification.setY(450-(framesSinceUnlock-0)*(unlockNotification.getHeight()/(appearFrames)));

                } else if (framesSinceUnlock > appearFrames+stayFrames) {
                    // move achievement display back up
                    unlockNotification.setY(450-((appearFrames+stayFrames+disappearFrames-framesSinceUnlock)*(unlockNotification.getHeight()/(disappearFrames))));
                }
                unlockNotification.draw(batch);
            } else if (unlockNotification != null) {
                unlockNotification = null;
            }

        }
    }

}
