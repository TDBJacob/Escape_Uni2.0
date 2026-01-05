package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * screen displayed when player wins
 */
public class WinScreen implements Screen {

    private final Main game;

    // stage and resources created in show() and disposed in dispose()
    private Stage stage;
    private Skin skin;
    private final GlyphLayout layout = new GlyphLayout();

    private TextButton exitButton;
    private TextButton mainMenuButton;
    private TextButton submitButton;

    private TextField nameField;

    private com.badlogic.gdx.InputProcessor previousInputProcessor; // used to restore on hide()

    private static final String TITLE_TEXT = "Congratulations, you escaped university :)";

    private ArrayList<LeaderboardEntry> leaderboardEntries;

    private boolean enteredName;
    private boolean hasSetUpLeaderboard;

    /**
     * initialise win screen
     * @param game current Instance of Main
     */
    public WinScreen(final Main game) {
        this.game = game;
        // DO NOT initialize stage/input here — do it in show()
    }

    @Override
    public void show() {
        // create stage with a fixed virtual size (you used 800x450)
        stage = new Stage(game.viewport);

        // remember previous input processor so we can restore it later
        previousInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);

        // Prefer shared skin from game (do NOT dispose it later)
        skin = game.buttonSkin;

        enteredName = false;

        // build UI
        setupUI();
    }

    private void initialiseLeaderboard() {
        //leaderboardEntries.add(new LeaderboardEntry("aaa",120));
        //leaderboardEntries.add(new LeaderboardEntry("bbb",122));
        //leaderboardEntries.add(new LeaderboardEntry("cccc",156));
        //leaderboardEntries.add(new LeaderboardEntry("ddd",120));
        //leaderboardEntries.add(new LeaderboardEntry("eee",325));
        //leaderboardEntries.add(new LeaderboardEntry("ff",775));
        //leaderboardEntries.add(new LeaderboardEntry("ggg",234));
        //leaderboardEntries.add(new LeaderboardEntry("hhh",435));
        //leaderboardEntries.add(new LeaderboardEntry("i",123));
        //leaderboardEntries.add(new LeaderboardEntry("jjjjj",233));
        //leaderboardEntries.add(new LeaderboardEntry("kkk",320));

        Collections.sort(leaderboardEntries); // sort the entries by score, descending
    }

    private void setupUI() {
        exitButton = createButton("Exit");
        mainMenuButton = createButton("Main Menu");
        submitButton = createButton("Submit Name");
        submitButton.setSize(280, 50);

        stage.addActor(exitButton);
        stage.addActor(mainMenuButton);
        stage.addActor(submitButton);

        nameField = new TextField("", skin);
        nameField.setMessageText("Enter your name");
        nameField.setMaxLength(5);
        nameField.setY(200);
        nameField.setX(420);

        stage.addActor(nameField);

        leaderboardEntries = new ArrayList<>();

        positionButtons();
        addListeners();
    }

    private TextButton createButton(String text) {
        // If skin is null, fallback to a simple TextButton may fail; ensure game.buttonSkin exists in assets
        TextButton button = new TextButton(text, skin);
        button.getLabel().setFontScale(1.6f);
        button.pad(25f);
        button.setSize(320, 100);
        button.setColor(new Color(0.0f, 0.95f, 0.95f, 1f));
        return button;
    }

    private void positionButtons() {
        float w = stage.getViewport().getWorldWidth();
        float h = stage.getViewport().getWorldHeight();

        //mainMenuButton.setPosition((w - mainMenuButton.getWidth()) / 2f, h / 2f -60);
        //exitButton.setPosition((w - exitButton.getWidth()) / 2f, h / 2f -170);

        mainMenuButton.setPosition(20, h / 2f -60);
        exitButton.setPosition(20, h / 2f -170);
        submitButton.setPosition(360, h / 2f -170);
    }

    private void addListeners() {
        Color normalColor = new Color(0.0f, 0.95f, 0.95f, 1f);
        Color clickColor = new Color(0.4f, 1f, 1f, 1f);


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
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuButton.setColor(clickColor);
                dispose();
                game.resetGame();
            }
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                mainMenuButton.setColor(clickColor);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                mainMenuButton.setColor(normalColor);
            }
        });
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submitName();
            }
        });
    }

    private void submitName() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) return;

        leaderboardEntries.add(new LeaderboardEntry(name, (int)game.score));

        submitButton.setVisible(false);
        nameField.setVisible(false);

        enteredName = true;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // update stage
        if (stage != null) {
            stage.act(delta);
        }

        // Draw background + title using game's batch (aligned to stage camera)
        if (stage != null) {
            game.batch.setProjectionMatrix(stage.getCamera().combined);
        } else {
            game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        }

        game.batch.begin();
        float w = (stage != null) ? stage.getViewport().getWorldWidth() : game.viewport.getWorldWidth();
        float h = (stage != null) ? stage.getViewport().getWorldHeight() : game.viewport.getWorldHeight();

        if (enteredName) {

            if (!hasSetUpLeaderboard) {
                initialiseLeaderboard();
                hasSetUpLeaderboard = true;
            }

            float brightness = 0.85f + 0.15f * (float) Math.sin(TimeUtils.millis() / 500f);
            if (game.menuFont != null) {
                game.menuFont.setColor(brightness, brightness, brightness, 1f);
                layout.setText(game.menuFont, TITLE_TEXT);
                game.menuFont.draw(game.batch, TITLE_TEXT, (w - layout.width) / 2f, h * 0.82f);
                game.menuFont.setColor(Color.WHITE);

                layout.setText(game.menuFont, "Score: " + (int) game.score);
                game.menuFont.draw(game.batch, ("Score: " + (int) game.score), (w - layout.width) / 2f, h * 0.7f);

                for (int i = 0; i < Math.min(leaderboardEntries.size(), 10); i++) {

                    LeaderboardEntry entry = leaderboardEntries.get(i);

                    String entryText = entry.entryName + ": " + (int) entry.score;

                    layout.setText(game.menuFont, entryText);
                    game.menuFont.draw(game.batch, entryText, w / 2 + (230 - layout.width), h * 0.6f - 22 * i);
                }
            }
        } else {
            // take name input for leaderboard

        }

        game.batch.end();

        if (stage != null) stage.draw();

        // allow quick keyboard start (space)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Gdx.app.postRunnable(() -> game.setScreen(new CharacterSelectScreen(game)));
        }
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.getViewport().update(width, height, true);
        positionButtons();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }

        if (previousInputProcessor != null) {
            Gdx.input.setInputProcessor(previousInputProcessor);
            previousInputProcessor = null;
        }
    }

    @Override
    public void dispose() {
        // Dispose only things we created here
        if (stage != null) {
            stage.dispose();
            stage = null;
        }


        // DO NOT dispose game.menuFont or game.buttonSkin or game.batch here
    }
}
