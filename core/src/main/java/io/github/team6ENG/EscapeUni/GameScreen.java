package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * GameScreen - main gameplay screen.
 *
 * This is class handle player movement and simple test UI.
 * The logic is very basic now but can expand later.
 */
public class GameScreen implements Screen {

    private final Main game;
    private Player player;
    private OrthographicCamera camera;
    private TiledMapTileLayer collisionLayer;
    private SimpleLighting lighting;
    private float speed = 2f;   // move speed, maybe can change later.

    OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private final int mapWallsId = 90;


    Goose goose = new Goose();
    float stateTime;

    public GameScreen(final Main game) {
        this.game = game;

        initializeMap();

        initializePlayer();

        initializeCamera();

        //initializeLighting();

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
        // load player image, or create fallback if not found
        player = new Player(game.activeSpritePath);
        player.sprite.setPosition((game.viewport.getScreenWidth()/2)-8, (game.viewport.getScreenHeight()/2)-8);   // player start position

    }

    // initialize camera to follow player
    private void initializeCamera() {
        camera = new OrthographicCamera(400,300);
        camera.position.set(
            player.sprite.getX() + player.sprite.getWidth() / 2,
            player.sprite.getY() + player.sprite.getHeight() / 2,
            0);
        camera.update();
    }

    // setup lighting system
    private void initializeLighting() {
        lighting = new SimpleLighting();

        // add a light centered on player
        float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2;
        float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2;

        lighting.addLight(
            playerCenterX,
            playerCenterY,
            200f,
            new Color(1f, 1f, 1f, 0.91f)
        );

         System.out.println("Lighting initialized at player center: (" + playerCenterX + ", " + playerCenterY + ")");

        // environment lighting null

    }

    // update game logic
    private void update(float delta) {

        handleInput(delta);
        updateCamera();
        //updateLightPositions();

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

    }

    // keep light centered on player
    private void updateLightPositions() {
        // renew player lighting position
        if (lighting != null && !lighting.getLights().isEmpty()) {
            SimpleLighting.LightSource playerLight = lighting.getLights().get(0);
            float playerCenterX = player.sprite.getX() + player.sprite.getWidth() / 2;
            float playerCenterY = player.sprite.getY() + player.sprite.getHeight() / 2;

            playerLight.x = playerCenterX;
            playerLight.y = playerCenterY;

            System.out.println("Light at player center: (" + playerCenterX + ", " + playerCenterY + ")");
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

        camera.update();
        game.viewport.apply();

        mapRenderer.setView(camera);
        mapRenderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        goose.moveGoose(stateTime, player.sprite.getX(),  player.sprite.getY());

        player.updatePlayer(stateTime);
        stateTime += Gdx.graphics.getDeltaTime();

        game.batch.draw(goose.currentGooseFrame, goose.x, goose.y);

        player.sprite.draw(game.batch);

        game.batch.end();

        updateLightPositions();
        if (lighting != null) {
            lighting.render(camera);
        }

        renderUI();

        // Cycle through screens for testing, remove later
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

}

    // handle keyboard input and move player
    private void handleInput(float delta) {
        float actualSpeed = speed * 60f * delta;

        TiledMapTileLayer.Cell cell;
        int x = (int)(player.sprite.getX()+(player.sprite.getWidth()/2))/16;
        int y = (int)(player.sprite.getY()+(player.sprite.getHeight()/2))/16;
        int mapWidth = collisionLayer.getWidth();
        int mapHeight = collisionLayer.getHeight();

        System.out.println(x + " , " + y);

        player.isMoving = false;
        player.isFacingLeft = false;
        player.isFacingUp = false;
        player.isMovingHorizontally = false;
        // move up
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (y + 1 < mapHeight) {
                cell = collisionLayer.getCell(x, y + 1);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    player.sprite.translateY(actualSpeed);
                    player.isMoving = true;
                    player.isFacingUp = true;
                }
            }
                System.out.println("Move Up (W or UP)");
        }

        // move down
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (y - 1 >= 0) {
                cell = collisionLayer.getCell(x, y -1);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    player.sprite.translateY(-actualSpeed);
                    player.isMoving = true;
                    player.isFacingUp = false;
                }
            }
                System.out.println("Move Down (S or DOWN)");
        }

        // move left
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (x - 1 >= 0) {
                cell = collisionLayer.getCell(x -1 , y);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    player.sprite.translateX(-actualSpeed);
                    player.isMoving = true;
                    player.isFacingLeft = true;
                    player.isMovingHorizontally = true;
                }
            }
                System.out.println("Move Left (A or LEFT)");

        }

        // move right
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (x + 1 < mapWidth) {
                cell = collisionLayer.getCell(x + 1, y);
                if (cell == null || cell.getTile().getId() != mapWallsId) {
                    player.sprite.translateX(actualSpeed);
                    player.isMoving = true;
                    player.isFacingLeft = false;
                    player.isMovingHorizontally = true;
                }
            }
                System.out.println("Move Right (D or RIGHT)");

        }
        // check boundary
        keepPlayerInBounds();

    }

    // limit inside screen
    private void keepPlayerInBounds() {
        float tileSize = 16f;

        float worldWidth = game.viewport.getWorldWidth() * tileSize;
        float worldHeight = game.viewport.getWorldHeight() * tileSize;

        if (player.sprite.getX() < 0) player.sprite.setX(0);

        if (player.sprite.getY() < 0) player.sprite.setY(0);

        if (player.sprite.getX() > worldWidth - player.sprite.getWidth())
            player.sprite.setX(worldWidth - player.sprite.getWidth());

        if (player.sprite.getY() > worldHeight - player.sprite.getHeight())
            player.sprite.setY(worldHeight - player.sprite.getHeight());
    }

    private void renderUI() {
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.menuFont.setColor(Color.WHITE);
        game.menuFont.draw(game.batch, "Main menu screen", 20, worldHeight - 20);
        game.menuFont.draw(game.batch, "Add game :)", 20, worldHeight - 50);

        String positionText = String.format("Position: (%.1f, %.1f)", player.sprite.getX(), player.sprite.getY());
        game.menuFont.draw(game.batch, positionText, 20, worldHeight - 80);

        String line1 = "Use arrow or WASD to move player.";
        String line2 = "Click mouse to go back to menu (testing only)";

        // calculate text width
        GlyphLayout layout1 = new GlyphLayout(game.menuFont, line1);
        GlyphLayout layout2 = new GlyphLayout(game.menuFont, line2);

        // center alignment
        float maxWidth = Math.max(layout1.width, layout2.width);
        float centerX = (worldWidth - maxWidth) / 2;

        float line1Y = 80f;
        game.menuFont.draw(game.batch, line1, centerX, line1Y);

        float line2Y = line1Y - layout1.height - 10f;
        game.menuFont.draw(game.batch, line2, centerX, line2Y);

        game.batch.end();
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
