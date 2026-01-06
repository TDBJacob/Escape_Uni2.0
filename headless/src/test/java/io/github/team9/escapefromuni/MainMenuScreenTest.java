package io.github.team9.escapefromuni;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MainMenuScreenTest {

    @BeforeAll
    public static void init() {
        if (Gdx.gl == null) {
            HeadlessApplicationConfiguration hAConfig = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, hAConfig);
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = Gdx.gl;
        }
    }

    @Test
    public void testConstructor() {
        Main game = mock(Main.class);
        MainMenuScreen screen = new MainMenuScreen(game);
        assertNotNull(screen);
    }

    @Test
    public void testShow() {
        Main game = mock(Main.class);
        MainMenuScreen screen = new MainMenuScreen(game);
        // show() initializes stage, but in headless, might need mocks.
        // For simplicity, test that it doesn't throw.
        screen.show();
    }
}