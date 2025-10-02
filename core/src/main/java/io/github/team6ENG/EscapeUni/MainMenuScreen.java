package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Main game;

    public MainMenuScreen(final Main game) {

        this.game = game;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.menuFont.draw(game.batch, "Escape York University", game.viewport.getScreenWidth()/200 - 2, game.viewport.getScreenHeight()/200 +1.5f );
        game.menuFont.draw(game.batch, "Click anywhere to play", game.viewport.getScreenWidth()/200 - 2, game.viewport.getScreenHeight()/200 + 0.5f);


        game.batch.end();

        if (Gdx.input.isTouched()) {

            game.setScreen(new GameScreen(game));
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

