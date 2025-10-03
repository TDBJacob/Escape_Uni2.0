package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Stack;

public class CharacterSelectScreen implements Screen {


    private final Main game;
    private Stage stage;
    private Table table;

    public CharacterSelectScreen(final Main game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //placeholder buttons, maybe change to image button when we have sprites
        final TextButton characterButton1 = new TextButton("Character 1", game.buttonSkin);
        table.add(characterButton1).expand().bottom().width(300).height(100).pad(20);
        TextButton characterButton2 = new TextButton("Character 2", game.buttonSkin);
        table.add(characterButton2).expand().bottom().width(300).height(100).pad(20);


        characterButton1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Set sprite here
                game.setScreen(new GameScreen(game));
                dispose();
            } });
        characterButton2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Set sprite here
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
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

        stage.act();
        stage.draw();


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
