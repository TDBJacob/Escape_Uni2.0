package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Represents the interior of the Ron Cooke building.
 * Displays a black background with a welcome message.
 */
public class RonCookeScreen implements Screen {

    private final Main game;
    private final BuildingManager buildingManager;
    private final GameScreen gameScreen;  // keep reference to return
    private final BitmapFont font;

    public RonCookeScreen(Main game, BuildingManager buildingManager, GameScreen gameScreen) {
        this.game = game;
        this.buildingManager = buildingManager;
        this.gameScreen = gameScreen;
        this.font = game.menuFont; // reuse same font
    }

    @Override
    public void render(float delta) {
        // Clear to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        // Welcome message
        font.setColor(Color.WHITE);
        String welcomeText = "Welcome to the Ron Cooke Building!";
        GlyphLayout layout = new GlyphLayout(font, welcomeText);
        font.draw(game.batch, welcomeText, (worldWidth - layout.width) / 2, worldHeight / 2 + 20);

        // Exit hint
        font.setColor(Color.GRAY);
        String exitText = "(Press I to return)";
        GlyphLayout exitLayout = new GlyphLayout(font, exitText);
        font.draw(game.batch, exitText, (worldWidth - exitLayout.width) / 2, worldHeight / 2 - 20);

        game.batch.end();

        // Press I to exit building
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            game.setScreen(gameScreen);
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) { game.viewport.update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
