package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Represents main menu screen
 */
public class MainMenuScreen implements Screen {

    final Main game;

    /**
     *
     * @param game instance of Main
     */
    public MainMenuScreen(final Main game) {

        this.game = game;
    }

    /**
     * Draw UI
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        String title = "Escape York University";
        GlyphLayout layout = new GlyphLayout(game.menuFont, title);
        float titleX = (worldWidth - layout.width) / 2;
        game.menuFont.draw(game.batch, title, titleX, worldHeight * 0.7f);

        String subtitle = "Click anywhere to play";
        GlyphLayout subtitleLayout = new GlyphLayout(game.menuFont, subtitle);
        float subtitleX = (worldWidth - subtitleLayout.width) / 2;
        game.menuFont.draw(game.batch, subtitle, subtitleX, worldHeight * 0.6f);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new CharacterSelectScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}

