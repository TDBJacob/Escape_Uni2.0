package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class CharacterSelectScreen implements Screen {


    private final Main game;
    private Stage stage;

    public CharacterSelectScreen(final Main game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.bottom();
        table.defaults().pad(20).fillX().uniformX();

        // 动态字体大
        BitmapFont buttonFont = new BitmapFont(); // 如果使用 TTF 用 FreeTypeFontGenerator
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

        //placeholder buttons, maybe change to image button when we have sprites
        TextButton characterButton1 = new TextButton("Character 1", game.buttonSkin);
        TextButton characterButton2 = new TextButton("Character 2", game.buttonSkin);
        
        table.add(characterButton1).height(Value.percentHeight(0.15f, table)).expandX();
        
        table.add(characterButton2).height(Value.percentHeight(0.15f, table)).expandX();


        characterButton1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Set sprite here
                game.activeSpritePath = "sprites/femaleSprite.png";
                game.setScreen(new GameScreen(game));
                dispose();
            } });
        characterButton2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Set sprite here
                game.activeSpritePath = "sprites/maleSprite.png";
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


        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        String title = "Character select screen";
        GlyphLayout layout = new GlyphLayout(game.menuFont, title);
        float titleX = (worldWidth - layout.width) / 2;
        game.menuFont.draw(game.batch, title, titleX, worldHeight * 0.7f);

        String subtitle = "Display 2 characters to choose from";
        GlyphLayout subtitleLayout = new GlyphLayout(game.menuFont, subtitle);
        float subtitleX = (worldWidth - subtitleLayout.width) / 2;
        game.menuFont.draw(game.batch, subtitle, subtitleX, worldHeight * 0.6f);

        game.batch.end();

        stage.act();
        stage.draw();


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
