package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Represnts game over screen
 */
public class GameOverScreen implements Screen {


    private final Main game;
    private Stage stage;
    private String deathMessage;

    /**
     * Initialse scene
     * @param game instance of Main
     * @param deathMsg reason of game over
     */
    public GameOverScreen(final Main game, String deathMsg) {
        this.game = game;
        deathMessage = deathMsg;
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

    }

    /**
     * Draw screen
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.PINK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();


        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        String title = "Game over screen";
        GlyphLayout layout = new GlyphLayout(game.menuFont, title);
        float titleX = (worldWidth - layout.width) / 2;
        game.menuFont.draw(game.batch, title, titleX, worldHeight * 0.7f);


        GlyphLayout subtitleLayout = new GlyphLayout(game.menuFont, deathMessage);
        float subtitleX = (worldWidth - subtitleLayout.width) / 2;
        game.menuFont.draw(game.batch, deathMessage, subtitleX, worldHeight * 0.6f);

        game.batch.end();

        stage.act();
        stage.draw();

        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

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

    }
}
