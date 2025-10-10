package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * GameScreen - main gameplay screen
 *
 * This is class handle player movement and simple test UI.
 *
 */
public class GameScreen implements Screen {

    private static final boolean DEBUG = false;

    private final Main game;
    private Player player;
    private OrthographicCamera camera;
    private TiledMapTileLayer collisionLayer;
    private SimpleLighting lighting;

    private boolean isPaused = false;
    OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private final int mapWallsId = 90;


    Goose goose = new Goose();
    private int gooseLightIndex = -1;
    float stateTime;

    private boolean hasTorch = false;   // torch status
    private int playerLightIndex = -1;  // index of player with torch

    public boolean hasTorch() {
        return hasTorch;
    }

    public void onGooseStealTorch() {
    if (hasTorch) {
        if (DEBUG) System.out.println("Goose stole the torch! Transferring light...");

        // player loses the torch
        setHasTorch(false);

        // goose stole the torch
        if (lighting != null) {
            float gooseCenterX = goose.x + goose.getWidth() / 2f;
            float gooseCenterY = goose.y + goose.getHeight() / 2f;

            // purple light
            Color purple = new Color(0.7f, 0.3f, 1f, 0.9f);
            lighting.addLight(gooseCenterX, gooseCenterY, 35f, purple);
            gooseLightIndex = lighting.getLights().size() - 1;

            if (DEBUG) System.out.println("💜 Purple goose light created at (" + gooseCenterX + ", " + gooseCenterY + ")");
        }
    }
}

    private float gooseFlickerTime = 0f;

    /**
     * Updates the goose light position
     *
     */
    private void updateGooseLightPosition() {
        if (goose.hasStolenTorch() && lighting != null && gooseLightIndex != -1) {
            if (!lighting.getLights().isEmpty()) {
                SimpleLighting.LightSource gooseLight = lighting.getLights().get(gooseLightIndex);

                // center light on goose
                float gooseCenterX = goose.x + goose.getWidth() / 2f;
                float gooseCenterY = goose.y + goose.getHeight() / 2f;

                gooseLight.x = gooseCenterX;
                gooseLight.y = gooseCenterY;

                // apply flickering effect
                gooseFlickerTime += Gdx.graphics.getDeltaTime();
                float flicker = 1f + 0.12f * (float) Math.sin(gooseFlickerTime * 8);
                gooseLight.radius = 50f * flicker;
            }
        }
    }

    public GameScreen(final Main game) {
        this.game = game;

        initializeMap();

        initializePlayer();

        initializeCamera();

        initializeLighting();

        stateTime = 0f;
        goose.loadSprite(collisionLayer, mapWallsId);
        goose.x = game.viewport.getScreenWidth() / 2;
        goose.x += 20;
        goose.y = game.viewport.getScreenHeight() / 2;
    }

    // load map and collision layer
    private void initializeMap() {
        map = new TmxMapLoader().load("tileMap/testMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        int mapWallsLayer = 0;
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(mapWallsLayer);
    }

    // initialize player sprite
    private void initializePlayer() {

        player = new Player(game);
        player.loadSprite(collisionLayer, mapWallsId);
        player.sprite.setPosition((game.viewport.getScreenWidth()/2)-8, (game.viewport.getScreenHeight()/2)-8);   // player start position

    }

    // initialize camera to follow player
    private void initializeCamera() {
        camera = new OrthographicCamera(400,225);
        camera.position.set(
            player.sprite.getX() + player.sprite.getWidth() / 2,
            player.sprite.getY() + player.sprite.getHeight() / 2,
            0);
        camera.update();
    }

    // setup lighting system
    private void initializeLighting() {
        lighting = new SimpleLighting();

        // req1: remove the light for the start of the game
        // hasTorch = false

        if (DEBUG) System.out.println("Lighting initialized - No light at game start");

    }

    // add a light centered on player
    private void addPlayerLight() {
        if (lighting != null && hasTorch) {
            float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2;
            float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2;

            Color dark = new Color(1f, 1f, 1f, 0.85f);
            lighting.addLight(playerCenterX, playerCenterY, 80f, dark);

            playerLightIndex = lighting.getLights().size() - 1;

            if (DEBUG) System.out.println("Player light added at: (" + playerCenterX + ", " + playerCenterY + ")");
        }
    }

    // update game logic
    private void update(float delta) {
        updateCamera();

        goose.checkAndStealTorch(this, player.sprite.getX(), player.sprite.getY());


        if(!isPaused) {
            handleInput(delta);
            player.handleInput(delta);
            player.updatePlayer(stateTime);
            updateCamera();
            updateLightPositions();
            goose.moveGoose(stateTime, player.sprite.getX() + (player.sprite.getWidth() / 2) - 20,
                player.sprite.getY() + (player.sprite.getHeight() / 2),
                player.isMoving);
        }
    }

    // move camera with player
    private void updateCamera() {
        camera.position.set(
            player.sprite.getX() + player.sprite.getWidth() / 2,
            player.sprite.getY() + player.sprite.getHeight() / 2,
            0);
        camera.update();

        System.out.println("Camera updated - Player center: (" +
            (player.sprite.getX() + player.sprite.getWidth() / 2) + ", " +
            (player.sprite.getY() + player.sprite.getHeight() / 2) + ")");
        if (DEBUG) System.out.println("Camera updated - Player center: (" +
            (player.sprite.getX() + player.sprite.getWidth() / 2) + ", " +
            (player.sprite.getY() + player.sprite.getHeight() / 2) + ")");

    }

    // keep light centered on player
    private void updateLightPositions() {
        // renew player lighting position
        if (hasTorch && lighting != null && !lighting.getLights().isEmpty()) {

            SimpleLighting.LightSource playerLight = lighting.getLights().get(playerLightIndex);

            float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2;
            float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2;

            playerLight.x = playerCenterX;
            playerLight.y = playerCenterY;

            if (DEBUG) System.out.println("Light updated at player center: (" + playerCenterX + ", " + playerCenterY + ")");
        }
           // update goose light position
            updateGooseLightPosition();

    }

    public SimpleLighting getLighting() {
        return lighting;
    }

    public void gainTorch() {
        if (!hasTorch) {
            hasTorch = true;
            addPlayerLight();
            if (lighting != null && gooseLightIndex != -1 && gooseLightIndex < lighting.getLights().size()) {
                lighting.getLights().remove(gooseLightIndex);
                gooseLightIndex = -1;
            }
            if (DEBUG) System.out.println("Torch acquired! Goose light removed.");
        }
    }

    public void loseTorch() {
        if (hasTorch) {
            hasTorch = false;
            if (lighting != null && playerLightIndex != -1 && playerLightIndex < lighting.getLights().size()) {
                lighting.getLights().remove(playerLightIndex);
                playerLightIndex = -1;
                System.out.println("Torch lost! Light removed.");
            }
        }
    }

    public void setHasTorch(boolean value) {
        hasTorch = value;
        if (!value) {
            // remove player's light source
            if (lighting != null && playerLightIndex != -1 && playerLightIndex < lighting.getLights().size()) {
                lighting.getLights().remove(playerLightIndex);
                playerLightIndex = -1;
                if (DEBUG) System.out.println("Player torch removed due to goose steal.");
            }
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.viewport.apply();

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();


        stateTime += Gdx.graphics.getDeltaTime();

        game.batch.draw(goose.currentGooseFrame, goose.x, goose.y);

        player.sprite.draw(game.batch);

        game.batch.end();

        if (lighting != null) {
            lighting.render(camera);
        }

        renderUI();

        // Cycle through screens for testing, remove later
        if (Gdx.input.justTouched()) {

            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
        }

}

    private void handleInput(float delta) {


        // req2: Toggle the torch with CTRL key
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
            if (!hasTorch) {
                gainTorch();
            }
        }
    }

    private void renderUI() {
    SpriteBatch batch = game.batch;
    BitmapFont font = game.menuFont;
    float worldHeight = game.viewport.getWorldHeight();

    batch.setProjectionMatrix(game.viewport.getCamera().combined);
    batch.begin();

    // === Section 1: game status display ===
    float y = worldHeight - 20f;
    float lineSpacing = 25f;

    // game title & basic information
    drawText(font, "Main Menu Screen", Color.WHITE, 20, y);
    y -= lineSpacing;
    drawText(font, "Add game :)", Color.WHITE, 20, y);
    y -= lineSpacing;

    // player coordinates
    drawText(font, String.format("Position: (%.1f, %.1f)", player.sprite.getX(), player.sprite.getY()), Color.LIGHT_GRAY, 20, y);
    y -= lineSpacing;

    // player's torch status
    drawText(font, "Torch: " + (hasTorch ? "ON" : "OFF"), hasTorch ? Color.YELLOW : Color.WHITE, 20, y);
    y -= lineSpacing;

    // goose's torch status
    drawText(font, "Goose has torch: " + (goose.hasStolenTorch() ? "YES" : "NO"),
            goose.hasStolenTorch() ? Color.CYAN : Color.WHITE, 20, y);
    y -= lineSpacing;

    // distance between player and goosen (only shown whenplayer has torch)
    if (hasTorch && !goose.hasStolenTorch()) {
        float distance = (float) Math.hypot(goose.x - player.sprite.getX(), goose.y - player.sprite.getY());
        drawText(font, String.format("Distance to goose: %.1f", distance), Color.LIGHT_GRAY, 20, y);
        y -= lineSpacing;
    }

    // === Section 2: control instructions ===
    drawText(font, "Press CTRL to pick up torch", Color.ORANGE, 20, 80);
    drawText(font, "Use Arrow Keys or WASD to move", Color.WHITE, 20, 55);
    drawText(font, "Click mouse to return to Menu", Color.GRAY, 20, 30);

        if(isPaused) {

            game.menuFont.draw(game.batch, "PAUSED", game.viewport.getScreenWidth()/ 2, worldHeight - 100);
        }
        game.batch.end();
}

    /**
     * Helper method: Unified text rendering logic to avoid repeated setColor() calls
     * @param font  The BitmapFont to use for rendering
     * @param text  The text string to display
     * @param color The color of the text
     * @param x     The x-coordinate for text position
     * @param y     The y-coordinate for text position
     */
    private void drawText(BitmapFont font, String text, Color color, float x, float y) {
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // release texture memory
        if (player.sprite.getTexture() != null) {
            player.sprite.getTexture().dispose();
        }

        if (map != null) {
            map.dispose();
        }

        if (lighting != null) {
            lighting.dispose();
        }

        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }
}
