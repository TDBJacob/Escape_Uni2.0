package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class CharacterSelectScreen implements Screen {


    private final Main game;

    public CharacterSelectScreen(final Main game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.GREEN);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        game.menuFont.draw(game.batch, "Character selection screen", game.viewport.getScreenWidth()/200 - 2, game.viewport.getScreenHeight()/200 +1.5f );
        game.menuFont.draw(game.batch, "Display 2 characters to choose from", game.viewport.getScreenWidth()/200 - 3, game.viewport.getScreenHeight()/200 + 0.5f);


        game.batch.end();


        if (Gdx.input.justTouched()) {

            game.setScreen(new GameScreen(game));
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
