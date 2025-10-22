package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Manages building-related interactions and rendering in the game world.
 *
 * Features:
 *
 *   Detects when the player approaches the Ron Cooke building trigger zone.
 *   Allows entering/exiting the building with specific key inputs.
 *   Displays prompts and simple UI transitions.
 *
 */
public class BuildingManager {

    // ====== Building State ======
    private final Main game;
    private final GameScreen gameScreen;
    private boolean inRonCooke = false;
    private boolean showEnterPrompt = false;
    private String currentBuilding = "";

    // ====== Building Trigger Zones ======
    private final Rectangle ronCookeTrigger;

    // ====== References ======
    private final Player player;

    // ====== UI Constants ======
    private static final float PROMPT_OFFSET_Y = 50f;

    /**
     * Constructs a BuildingManager with a reference to the player.
     *
     * @param player The player instance used to track position and interaction.
     */
    public BuildingManager(Main game, GameScreen gameScreen, Player player) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        this.ronCookeTrigger = new Rectangle(100, 200, 50, 50); // Example trigger zone
    }

    /**
     * Updates building logic each frame.
     *
     * @param delta Time elapsed since the last frame (in seconds).
     */
    public void update(float delta) {
        if (inRonCooke) {
            // Inside Ron Cooke: allow exit with I key
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                exitRonCooke();
            }
        } else {
            // Outside: check if player is near the building
            checkBuildingTrigger();
            if (showEnterPrompt && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                enterRonCooke();
            }
        }
    }

    /**
     * Checks if the player is near the Ron Cooke building trigger area.
     */
    private void checkBuildingTrigger() {
        Rectangle playerRect = new Rectangle(
                player.sprite.getX(),
                player.sprite.getY(),
                player.sprite.getWidth(),
                player.sprite.getHeight()
        );

        showEnterPrompt = playerRect.overlaps(ronCookeTrigger);
        currentBuilding = showEnterPrompt ? "Ron Cooke" : "";
    }

    /**
     * Enters the Ron Cooke building.
     * Switches to an indoor view.
     */
    private void enterRonCooke() {
        inRonCooke = true;
        game.setScreen(new RonCookeScreen(game, this, gameScreen));
        gameScreen.isDark = true;
        gameScreen.hasTorch = true;
    }

    /**
     * Exits the Ron Cooke building and returns to the main world.
     */
    private void exitRonCooke() {
        inRonCooke = false;
        game.setScreen(gameScreen);
    }

    /**
     * Renders the building-related visuals.
     *
     * @param batch       The SpriteBatch used for drawing text and UI elements.
     * @param font        The font used to render messages.
     * @param worldWidth  The width of the game world (used for centering text).
     * @param worldHeight The height of the game world.
     */
    public void render(SpriteBatch batch, BitmapFont font, float worldWidth, float worldHeight) {
        renderWorldPrompts(batch, font, worldWidth, worldHeight);
    }

    /**
     * Renders prompts while the player is outside the building.
     */
    private void renderWorldPrompts(SpriteBatch batch, BitmapFont font, float worldWidth, float worldHeight) {
        if (showEnterPrompt) {
            font.setColor(Color.YELLOW);
            String text = "Press G to enter " + currentBuilding;

            GlyphLayout layout = new GlyphLayout(font, text);
            float textWidth = layout.width;
            font.draw(batch, text, (worldWidth - textWidth) / 2, worldHeight - PROMPT_OFFSET_Y);
        }
    }


    /**
     * Renders the building map (placeholder for future map logic).
     *
     * @param camera The game camera used for view rendering.
     */
    public void renderBuildingMap(OrthographicCamera camera) {
        // Placeholder for map rendering logic
    }

    /**
     * Renders building-related UI.
     *
     * @param batch       The SpriteBatch used to render.
     * @param smallFont   A smaller font for hints or labels.
     * @param bigFont     A larger font for titles or messages.
     * @param worldWidth  The game world width.
     * @param worldHeight The game world height.
     */
    public void renderUI(SpriteBatch batch, BitmapFont smallFont, BitmapFont bigFont, float worldWidth, float worldHeight) {
        render(batch, bigFont, worldWidth, worldHeight);
    }

    /**
     * Disposes building-related assets.
     * (Currently a placeholder since no disposable resources are loaded here.)
     */
    public void dispose() {
        // Nothing to dispose yet — reserved for future textures/maps
    }

    /**
     * Returns whether the player is currently inside the Ron Cooke building.
     *
     * @return true if the player is inside, false otherwise.
     */
    public boolean isInRonCooke() {
        return inRonCooke;
    }
}
