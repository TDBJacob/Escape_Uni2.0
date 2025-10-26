package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import javax.swing.*;

/**
 * Represents the interior of the Ron Cooke building.
 * Displays a black background with a welcome message.
 */
public class LangwithScreen implements Screen {

    private final Main game;
    private final BuildingManager buildingManager;
    private final GameScreen gameScreen;  // keep reference to return
    private final BitmapFont font;
    private Player player;
    private float stateTime;


    float worldWidth;
    float worldHeight ;

    private boolean isEPressed = false;

    public LangwithScreen(Main game, BuildingManager buildingManager, GameScreen gameScreen) {
        this.game = game;
        this.buildingManager = buildingManager;
        this.gameScreen = gameScreen;
        this.font = game.menuFont;

        initialisePlayer((int) 60,(int) game.viewport.getWorldHeight()/2);
        stateTime = 0;
    }

    /**
     * Initialise player and set its position
     */
    private void initialisePlayer(int x, int y) {
        player = new Player(game);
        player.loadSprite(new TiledMapTileLayer( 400, 225, 16,16), 0, 16);
        player.sprite.setPosition(x, y);
        player.sprite.setScale(4);
        player.speed  = 2;

    }


    @Override
    public void render(float delta) {
        // Clear to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.handleInput(delta);
        player.updatePlayer(stateTime);
        game.batch.begin();

        player.sprite.draw(game.batch);

        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();


        for(String key: gameScreen.items.keySet()){
            Collectable item = gameScreen.items.get(key);
            if(item.isVisible && !item.playerHas && item.originScreen.equals("RonCookeScreen")){
                item.img.draw(game.batch, 1);
                if (item.checkInRange(player.sprite.getX()- (player.sprite.getHeight()/2) , player.sprite.getY() - (player.sprite.getHeight()/2)) && isEPressed){
                    item.Collect();
                    System.out.println("Here");
                    isEPressed = false;

                }
            }
        }
        game.batch.end();
        renderUI();
        game.gameTimer -= delta;
        buildingManager.update(delta);
        stateTime += delta;
        isEPressed = Gdx.input.isKeyJustPressed(Input.Keys.E);

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
    private void renderUI(){

        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        float itemXPos = (worldWidth - (gameScreen.numOfInventoryItems * 32))/2;
        String instructions = "";

        // draw items either in inventory or on screen
        for(String key:gameScreen.items.keySet()) {

            Collectable item = gameScreen.items.get(key);
            if (item.playerHas){
                item.img.setPosition(itemXPos, worldHeight * 0.8f);
                item.img.draw(game.batch, 1);
                itemXPos += 32;
            }
            else if (item.originScreen.equals("RonCookeScreen") && item.isVisible && item.checkInRange(player.sprite.getX()- 32, player.sprite.getY() - (player.sprite.getHeight()/2))){
                if (instructions.isEmpty()) {
                    instructions = "Press 'e' to collect " + key;
                }
            }
        }
        GlyphLayout layout = new GlyphLayout(game.menuFont, instructions);
        float textX = (worldWidth - layout.width) / 2;
        drawText(font, instructions, Color.WHITE, textX, worldHeight * 0.75f);
        drawText(font, String.format("%d:%02d ", (int)game.gameTimer/60, (int)game.gameTimer % 60), Color.WHITE, worldWidth - 80f, worldHeight-20f);

        font.setColor(Color.GRAY);
        String exitText = "Press G to leave";
        GlyphLayout exitLayout = new GlyphLayout(font, exitText);
        font.draw(game.batch, exitText, (worldWidth - exitLayout.width) / 2, worldHeight - 20);

        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) { game.viewport.update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
