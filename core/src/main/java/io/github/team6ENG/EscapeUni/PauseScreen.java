package io.github.team6ENG.EscapeUni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen implements Screen {

    private final Main game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Skin skin;

    private final Slider musicSlider;
    private final Slider volumeSlider;
    private final TextButton continueButton;

    public PauseScreen(final Main game, final GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport());
        this.skin = game.buttonSkin;

        Gdx.input.setInputProcessor(stage);

        // title
        Label titleLabel = new Label("Paused", new Label.LabelStyle(game.menuFont, Color.WHITE));

        // music slider
        Label musicLabel = new Label("Music Volume", new Label.LabelStyle(game.menuFont, Color.WHITE));
        musicSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicSlider.setValue(game.musicVolume);
        // volume slider
        Label volumeLabel = new Label("Sound Volume", new Label.LabelStyle(game.menuFont, Color.WHITE));
        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(game.gameVolume);

        continueButton = createButton("Continue");

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        table.add(titleLabel).padBottom(50f).row();
        table.add(musicLabel).padBottom(10f).row();
        table.add(musicSlider).width(300).padBottom(40f).row();
        table.add(volumeLabel).padBottom(10f).row();
        table.add(volumeSlider).width(300).padBottom(40f).row();
        table.add(continueButton).width(250).height(90).padBottom(20f).row();

        addListeners();
    }

    private TextButton  createButton(String text) {
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(1.3f);
        button.setColor(new Color(0.0f, 0.95f, 0.95f, 1f)); // turquoise color
        return button;
    }

    private void addListeners() {
        // Continue button returns to same paused game
        continueButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.unpause();
                game.setScreen(gameScreen);
            }
        });
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.musicVolume = musicSlider.getValue();
            }

        });
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameVolume = volumeSlider.getValue();
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
