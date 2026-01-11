package io.github.team9.escapefromuni;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Represents collectable game items
 */
public class Collectible {
    Main game;
    public boolean playerHas = false;
    public Image img;
    public float x, y;
    public boolean isVisible;
    public String originScreen;
    private AudioManager audioManager;

    public Collectible(final Main game, String path, float x, float y, float scale, boolean isVisible, String originScreen, AudioManager audioManager) {
        this.game = game;
        this.x = x;
        this.y = y;
        if (path.equals("test")) {
            Texture tex = null;
            img = null;
        }
        else {
            Texture tex = new Texture(path);
            img = new Image(tex);
            img.setPosition(x, y);
            img.setScale(scale);
        }
        this.isVisible = isVisible;
        this.originScreen = originScreen;
        this.audioManager = audioManager;

    }

    /**
     * Check if player is in range
     *
     * @param playerX
     * @param playerY
     * @return true if in range
     */
    public boolean checkInRange(float playerX, float playerY) {

        if(!playerHas) {
            float dx = x -playerX;
            float dy = y - playerY ;
            float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
            return distance < 30f;
        }

        return false;
    }

    /**
     * Mark as collected and scale to inventory bar size
     */
    public void Collect() {
        playerHas = true;
        if (img != null) {
            img.setScale(1f, 1f);
            float multiplier = img.getHeight() / img.getWidth();
            img.setWidth(24);
            img.setHeight(24 * multiplier);

            audioManager.playCollect();
        }
    }

    /**
     * play sound
     */
    public void playSound() {
        audioManager.playCollect();
    }

    //the following are mainly for testing but can be used for other purposes where there is a risk of changing coordinates accidentally
    public float getCollectibleX() {
        return x;
    }

    public float getCollectibleY() {
        return y;
    }

    public boolean getPlayerHas() {
        return this.playerHas;
    }
}
