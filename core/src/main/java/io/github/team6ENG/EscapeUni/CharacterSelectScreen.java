package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CharacterSelectScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final Skin skin;
    private final Texture background;
    private final GlyphLayout layout;
    private final ImageButton character1;
    private final ImageButton character2;
    private final TextButton backButton;

    private static final String TITLE_TEXT = "Select Your Character";

    public CharacterSelectScreen(final Main game) {
        this.game = game;
        this.layout = new GlyphLayout();


        // background image same as main menu for now
        if (Gdx.files.internal("mainMenu/menuBackground.png").exists()) {
            background = new Texture(Gdx.files.internal("mainMenu/menuBackground.png"));
        } else {
            background = null;
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = game.buttonSkin;

        Texture femaleTex = new Texture(Gdx.files.internal("characterSelection/femaleCharacter.png"));
        Texture maleTex = new Texture(Gdx.files.internal("characterSelection/maleCharacter.png"));

        character1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(femaleTex)));
        character2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(maleTex)));

        character1.setSize(150, 150);  // width, height
        character2.setSize(150, 150);
        backButton = createButton("Back");

        // add buttons to stage
        stage.addActor(character1);
        stage.addActor(character2);
        stage.addActor(backButton);

        positionButtons();
        addListeners();
    }

    private TextButton createButton(String text) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(1.4f); // smaller text
        button.pad(20f); // tighter padding
        button.setSize(260, 85); // smaller overall button size
        button.setColor(new Color(0.0f, 0.95f, 0.95f, 1f)); // turquoise
        return button;
    }

    private void positionButtons() {
        float w = stage.getViewport().getWorldWidth();
        float h = stage.getViewport().getWorldHeight();

        character1.setPosition(w / 2f - 120 - character1.getWidth() / 2f, h / 2f - character1.getHeight() / 2f);
        character2.setPosition(w / 2f + 120 - character2.getWidth() / 2f, h / 2f - character2.getHeight() / 2f);

        backButton.setPosition(w / 2f - backButton.getWidth() / 2f, h / 2f - 180);
    }



    private void addListeners() {
        Color normalColor = new Color(0.0f, 0.95f, 0.95f, 1f);
        Color clickColor = new Color(0.4f, 1f, 1f, 1f);

        character1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                character1.setColor(clickColor);
                game.activeSpritePath = "sprites/femaleSprite.png";
                Gdx.app.postRunnable(() -> game.setScreen(new GameScreen(game)));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                character1.setColor(clickColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                character1.setColor(normalColor);
            }
        });

        character2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                character2.setColor(clickColor);
                game.activeSpritePath = "sprites/maleSprite.png";
                Gdx.app.postRunnable(() -> game.setScreen(new GameScreen(game)));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                character2.setColor(clickColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                character2.setColor(normalColor);
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backButton.setColor(clickColor);
                Gdx.app.postRunnable(() -> game.setScreen(new MainMenuScreen(game, 300f)));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                backButton.setColor(clickColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                backButton.setColor(normalColor);
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.getViewport().apply();
        game.batch.setProjectionMatrix(stage.getCamera().combined);

        game.batch.begin();

        float w = stage.getViewport().getWorldWidth();
        float h = stage.getViewport().getWorldHeight();

        if (background != null) {
            game.batch.draw(background, 0, 0, w, h);
        }

        float brightness = 0.85f + 0.15f * (float) Math.sin(TimeUtils.millis() / 500f);
        game.menuFont.setColor(brightness, brightness, brightness, 1f);
        layout.setText(game.menuFont, TITLE_TEXT);
        game.menuFont.draw(game.batch, TITLE_TEXT, (w - layout.width) / 2f, h * 0.82f);
        game.menuFont.setColor(Color.WHITE);

        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        positionButtons();
    }

    @Override public void show() {

    }
    @Override public void hide() {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        stage.dispose();
    }
}
