package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {


    private final Main game;

    public GameScreen(final Main game) {
        this.game = game;
    }

        @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.RED);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.menuFont.draw(game.batch, "Main menu screen", game.viewport.getScreenWidth()/200 - 2, game.viewport.getScreenHeight()/200 +1.5f );
        game.menuFont.draw(game.batch, "Add game :)", game.viewport.getScreenWidth()/200 - 2, game.viewport.getScreenHeight()/200 + 0.5f);


        game.batch.end();

        // Cycle through screens for testing, remove later
        if (Gdx.input.justTouched()) {

            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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
