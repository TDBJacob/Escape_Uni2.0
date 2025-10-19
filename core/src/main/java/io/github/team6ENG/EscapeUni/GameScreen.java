package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import static java.lang.Math.abs;

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
    private Lighting lighting;
    private boolean isDark = false;
    private float gameTimer = 300f;

    private int totalNegativeEvents = 1;
    private int totalPositiveEvents = 1;
    private int totalHiddenEvents = 1;

    private int foundNegativeEvents = 0;
    private int foundPositiveEvents = 0;
    private int foundHiddenEvents = 0;

    private boolean isPaused = false;
    private boolean isCtrl = true;
    private boolean isEPressed = false;
    private boolean exitConfirm = false;
    OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private final int mapWallsId = 90;


    Goose goose = new Goose();
    float stateTime;

    private boolean hasTorch = false;
    private boolean isCamOnGoose = false;

    private Collectable[] items = {};
    private int numOfInventoryItems = 0;

    public GameScreen(final Main game) {
        this.game = game;

        initializeMap();

        initializePlayer();

        initializeCamera();

        initializeLighting();

        initiliseGoose();

        initiliseItems();
        stateTime = 0f;
    }




    /**
     * Updates the goose light position
     *
     */

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
        player.sprite.setPosition(300, 300);   // player start position

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
    private void initiliseGoose(){

        goose.loadSprite(collisionLayer, mapWallsId);
        goose.x = 330;
        goose.y = 310;
    }

    // setup lighting system
    private void initializeLighting() {
        lighting = new Lighting();

        lighting.addLightSource("playerTorch",
            player.sprite.getX() + (player.sprite.getWidth() / 2),
            player.sprite.getY() + (player.sprite.getHeight() / 2),
            new Color(0, 0, 0, 0),
            50);
        lighting.addLightSource("playerNoTorch",
            player.sprite.getX() + (player.sprite.getWidth() / 2),
            player.sprite.getY() + (player.sprite.getHeight() / 2),
            new Color(0, 0, 0, 0.4f),
            12);
        lighting.addLightSource("gooseTorch",goose.x+ (goose.getWidth() / 2), goose.y + (goose.getHeight() / 2), new Color(1,0.2f,0.1f,0.4f), 30);

        lighting.isVisible("playerTorch", false);
        lighting.isVisible("playerNoTorch", false);
        lighting.isVisible("gooseTorch", false);


    }

    private void initiliseItems() {
        //items = new Collectable[]{new Collectable(game, "items/torch.png", 300, 200, 0.1f),
                                  //  new Collectable(game, "items/torch.png", 350, 250, 0.2f),};
    }
    // update game logic
    private void update(float delta) {

        updateCamera();

        float mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
        float mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();


        goose.x = Math.max(0, Math.min(goose.x, mapWidth - goose.getWidth()));
        goose.y = Math.max(0, Math.min(goose.y, mapHeight - goose.getHeight()));


        //If goose has not yet stolen torch, check if goose can steal torch
        if(!goose.hasStolenTorch && hasTorch){
            float dx = goose.x - player.sprite.getX();
            float dy = goose.y - player.sprite.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 30f) {
                goose.hasStolenTorch = true;
                hasTorch = false;
                isCamOnGoose = true;
                lighting.isVisible("gooseTorch", true);
                lighting.isVisible("playerTorch", false);
                lighting.isVisible("playerNoTorch", true);
            }

        }
        // Update light positions

        lighting.updateLightSource("playerTorch", player.sprite.getX() + (player.sprite.getWidth() / 2), player.sprite.getY() + (player.sprite.getHeight() / 2));
        lighting.updateLightSource("playerNoTorch", player.sprite.getX() + (player.sprite.getWidth() / 2), player.sprite.getY() + (player.sprite.getHeight() / 2));

        if(goose.hasStolenTorch){
            lighting.updateLightSource("gooseTorch", goose.x+ (goose.getWidth() / 2), goose.y + (goose.getHeight() / 2));

        }
        // Move if not paused
        if(!isPaused) {
            gameTimer -= delta;
            handleInput(delta);
            player.handleInput(delta);
            player.updatePlayer(stateTime);
            goose.moveGoose(stateTime,
                            player.sprite.getX() + (player.sprite.getWidth() / 2) - 20,
                            player.sprite.getY() + (player.sprite.getHeight() / 2),
                            player.isMoving);

            for(Collectable item :items){
                if(!item.playerHas){
                    if (item.checkInRange(player.sprite.getX(), player.sprite.getY()) && isEPressed){
                        item.Collect();

                    }
                }
            }
            isEPressed = false;

        }
        // If time up
        if(gameTimer <= 0) {
            game.setScreen(new GameOverScreen(game, "Sorry you missed the bus, better luck next time"));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            isPaused = !isPaused;
        }

        player.sprite.setX(Math.max(0, Math.min(player.sprite.getX(), mapWidth - player.sprite.getWidth())));
        player.sprite.setY(Math.max(0, Math.min(player.sprite.getY(), mapHeight - player.sprite.getHeight())));
    }

    // move camera with player
    private void updateCamera() {

        float mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
        float mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();

        float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2f;
        float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2f;

        float gooseCenterX = goose.x + goose.getWidth() / 2f;
        float gooseCenterY = goose.y + goose.getHeight() / 2f;

        float finalX = (playerCenterX + gooseCenterX) / 2f;
        float finalY = (playerCenterY + gooseCenterY) / 2f;

        // camera follows player
        float slope = 0.1f;

        if(isCamOnGoose){

            camera.position.x += (finalX - camera.position.x) * slope;
            camera.position.y += (finalY - camera.position.y) * slope;
        }
        else {
            camera.position.x += (playerCenterX - camera.position.x) * slope;
            camera.position.y += (playerCenterY - camera.position.y) * slope;
        }


        float halfWidth = camera.viewportWidth / 2f;
        float halfHeight = camera.viewportHeight / 2f;

        if (mapWidth > camera.viewportWidth) {
            camera.position.x = Math.max(halfWidth, Math.min(camera.position.x, mapWidth - halfWidth));
        } else {

            camera.position.x = mapWidth / 2f;
        }

        if (mapHeight > camera.viewportHeight) {
            camera.position.y = Math.max(halfHeight, Math.min(camera.position.y, mapHeight - halfHeight));
        } else {
            camera.position.y = mapHeight / 2f;
        }

        camera.update();

        if (DEBUG) System.out.println("Camera updated - Player center: (" +
            (player.sprite.getX() + player.sprite.getWidth() / 2) + ", " +
            (player.sprite.getY() + player.sprite.getHeight() / 2) + ")");

        if (DEBUG) System.out.println("Camera updated - Player center: (" +
            (player.sprite.getX() + player.sprite.getWidth() / 2) + ", " +
            (player.sprite.getY() + player.sprite.getHeight() / 2) + ")");

        }





    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        try {
              update(delta);

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (mapRenderer != null) {
                game.viewport.apply();
                mapRenderer.setView(camera);
                mapRenderer.render();
                Gdx.gl.glFlush();   // follow the status
            }

            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();

            stateTime += Gdx.graphics.getDeltaTime();

            if (goose != null && goose.currentGooseFrame != null) {
                game.batch.draw(goose.currentGooseFrame, goose.x, goose.y);

            }

            for(Collectable item: items){
                if(!item.playerHas){
                    item.img.draw(game.batch, 1);
                }
            }
            if (player.sprite.getTexture() != null) {
                player.sprite.draw(game.batch);

            }
            if(hasTorch){
                player.torch.draw(game.batch, 1);
            }

            int mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
            int mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();
            if(isDark) {
                game.batch.draw(lighting.render(camera, mapWidth, mapHeight), 0, 0);
            }
            game.batch.end();


            renderUI();

        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Render error: " + e.getMessage(), e);
        }

    }

    private void handleInput(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
               if (!exitConfirm) {
                    exitConfirm = true;
                } else {
                    Gdx.app.exit();
                }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            isEPressed = true;
        }


        // req2: Toggle the torch with CTRL key
        if (isCtrl && (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT))) {
            isDark = !isDark;
            goose.hasStolenTorch = false;
            isCamOnGoose = false;
            hasTorch = false;
            if (isDark) {

                hasTorch = true;
                lighting.isVisible("playerNoTorch", false);
                lighting.isVisible("playerTorch", true);
                lighting.isVisible("gooseTorch", false);
            }
        }

        // Cycle through screens for testing, remove later
        if (!isPaused && Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }

    }

    private void renderUI() {

        BitmapFont smallFont = game.gameFont;
        BitmapFont bigFont = game.menuFont;
        float worldHeight = game.viewport.getWorldHeight();
        float worldWidth = game.viewport.getWorldWidth();

        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float itemXPos = (worldWidth - (numOfInventoryItems * 32))/2;
        for(Collectable item:items) {
            if (item.playerHas){

                item.img.setPosition(itemXPos, worldHeight * 0.9f);

                item.img.draw(game.batch, 1);
                itemXPos += 32;
            }
            else if (item.checkInRange(player.sprite.getX(), player.sprite.getY())){
                String text = "Press 'e' to collect";
                GlyphLayout layout = new GlyphLayout(game.menuFont, text);
                float textX = (game.viewport.getScreenWidth() - layout.width) / 2;
                drawText(bigFont, text,Color.BLACK, textX, game.viewport.getScreenHeight()* 0.7f);

            }
        }

        // === Section 1: game status display ===
        float y = worldHeight - 20f;
        float lineSpacing = 15f;

        drawText(smallFont, String.format("Negative Events: %d/%d", foundNegativeEvents, totalNegativeEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(smallFont, String.format("Positive Events: %d/%d", foundPositiveEvents, totalPositiveEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(smallFont, String.format("Hidden Events:   %d/%d", foundHiddenEvents, totalHiddenEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(bigFont, String.format("%d:%d ", (int)gameTimer/60, (int)gameTimer % 60), Color.WHITE, worldWidth - 80f, worldHeight-20f);

        // player coordinates
        drawText(smallFont, String.format("Position: (%.1f, %.1f)", player.sprite.getX(), player.sprite.getY()), Color.LIGHT_GRAY, 20, y);
        y -= lineSpacing;

        // player's torch status
        drawText(smallFont, "Torch: " + (hasTorch ? "ON" : "OFF"), hasTorch ? Color.YELLOW : Color.WHITE, 20, y);
        y -= lineSpacing;

        // goose's torch status
        drawText(smallFont, "Goose has torch: " + (goose.hasStolenTorch ? "YES" : "NO"),
        goose.hasStolenTorch ? Color.CYAN : Color.WHITE, 20, y);
        y -= lineSpacing;

        // distance between player and goosen (only shown whenplayer has torch)
        if (hasTorch && !goose.hasStolenTorch) {
            float distance = (float) Math.hypot(goose.x - player.sprite.getX(), goose.y - player.sprite.getY());
            drawText(smallFont, String.format("Distance to goose: %.1f", distance), Color.LIGHT_GRAY, 20, y);
            y -= lineSpacing;
        }

        // === Section 2: control instructions ===
        drawText(bigFont, "Press CTRL to pick up torch", Color.ORANGE, 20, 80);
        drawText(bigFont, "Use Arrow Keys or WASD to move", Color.WHITE, 20, 55);
        drawText(bigFont, "Click mouse to return to Menu", Color.GRAY, 20, 30);

        if(isPaused) {
            smallFont.draw(game.batch, "PAUSED", game.viewport.getScreenWidth()/ 2, worldHeight - 100);
        }

        if (exitConfirm) {
            drawText(smallFont, "Press ESC again to quit", Color.RED, 20, 150);
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

        if (goose != null && goose.currentGooseFrame != null) {
            goose.currentGooseFrame.getTexture().dispose();
        }

        if (map != null) {
            map.dispose();
        }


        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }
}
