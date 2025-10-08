package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    private Sprite player;
    private float speed = 2f;   // move speed, maybe can change later.

    OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private int mapWallsLayer = 0;
    private int mapWallsId = 90;

    TiledMapTileLayer wallsLayer;

    private OrthographicCamera cam;

    Goose goose = new Goose();
    float stateTime;

    public GameScreen(final Main game) {
        this.game = game;

        // load player image, or create fallback if not found
        try {
            Texture playerTexture = new Texture(Gdx.files.internal("player.jpg"));
            player = new Sprite(playerTexture);
        } catch (Exception e) {
            System.out.println("Failed to load player.png, creating fallback graphic");

            // create a new player
            Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.CYAN);
            pixmap.fill();
            pixmap.setColor(Color.ORANGE);
            pixmap.fillCircle(8, 8, 5);
            player = new Sprite(new Texture(pixmap));
            pixmap.dispose();
        }
        player.setPosition((game.viewport.getScreenWidth()/2)-8, (game.viewport.getScreenHeight()/2)-8);   // player start position


        map = new TmxMapLoader().load("tileMap/testMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1);
        wallsLayer = (TiledMapTileLayer)map.getLayers().get(mapWallsLayer);

        cam = new OrthographicCamera(400,300);
        cam.position.set(game.viewport.getScreenWidth() / 2f, game.viewport.getScreenHeight() / 2f, 0);


        stateTime = 0f;

        goose.loadGoose(wallsLayer, mapWallsId);
        goose.x = game.viewport.getScreenWidth() / 2;
        goose.x += 20;
        goose.y = game.viewport.getScreenHeight() / 2;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // clean screen
        ScreenUtils.clear(Color.WHITE);

        // process input each frame
        handleInput(delta);
        goose.moveGoose(stateTime, player.getX(),  player.getY());
        stateTime += Gdx.graphics.getDeltaTime();
        game.viewport.apply();
        cam.update();

        mapRenderer.setView((OrthographicCamera) cam);
        mapRenderer.render();

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);



        // draw player and text
        game.batch.begin();
        player.draw(game.batch);



        game.batch.draw(goose.currentGooseFrame, goose.x, goose.y);

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.menuFont.draw(game.batch, "Main game screen", 20, worldHeight - 20);
        game.menuFont.draw(game.batch, "Add game :)", 20, worldHeight - 50);

        String line1 = "Use arrow or WASD to move player.";
        String line2 = "Click mouse to go back to menu (testing only)";

        // calculate text width
        GlyphLayout layout1 = new GlyphLayout(game.menuFont, line1);
        GlyphLayout layout2 = new GlyphLayout(game.menuFont, line2);

        // center alignment
        float maxWidth = Math.max(layout1.width, layout2.width);
        float centerX = (worldWidth - maxWidth) / 2;

        float line1Y = worldHeight * 0.6f;
        game.menuFont.draw(game.batch, line1, centerX, line1Y);

        float line2Y = line1Y - layout1.height - 20f;
        game.menuFont.draw(game.batch, line2, centerX, line2Y);


        game.batch.end();
        // Cycle through screens for testing, remove later
        if (Gdx.input.justTouched()) {

            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    // handle keyboard input and move player
    private void handleInput(float delta) {
        float actualSpeed = speed * 60f * delta;

        int x = (int)(player.getX()+8)/16;
        int y = (int)(player.getY()+8)/16;

        TiledMapTileLayer.Cell cell;
        // move up
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {

            if(wallsLayer.getCell(x,y+1).getTile().getId() !=mapWallsId) {
                cam.translate(0, actualSpeed, 0);
                player.translateY(actualSpeed);
                System.out.println("Move Up (W or UP)");}
        }

        // move down
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(wallsLayer.getCell(x,y-1).getTile().getId() !=mapWallsId) {
                player.translateY(-actualSpeed);
                cam.translate(0, -actualSpeed, 0);
                System.out.println("Move Down (S or DOWN)");
            }
        }

        // move left
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(wallsLayer.getCell(x-1,y).getTile().getId() !=mapWallsId) {
                player.translateX(-actualSpeed);
                cam.translate(-actualSpeed, 0, 0);
                System.out.println("Move Left (A or LEFT)");
            }
        }

        // move right
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(wallsLayer.getCell(x+1,y).getTile().getId() !=mapWallsId) {
                player.translateX(actualSpeed);
                cam.translate(actualSpeed, 0, 0);
                System.out.println("Move Right (D or RIGHT)");
            }
        }

        // limit inside screen
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        if (player.getX() < 0) player.setX(0);

        if (player.getY() < 0) player.setY(0);

        if (player.getX() > worldWidth - player.getWidth())
            player.setX(worldWidth - player.getWidth());

        if (player.getY() > worldHeight - player.getHeight())
            player.setY(worldHeight - player.getHeight());
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
        if (player.getTexture() != null) {
            player.getTexture().dispose();
        }


    }
}
