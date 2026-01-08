package io.github.team9.escapefromuni;


import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class Trap {

    Main game;
    public Image img;
    public float x, y;
    public boolean isVisible;
    public String originScreen;

    public boolean isActive = false; // Check if  Trap is active
    private String escapeKey = "F"; // Escape trap key
    private float trapDuration = 3f; // Duration since trap was turned on
    private final float MAX_TRAP_DURATION = 30f; // Maximum time it can stay turned on
    private float activationRadius = 100f; // Activation Radius
    public boolean playerWasInRange = false; // Check if Player within range

    public Trap(final Main game, Image img, float x, float y, boolean isVisible, String originScreen) {
        this.game = game;
        this.img = img;
        this.x = x; // center x
        this.y = y; // center y
        this.isVisible = isVisible;
        this.originScreen = originScreen;
        // default activation radius = half the max dimension of the image (so standing on sprite triggers)
        float imgW = img.getWidth() * img.getScaleX();
        float imgH = img.getHeight() * img.getScaleY();
        this.activationRadius = Math.max(imgW, imgH) / 2f;
    }

    /**
     * Check if player's center point is on the trap image (requires player to be on the trap)
     * Trap coordinates (x,y) are interpreted as the trap center.
     * Uses a small activation radius (in pixels) around the trap center.
     * @param playerCenterX player's center x coordinate
     * @param playerCenterY player's center y coordinate
     * @return true if player's center is inside the trap active radius
     */
    public boolean checkInRange(float playerCenterX, float playerCenterY) {
        float dx = playerCenterX - x;
        float dy = playerCenterY - y;
        float distSq = dx * dx + dy * dy;
        return distSq <= activationRadius * activationRadius;
    }

    public void setActivationRadius(float radius) {
        this.activationRadius = radius;
    }

    /**
     * Activate the trap when player steps on it
     */
    public void activateTrap() {
        if (!isActive) {
            isActive = true;
            trapDuration = 0f;
        }
    }

    /**
     * Deactivate the trap
     */
    public void deactivateTrap() {
        isActive = false;
        trapDuration = 0f;
    }

    /**
     * Check if player pressed the escape key
     * @param keyPressed the key that was pressed
     * @return true if the correct key was pressed
     */
    public boolean checkEscapeInput(String keyPressed) {
        if (keyPressed == null) return false;
        return keyPressed.equalsIgnoreCase(escapeKey) && isActive;
    }

    /**
     * Update trap duration and deactivate if time expires
     * @param delta time since last frame
     */
    public void update(float delta) {
        if (isActive) {
            trapDuration += delta;
            if (trapDuration >= MAX_TRAP_DURATION) {
                deactivateTrap();
            }
        }
    }

    public float getSlowMultiplier() {
        return 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setEscapeKey(String key) {
        this.escapeKey = key;
    }

    public float getTrapDuration() {
        return trapDuration;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public String getOriginScreen() {
        return originScreen;
    }

    public float getActivationRadius() {
        return activationRadius;
    }
}
