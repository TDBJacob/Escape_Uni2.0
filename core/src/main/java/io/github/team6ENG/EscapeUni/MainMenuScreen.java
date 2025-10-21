package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final Skin skin;
    private final Texture background;
    private final GlyphLayout layout;

    private final TextButton playButton;
    private final TextButton exitButton;

    private static final String TITLE_TEXT = "Escape University Of York";

    public MainMenuScreen(final Main game, float timeRemaining) {
        this.game = game;
        this.layout = new GlyphLayout();

        if (Gdx.files.internal("mainMenu/menuBackground.png").exists()) {
            background = new Texture(Gdx.files.internal("mainMenu/menuBackground.png"));
        } else {
            background = null;
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = game.buttonSkin;

        playButton = createButton("Play");
        exitButton = createButton("Exit");

        stage.addActor(playButton);
        stage.addActor(exitButton);

        positionButtons();
        addListeners();
    }

    public MainMenuScreen(final Main game) {
        this(game, 300f); // call the existing one with default value
    }

    private TextButton createButton(String text) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(1.6f);
        button.pad(25f);
        button.setSize(320, 100);
        button.setColor(new Color(0.0f, 0.95f, 0.95f, 1f)); // turquoise base
        return button;
    }

    private void positionButtons() {
        float w = stage.getViewport().getWorldWidth();
        float h = stage.getViewport().getWorldHeight();

        playButton.setPosition(w / 2f - playButton.getWidth() / 2f, h / 2f + 20);
        exitButton.setPosition(w / 2f - exitButton.getWidth() / 2f, h / 2f - 120);
    }

    private void addListeners() {
        Color normalColor = new Color(0.0f, 0.95f, 0.95f, 1f);
        Color clickColor = new Color(0.4f, 1f, 1f, 1f);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButton.setColor(clickColor);
                Gdx.app.postRunnable(() -> game.setScreen(new CharacterSelectScreen(game)));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.setColor(clickColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.setColor(normalColor);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitButton.setColor(clickColor);
                Gdx.app.postRunnable(Gdx.app::exit);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitButton.setColor(clickColor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                exitButton.setColor(normalColor);
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
