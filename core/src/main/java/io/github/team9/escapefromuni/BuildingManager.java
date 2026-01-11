package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
    private boolean inLangwith = false;
    private boolean showEnterPrompt = false;
    private String currentBuilding = "";
    private float lockedOutTime = 0;
    public float originalPlayerX;
    public float originalPlayerY;
    AudioManager audioManager;
    // ====== Building Trigger Zones ======
    private final Rectangle ronCookeTrigger;
    private final Rectangle langwithTrigger;

    // ====== References ======
    private final Player player;

    // ====== UI Constants ======
    private static final float PROMPT_OFFSET_Y = 50f;

    /**
     * Constructs a BuildingManager with a reference to the player.
     *
     * @param player The player instance used to track position and interaction.
     */
    public BuildingManager(Main game, GameScreen gameScreen, Player player, AudioManager audioManager) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.player = player;
        this.ronCookeTrigger = new Rectangle(350, 455, 50, 50);
        this.langwithTrigger = new Rectangle(1078, 1215, 50, 50);
        this.audioManager = audioManager;
    }

    /**
     * Updates building logic each frame.
     *
     * @param delta Time elapsed since the last frame (in seconds).
     */
    public void update(float delta) {
        if (inRonCooke || inLangwith) {
            // Inside Ron Cooke: allow exit with I key
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                exitBuilding();
            }
        }
        else {
            // Outside: check if player is near the building
            checkBuildingTrigger();
            if (showEnterPrompt && currentBuilding.equals("Ron Cooke") && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                //collects entry coordinates to be used when exiting
                originalPlayerX = Player.getPlayerX();
                originalPlayerY = Player.getPlayerY();
                enterRonCooke();
            }
            else if(showEnterPrompt && currentBuilding.equals("Langwith") && Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                if(gameScreen.items.get("keyCard").playerHas) {
                    //collects entry coordinates to be used when exiting
                    originalPlayerX = Player.getPlayerX();
                    originalPlayerY = Player.getPlayerY();
                    gameScreen.items.get("keyCard").playSound();
                    enterLangwith();
                }
                else{
                    lockedOutTime = 5;
                    audioManager.playNoAccess();
                }
            }
        }
    }

    /**
     * Checks if the player is near the Ron Cooke building trigger area.
     */
    public void checkBuildingTrigger() {
        Rectangle playerRect = new Rectangle(
                player.sprite.getX(),
                player.sprite.getY(),
                player.sprite.getWidth(),
                player.sprite.getHeight()
        );
        currentBuilding = "";
        showEnterPrompt = false;
        if(playerRect.overlaps(ronCookeTrigger)) {
            showEnterPrompt = true;
            currentBuilding = "Ron Cooke";
        }
        else if (playerRect.overlaps(langwithTrigger)) {

            showEnterPrompt = true;
            currentBuilding = "Langwith";
        }



    }

    protected void setRonCookeScreen() {
        game.setScreen(new RonCookeScreen(game, this, gameScreen));
    }

    /**
     * Enters the Ron Cooke building.
     * Switches to an indoor view.
     */
    public void enterRonCooke() {
        inRonCooke = true;
        gameScreen.audioManager.stopFootsteps();
        setRonCookeScreen();
        gameScreen.isDark = true;
        gameScreen.hasTorch = true;
        gameScreen.lighting.isVisible("playerNoTorch", true);
        gameScreen.lighting.isVisible("gooseNoTorch", true);

    }

    protected void setLangwithScreen() {
        game.setScreen(new LangwithScreen(game, this, gameScreen));
    }

    /**
     * Enters the Langwith College.
     * Switches to an indoor view.
     */
    public void enterLangwith() {
        inLangwith = true;
        gameScreen.audioManager.stopFootsteps();
        setLangwithScreen();
    }

    /**
     * Exits the Ron Cooke building and returns to the main world.
     */
    public void exitBuilding() {
        inRonCooke = false;
        inLangwith = false;
        player.sprite.setScale(1);
        //return player to the position they entered at
        Player.setPlayerX(originalPlayerX);
        Player.setPlayerY(originalPlayerY);
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
        lockedOutTime -= Gdx.graphics.getDeltaTime();
    }

    /**
     * Renders prompts while the player is outside the building.
     */
    GlyphLayout layout = new GlyphLayout();
    private void renderWorldPrompts(SpriteBatch batch, BitmapFont font, float worldWidth, float worldHeight) {
        if (showEnterPrompt) {
            font.setColor(Color.YELLOW);
            String text = "Press G to enter " + currentBuilding;

            layout = new GlyphLayout(font, text);
            float textWidth = layout.width;
            font.draw(batch, text, (worldWidth - textWidth) / 2, worldHeight - PROMPT_OFFSET_Y);
        }

        if(lockedOutTime > 0){
            String lockedOutText = "Looks like you've lost your keycard\nhead over to Ron Cooke for a new one";
            layout = new GlyphLayout(font, lockedOutText);
            float textWidth = layout.width;
            font.setColor(Color.RED);
            font.draw(batch, lockedOutText, (worldWidth - textWidth) / 2, worldHeight - PROMPT_OFFSET_Y - 30);
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

    public boolean getShowEnterPrompt() {
        return showEnterPrompt;
    }

    public String getCurrentBuilding() {
        return currentBuilding;
    }

    /**
     * Returns whether the player is currently inside the Ron Cooke building.
     *
     * @return true if the player is inside, false otherwise.
     */
    public boolean isInRonCooke() {
        return inRonCooke;
    }
    /**
     * Returns whether the player is currently inside Langwith.
     *
     * @return true if the player is inside, false otherwise.
     */
    public boolean isInLangwith() {
        return inLangwith;
    }
}
