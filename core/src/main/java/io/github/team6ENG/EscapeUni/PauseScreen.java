package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {

    private final Main game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Skin skin;

    private final Slider volumeSlider;
    private final TextButton continueButton;
    private final TextButton mainMenuButton;

    public PauseScreen(final Main game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());
        this.skin = game.buttonSkin;

        Gdx.input.setInputProcessor(stage);

        // title
        Label titleLabel = new Label("Paused", new Label.LabelStyle(game.menuFont, Color.WHITE));

        // music slider
        Label volumeLabel = new Label("Music Volume", new Label.LabelStyle(game.menuFont, Color.WHITE));
        volumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        volumeSlider.setValue(0.5f);

        continueButton = createButton("Continue");
        mainMenuButton = createButton("Main Menu");

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        table.add(titleLabel).padBottom(50f).row();
        table.add(volumeLabel).padBottom(10f).row();
        table.add(volumeSlider).width(300).padBottom(40f).row();
        table.add(continueButton).width(250).height(90).padBottom(20f).row();
        table.add(mainMenuButton).width(250).height(90).padBottom(20f).row();

        addListeners();
    }

    private TextButton createButton(String text) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(1.3f);
        button.setColor(new Color(0.0f, 0.95f, 0.95f, 1f)); // turquoise color
        return button;
    }

    private void addListeners() {
        // Continue button returns to same paused game
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setPaused(false);
                game.setScreen(gameScreen);
            }
        });

        // Main menu button goes back to main menu
        mainMenuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {

    }
    @Override public void hide() {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void dispose() { stage.dispose();
    }
}
