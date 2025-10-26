package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;
import java.util.Random;


/**
 * GameScreen - main gameplay screen
 *
 */
public class GameScreen implements Screen {

    private static final boolean DEBUG = false;

    private final Main game;
    private Player player;
    private BuildingManager buildingManager;

    private OrthographicCamera camera;
    private TiledMapTileLayer collisionLayer;
    private Lighting lighting;
    public boolean isDark = false;

    private final int totalNegativeEvents = 1;
    private final int totalPositiveEvents = 1;
    private final int totalHiddenEvents = 1;

    private int foundNegativeEvents = 0;
    private int foundPositiveEvents = 0;
    private int foundHiddenEvents = 0;

    private boolean isPaused = false;
    private boolean isEPressed = false;
    private boolean exitConfirm = false;
    OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private Image mapImg;
    private final int mapWallsId = 610;
    private final int tileDimensions  = 8;


    Goose goose = new Goose();
    float stateTime;

    public boolean hasTorch = false;
    private boolean isTorchOn = false;
    private boolean isCamOnGoose = false;
    boolean hasGooseFood = false;

    private Sound torchClick;
    private Sound honk;
    private final float probabilityOfHonk = 1000;
    private Sound music;

    public final HashMap<String, Collectable> items = new HashMap<String, Collectable>();
    public int numOfInventoryItems = 0;

    /**
     * Initialise the game elements
     * @param game - Instance of Main
     */
    public GameScreen(final Main game) {
        this.game = game;

        initializeMap(0);

        initializePlayer(1055,1215);

        initializeCamera();

        initializeLighting();

        initialiseGoose(950,1215);

        initialiseItems();

        music = Gdx.audio.newSound(Gdx.files.internal("soundEffects/music.mp3"));
        music.loop(game.musicVolume);
        torchClick = Gdx.audio.newSound(Gdx.files.internal("soundEffects/click.mp3"));
        buildingManager = new BuildingManager(game, this, player);
        stateTime = 0f;
    }



    /**
     * Load map and collision layer
     */
    private void initializeMap(int wallLayer) {
        Texture mapTex = new Texture(Gdx.files.internal("tileMap/map.png"));
        mapImg = new Image(mapTex);
        map = new TmxMapLoader().load("tileMap/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        int mapWallsLayer = wallLayer;
        collisionLayer = (TiledMapTileLayer)map.getLayers().get(mapWallsLayer);
    }

    /**
     * Initialise player and set its position
     */
    private void initializePlayer(int x, int y) {
        player = new Player(game);
        player.loadSprite(collisionLayer, mapWallsId, tileDimensions);
        player.sprite.setPosition(x, y);
        player.speed = 1;

    }

    /**
     * Initialise camera and set to position of player
     */
    private void initializeCamera() {
        camera = new OrthographicCamera(400,225);
        camera.position.set(
            player.sprite.getX() + player.sprite.getWidth() / 2,
            player.sprite.getY() + player.sprite.getHeight() / 2,
            0);
        camera.update();
    }

    /**
     * Initialise goose and set its position
     */
    private void initialiseGoose(int x, int y){

        goose.loadSprite(collisionLayer, mapWallsId, tileDimensions);
        goose.x = x;
        goose.y = y;


        honk = Gdx.audio.newSound(Gdx.files.internal("soundEffects/honk.mp3"));
    }


    /**
    * Add and hide light circles for player and goose
     */
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

    /**
     * Load collectable items into items class
     * They will then appear on screen and allow the player to pick them up
     */
    private void initialiseItems() {
        items.put("gooseFood", new Collectable(game, "items/gooseFood.png",   300, 200, 0.03f, true, "GameScreen"));
        items.put("keyCard", new Collectable(game, game.activeUniIDPath,   300, 200, 0.05f, false, "RonCookeScreen"));
        items.put("torch", new Collectable(game, "items/torch.png",   300, 200, 0.1f, false, "RonCookeScreen"));
        numOfInventoryItems = items.size();


    }

    /**
     * Call every frame to update game state
     * @param delta - Time since last frame
     */
    private void update(float delta) {

        if(!isPaused) {
            updateCamera();

            game.gameTimer -= delta;
            handleInput(delta);
            player.handleInput(delta);
            float mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
            float mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();


            /*
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

             */

            // Update light positions
            lighting.updateLightSource("playerTorch", player.sprite.getX() + (player.sprite.getWidth() / 2), player.sprite.getY() + (player.sprite.getHeight() / 2));
            lighting.updateLightSource("playerNoTorch", player.sprite.getX() + (player.sprite.getWidth() / 2), player.sprite.getY() + (player.sprite.getHeight() / 2));

            if(goose.hasStolenTorch){
                lighting.updateLightSource("gooseTorch", goose.x+ (goose.getWidth() / 2), goose.y + (goose.getHeight() / 2));

            }

            if(isDark){
                lighting.isVisible("playerNoTorch", true);
            }
            player.updatePlayer(stateTime);

            // Goose follow player
            goose.moveGoose(stateTime,
                            player.sprite.getX() + (player.sprite.getWidth() / 2) - 20,
                            player.sprite.getY() + (player.sprite.getHeight() / 2),
                            player.isMoving);

            // If there are baby geese, they follow the goose directly in front of them
            Goose trail = goose;
            float stateOffset = 0.075f;
            while (trail.baby != null) {
                trail.baby.moveGoose(stateTime - stateOffset,
                    trail.x,
                    trail.y,
                    player.isMoving);
                stateOffset += 0.075f;
                trail = trail.baby;
            }

            // Check if player can pick up items
            for(String key: items.keySet()){
                Collectable item = items.get(key);
                if(!item.playerHas && item.isVisible && item.originScreen.equals("GameScreen")){
                    if (item.checkInRange(player.sprite.getX(), player.sprite.getY()) && isEPressed){
                        item.Collect();
                        isEPressed = false;
                        if (key.equals("gooseFood")){
                            hasGooseFood = true;
                        }

                    }
                }
            }

            // Feed goose if player has food and in range
            float dx = (goose.x + (goose.getWidth())/2) - (player.sprite.getX()+ (player.sprite.getWidth()/2));
            float dy = (goose.y + (goose.getHeight()/2)) - (player.sprite.getY() + (player.sprite.getHeight()/2));

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < 30f && hasGooseFood && isEPressed) {

                items.remove("gooseFood");
                goose.loadBabyGoose(0);
                foundHiddenEvents += 1;
            }

            isEPressed = false;

            // Keep sprites in map boundary
            player.sprite.setX(Math.max(0, Math.min(player.sprite.getX(), mapWidth - player.sprite.getWidth())));
            player.sprite.setY(Math.max(0, Math.min(player.sprite.getY(), mapHeight - player.sprite.getHeight())));
            goose.x = Math.max(0, Math.min(goose.x, mapWidth - goose.getWidth()));
            goose.y = Math.max(0, Math.min(goose.y, mapHeight - goose.getHeight()));

        } // End isPaused

        // If time up
        if(game.gameTimer <= 0) {
            game.setScreen(new GameOverScreen(game, "Sorry you missed the bus, better luck next time"));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if(isPaused){

                music.resume();
            }
            else{
                music.pause();
            }
            isPaused = !isPaused;
        }

        buildingManager.update(delta);

        playAudio();

    }

    /**
     * Make camera follow player
     */
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
}





    @Override
    public void show() {
    }

    /**
     * Calls every frame to draw game screen
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mapRenderer != null) {
            game.viewport.apply();
            mapRenderer.setView(camera);
            mapRenderer.render();
            Gdx.gl.glFlush();
        }
        buildingManager.renderBuildingMap(camera);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        mapImg.draw(game.batch, 1);
        stateTime += delta;

        game.batch.draw(goose.currentGooseFrame, goose.x, goose.y);

        //Draw yellow baby geese
        game.batch.setColor(Color.YELLOW);
        Goose trail = goose;
        while (trail.baby != null){
            trail = trail.baby;
            if (trail.currentGooseFrame != null) {

                game.batch.draw(trail.currentGooseFrame, trail.x, trail.y, 16f, 16f);

            }

        }
        game.batch.setColor(Color.WHITE);

        // Draw uncollected items in game
        for(String key: items.keySet()){
            Collectable item = items.get(key);
            if(item.isVisible && !item.playerHas && item.originScreen.equals( "GameScreen")){
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


    }
    private void playAudio(){
        Random random = new Random();
        int doHonk = random.nextInt((int) probabilityOfHonk);
        if(doHonk == 0 && !isPaused) {
            honk.play(game.gameVolume);
        }
    }

    /**
     * Check for keyboard input
     * @param delta time in seconds since last frame
     */
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


        // Toggle the torch with click
        if(Gdx.input.justTouched() && hasTorch){
            isTorchOn = !isTorchOn;
            lighting.isVisible("playerTorch", isTorchOn);
            torchClick.play(game.gameVolume);
        }

    }

    /**
     * Draw UI on screen
     */
    private void renderUI() {

        BitmapFont smallFont = game.gameFont;
        BitmapFont bigFont = game.menuFont;
        float worldHeight = game.viewport.getWorldHeight();
        float worldWidth = game.viewport.getWorldWidth();

        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // Draw collected items in inventory bar
        // Display instructions if an item can be used or collected
        float itemXPos = (worldWidth - (numOfInventoryItems * 32))/2;
        String instructions= "";
        for(String key:items.keySet()) {
            Collectable  item = items.get(key);
            if (item.playerHas){
                item.img.setPosition(itemXPos, worldHeight * 0.9f);
                item.img.draw(game.batch, 1);
                itemXPos += 32;
                instructions = getInstructions(key);

            }
            else if (item.originScreen.equals("GameScreen") && item.isVisible && item.checkInRange(player.sprite.getX(), player.sprite.getY())) {
                instructions = "Press 'e' to collect " + key;

            }
        }
        //Draw instructions
        GlyphLayout layout = new GlyphLayout(game.menuFont, instructions);
        float textX = (worldWidth - layout.width) / 2;
        if(isDark){
            drawText(bigFont, instructions,Color.WHITE, textX, worldHeight* 0.75f);

        }else {
            drawText(bigFont, instructions, Color.BLACK, textX, worldHeight * 0.75f);
        }

        float y = worldHeight - 20f;
        float lineSpacing = 15f;

        // Requirements: Events tracker and game timer
        drawText(smallFont, String.format("Negative Events: %d/%d", foundNegativeEvents, totalNegativeEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(smallFont, String.format("Positive Events: %d/%d", foundPositiveEvents, totalPositiveEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(smallFont, String.format("Hidden Events:   %d/%d", foundHiddenEvents, totalHiddenEvents), Color.WHITE, 20, y);
        y -= lineSpacing;
        drawText(bigFont, String.format("%d:%02d ", (int)game.gameTimer/60, (int)game.gameTimer % 60), Color.WHITE, worldWidth - 80f, worldHeight-20f);

        // player coordinates
        drawText(smallFont, String.format("Position: (%.1f, %.1f)", player.sprite.getX(), player.sprite.getY()), Color.LIGHT_GRAY, 20, y);
        y -= lineSpacing;


        // Game instructions
        if(hasTorch) {
            drawText(bigFont, "Left click to switch on torch", Color.ORANGE, 20, 80);
        }
        drawText(bigFont, "Use Arrow Keys or WASD to move", Color.WHITE, 20, 55);
        drawText(bigFont, "Click mouse to return to Menu", Color.GRAY, 20, 30);

        if(isPaused) {
            smallFont.draw(game.batch, "PAUSED", (float) worldWidth / 2, worldHeight - 100);
        }

        if (exitConfirm) {
            drawText(smallFont, "Press ESC again to quit", Color.RED, 20, 150);
        }

        buildingManager.renderUI(game.batch, smallFont, bigFont, worldWidth, worldHeight);
        game.batch.end();
    }

    /**
     * Checks if inventory item can be used
     * @param key item being checked
     * @return String of instructions to display
     */
    private String getInstructions(String key) {
        if(key.equals("gooseFood")) {

            float dx = (goose.x + (goose.getWidth())/2) - (player.sprite.getX()+ (player.sprite.getWidth()/2));
            float dy = (goose.y + (goose.getHeight()/2)) - (player.sprite.getY() + (player.sprite.getHeight()/2));

            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < 30f) {
                return "Press 'e' to feed seeds to goose";
            }
        }
        return "";
    }

    /**
     * Helper method: text rendering logic to avoid repeated setColor() calls
     * @param font  The BitmapFont to use for rendering
     * @param text  The text string to display
     * @param colour The colour of the text
     * @param x     The x-coordinate for text position
     * @param y     The y-coordinate for text position
     */
    private void drawText(BitmapFont font, String text, Color colour, float x, float y) {
        font.setColor(colour);
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

        if (buildingManager != null) {
            buildingManager.dispose();
        }
    }
}
